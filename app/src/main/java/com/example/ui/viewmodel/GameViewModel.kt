package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.entities.MatchHistoryEntity
import com.example.data.model.Achievement
import com.example.data.model.AnimeCharacter
import com.example.data.model.GameDifficulty
import com.example.data.model.GamePhase
import com.example.data.model.GameSettings
import com.example.data.model.Player
import com.example.data.model.UserProfile
import com.example.data.model.WinnerTeam
import com.example.data.repository.CharacterRepository
import com.example.data.repository.UserProfileRepository
import com.example.engine.BotAiController
import com.example.engine.GameEngine
import com.example.engine.GameState
import com.example.engine.SoundController
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    CREATE_ROOM,
    JOIN_ROOM,
    LOBBY,
    GAMEPLAY,
    CODEX,
    PROFILE,
    SETTINGS
}

data class UiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val userProfile: UserProfile = UserProfile(),
    val gameState: GameState = GameState(),
    val allCharacters: List<AnimeCharacter> = emptyList(),
    val recentMatches: List<MatchHistoryEntity> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val selectedSuspectId: String? = null,
    val currentClueInput: String = "",
    val clueError: String? = null,
    val imposterGuessInput: String = "",
    val toastMessage: String? = null,
    val isSoundEnabled: Boolean = true,
    val isHapticsEnabled: Boolean = true,
    val isCreatingRoom: Boolean = false,
    val selectedCodexCategory: String = "All Categories",
    val customCharacterName: String = "",
    val customCharacterAnime: String = "",
    val customCharacterCategory: String = "Shonen",
    val customCharacterTraits: String = "",
    val customCharacterAbilities: String = ""
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val characterRepository = CharacterRepository(database.characterDao())
    private val userProfileRepository = UserProfileRepository(database.userProfileDao(), database.matchHistoryDao())
    private val soundController = SoundController(application)
    private val gameEngine = GameEngine()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var botTurnJob: Job? = null

    init {
        viewModelScope.launch {
            characterRepository.initializePresetCharactersIfNeeded()
        }

        viewModelScope.launch {
            characterRepository.allCharacters.collect { list ->
                _uiState.update { it.copy(allCharacters = list) }
            }
        }

        viewModelScope.launch {
            userProfileRepository.userProfile.collect { profile ->
                val achs = userProfileRepository.computeAchievements(profile)
                _uiState.update { it.copy(userProfile = profile, achievements = achs) }
            }
        }

        viewModelScope.launch {
            userProfileRepository.recentMatches.collect { matches ->
                _uiState.update { it.copy(recentMatches = matches) }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        soundController.playClick()
        _uiState.update { it.copy(currentScreen = screen, clueError = null) }
    }

    fun showToast(message: String) {
        _uiState.update { it.copy(toastMessage = message) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun updateProfile(username: String, avatarId: String) {
        viewModelScope.launch {
            userProfileRepository.updateUsernameAndAvatar(username, avatarId)
            soundController.playClick()
            showToast("Profile updated!")
        }
    }

    // --- Lobby & Room Actions ---

    fun createRoom(settings: GameSettings = GameSettings()) {
        val profile = _uiState.value.userProfile
        val host = Player(
            id = profile.id,
            username = profile.username,
            avatarId = profile.avatarId,
            level = profile.level,
            isHost = true,
            isReady = true
        )
        val state = gameEngine.createRoom(host, settings)
        // Add 5 default bot players to make a full 6-player lobby ready out of the box!
        gameEngine.fillWithBotsUpTo(6)
        syncGameState()
        navigateTo(AppScreen.LOBBY)
    }

    fun startQuickMatch() {
        createRoom(GameSettings(playerLimit = 6, clueRoundsBeforeVoting = 2))
        showToast("Quick match created! AI competitors assembled.")
    }

    fun startPracticeMode() {
        createRoom(GameSettings(playerLimit = 5, clueRoundsBeforeVoting = 2, imposterGuessingEnabled = true))
        showToast("Practice session with AI detectives started.")
    }

    fun joinRoomByCode(code: String) {
        if (code.isBlank() || code.length < 4) {
            showToast("Please enter a valid 6-character room code.")
            return
        }
        // In local/simulated multiplayer mode: create or connect to room
        val profile = _uiState.value.userProfile
        val localPlayer = Player(
            id = profile.id,
            username = profile.username,
            avatarId = profile.avatarId,
            level = profile.level
        )
        gameEngine.createRoom(localPlayer, GameSettings(), customRoomCode = code.uppercase())
        gameEngine.fillWithBotsUpTo(6)
        syncGameState()
        navigateTo(AppScreen.LOBBY)
        showToast("Joined Room $code!")
    }

    fun addBot() {
        gameEngine.addBotPlayer()
        soundController.playClick()
        syncGameState()
    }

    fun removePlayer(playerId: String) {
        gameEngine.removePlayer(playerId)
        soundController.playClick()
        syncGameState()
    }

    fun toggleReady() {
        val myId = _uiState.value.userProfile.id
        gameEngine.togglePlayerReady(myId)
        soundController.playClick()
        syncGameState()
    }

    fun updateSettings(settings: GameSettings) {
        gameEngine.updateSettings(settings)
        syncGameState()
    }

    fun startGame() {
        val characters = _uiState.value.allCharacters
        val (success, message) = gameEngine.startGame(characters)
        if (!success) {
            showToast(message)
            return
        }
        soundController.playVictory()
        syncGameState()
        navigateTo(AppScreen.GAMEPLAY)
        startPhaseTimer()
    }

    // --- In-Game Deduction Loop & Timers ---

    private fun startPhaseTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val current = gameEngine.state
                val remaining = current.timerSecondsRemaining

                if (remaining > 1) {
                    gameEngine.state = current.copy(timerSecondsRemaining = remaining - 1)
                    if (remaining - 1 <= 5) {
                        soundController.playTimerTick()
                    }
                    syncGameState()
                } else {
                    // Timer expired: Advance phase
                    handlePhaseTimeout()
                    break
                }
            }
        }

        // Trigger bot turn if needed in CLUE_ROUND or VOTING
        triggerBotActionIfNeeded()
    }

    private fun handlePhaseTimeout() {
        val current = gameEngine.state
        when (current.phase) {
            GamePhase.ROLE_REVEAL -> {
                gameEngine.advanceFromRoleReveal()
                syncGameState()
                startPhaseTimer()
            }
            GamePhase.CHARACTER_REVEAL -> {
                gameEngine.advanceFromCharacterReveal()
                soundController.playTurnNotification()
                syncGameState()
                startPhaseTimer()
            }
            GamePhase.CLUE_ROUND -> {
                // Auto-submit clue for current player
                val currentTurnPlayer = current.currentPlayerTurn
                if (currentTurnPlayer != null) {
                    if (currentTurnPlayer.isBot) {
                        val botClue = BotAiController.generateClueForBot(
                            currentTurnPlayer,
                            current.character,
                            current.clues,
                            current.clueRoundIndex
                        )
                        gameEngine.submitClue(currentTurnPlayer.id, botClue, isAutoSubmitted = false)
                    } else {
                        gameEngine.submitClue(currentTurnPlayer.id, "No clue given in time.", isAutoSubmitted = true)
                    }
                }
                syncGameState()
                startPhaseTimer()
            }
            GamePhase.CLUE_FEED -> {
                gameEngine.advanceFromClueFeed()
                if (gameEngine.state.phase == GamePhase.VOTING) {
                    soundController.playVotingTension()
                }
                syncGameState()
                startPhaseTimer()
            }
            GamePhase.VOTING -> {
                gameEngine.handleVotingTimeout()
                soundController.playVotingTension()
                syncGameState()
                startPhaseTimer()
            }
            GamePhase.VOTE_RESULTS -> {
                if (current.tiedPlayerIds.isNotEmpty()) {
                    gameEngine.triggerRevote()
                    soundController.playVotingTension()
                } else {
                    // Proceed to elimination
                    gameEngine.advanceFromElimination()
                }
                syncGameState()
                startPhaseTimer()
            }
            GamePhase.ELIMINATION -> {
                soundController.playElimination()
                gameEngine.advanceFromElimination()
                if (gameEngine.state.phase == GamePhase.GAME_OVER) {
                    onGameOver()
                }
                syncGameState()
                startPhaseTimer()
            }
            GamePhase.IMPOSTER_GUESS -> {
                // Skip if timeout
                gameEngine.skipImposterGuess()
                if (gameEngine.state.phase == GamePhase.GAME_OVER) {
                    onGameOver()
                }
                syncGameState()
                startPhaseTimer()
            }
            GamePhase.GAME_OVER -> {
                // End loop
            }
            GamePhase.LOBBY -> {}
        }
    }

    private fun triggerBotActionIfNeeded() {
        botTurnJob?.cancel()
        val current = gameEngine.state
        val myId = _uiState.value.userProfile.id

        if (current.phase == GamePhase.CLUE_ROUND) {
            val turnPlayer = current.currentPlayerTurn
            if (turnPlayer != null && turnPlayer.isBot) {
                botTurnJob = viewModelScope.launch {
                    delay(1500) // Realistic bot think delay
                    val clue = BotAiController.generateClueForBot(
                        turnPlayer,
                        current.character,
                        current.clues,
                        current.clueRoundIndex
                    )
                    gameEngine.submitClue(turnPlayer.id, clue, isAutoSubmitted = false)
                    soundController.playClueSubmitted()
                    syncGameState()
                    startPhaseTimer()
                }
            } else if (turnPlayer != null && turnPlayer.id == myId) {
                soundController.playTurnNotification()
            }
        } else if (current.phase == GamePhase.VOTING) {
            // Bots cast votes automatically after realistic staggered delays
            botTurnJob = viewModelScope.launch {
                val botsToVote = current.activeAlivePlayers.filter { it.isBot && current.currentVotes.none { v -> v.voterId == it.id } }
                for (bot in botsToVote) {
                    delay(800)
                    val target = BotAiController.pickVoteForBot(
                        bot,
                        current.activeAlivePlayers,
                        current.clues,
                        current.imposterPlayerId
                    )
                    gameEngine.submitVote(bot.id, target.id)
                    syncGameState()
                }
            }
        }
    }

    fun onClueInputChanged(input: String) {
        _uiState.update { it.copy(currentClueInput = input, clueError = null) }
    }

    fun submitMyClue() {
        val myId = _uiState.value.userProfile.id
        val input = _uiState.value.currentClueInput.trim()
        val (success, message) = gameEngine.submitClue(myId, input, isAutoSubmitted = false)
        if (!success) {
            _uiState.update { it.copy(clueError = message) }
            showToast(message)
            return
        }
        soundController.playClueSubmitted()
        _uiState.update { it.copy(currentClueInput = "", clueError = null) }
        syncGameState()
        startPhaseTimer()
    }

    fun onSelectSuspect(suspectId: String) {
        soundController.playClick()
        _uiState.update { it.copy(selectedSuspectId = suspectId) }
    }

    fun submitMyVote() {
        val myId = _uiState.value.userProfile.id
        val targetId = _uiState.value.selectedSuspectId ?: run {
            showToast("Please choose a suspect to vote for.")
            return
        }
        val (success, message) = gameEngine.submitVote(myId, targetId)
        if (!success) {
            showToast(message)
            return
        }
        soundController.playClick()
        _uiState.update { it.copy(selectedSuspectId = null) }
        syncGameState()
        startPhaseTimer()
    }

    fun onImposterGuessInputChanged(input: String) {
        _uiState.update { it.copy(imposterGuessInput = input) }
    }

    fun submitImposterGuess() {
        val guess = _uiState.value.imposterGuessInput.trim()
        if (guess.isBlank()) {
            showToast("Please enter a character name.")
            return
        }
        gameEngine.submitImposterGuess(guess)
        soundController.playVictory()
        _uiState.update { it.copy(imposterGuessInput = "") }
        if (gameEngine.state.phase == GamePhase.GAME_OVER) {
            onGameOver()
        }
        syncGameState()
        startPhaseTimer()
    }

    fun skipImposterGuess() {
        gameEngine.skipImposterGuess()
        soundController.playClick()
        if (gameEngine.state.phase == GamePhase.GAME_OVER) {
            onGameOver()
        }
        syncGameState()
        startPhaseTimer()
    }

    private fun onGameOver() {
        val state = gameEngine.state
        val myId = _uiState.value.userProfile.id
        val wasImposter = (state.imposterPlayerId == myId)
        val didWin = (wasImposter && state.winnerTeam == WinnerTeam.IMPOSTER) ||
                (!wasImposter && state.winnerTeam == WinnerTeam.PLAYERS)

        val charName = state.character?.name ?: "Unknown Character"
        val animeName = state.character?.anime ?: "Unknown Anime"
        val category = state.character?.category ?: "Shonen"

        soundController.playVictory()

        viewModelScope.launch {
            val (updatedProfile, xpEarned) = userProfileRepository.recordGameResult(
                wasImposter = wasImposter,
                didWin = didWin,
                characterName = charName,
                animeName = animeName,
                category = category,
                survivedRounds = state.activeRoundNumber
            )
            showToast("Match finished! Earned +$xpEarned XP!")
        }
    }

    fun playAgain() {
        val host = _uiState.value.userProfile
        createRoom(gameEngine.state.settings)
        startGame()
    }

    fun returnToLobby() {
        timerJob?.cancel()
        botTurnJob?.cancel()
        gameEngine.state = gameEngine.state.copy(
            phase = GamePhase.LOBBY,
            clues = emptyList(),
            currentVotes = emptyList(),
            voteTallies = emptyMap(),
            eliminatedHistory = emptyList(),
            winnerTeam = WinnerTeam.NONE,
            imposterGuessedCorrectly = false
        )
        syncGameState()
        navigateTo(AppScreen.LOBBY)
    }

    private fun syncGameState() {
        val myId = _uiState.value.userProfile.id
        val sanitized = gameEngine.sanitizeStateForPlayer(myId)
        _uiState.update { it.copy(gameState = sanitized) }
    }

    // --- Codex & Admin ---

    fun filterCodexCategory(category: String) {
        _uiState.update { it.copy(selectedCodexCategory = category) }
    }

    fun onCustomCharacterFieldChanged(
        name: String = _uiState.value.customCharacterName,
        anime: String = _uiState.value.customCharacterAnime,
        category: String = _uiState.value.customCharacterCategory,
        traits: String = _uiState.value.customCharacterTraits,
        abilities: String = _uiState.value.customCharacterAbilities
    ) {
        _uiState.update {
            it.copy(
                customCharacterName = name,
                customCharacterAnime = anime,
                customCharacterCategory = category,
                customCharacterTraits = traits,
                customCharacterAbilities = abilities
            )
        }
    }

    fun addCustomCharacter() {
        val s = _uiState.value
        if (s.customCharacterName.isBlank() || s.customCharacterAnime.isBlank()) {
            showToast("Name and Anime title cannot be empty.")
            return
        }

        val character = AnimeCharacter(
            id = "custom_${System.currentTimeMillis()}",
            name = s.customCharacterName.trim(),
            anime = s.customCharacterAnime.trim(),
            category = s.customCharacterCategory,
            difficulty = "Medium",
            traits = s.customCharacterTraits.split(",").map { it.trim() }.filter { it.isNotBlank() },
            abilities = s.customCharacterAbilities.split(",").map { it.trim() }.filter { it.isNotBlank() },
            isCustom = true
        )

        viewModelScope.launch {
            characterRepository.addCharacter(character)
            soundController.playClick()
            showToast("Added ${character.name} to the Character Codex!")
            _uiState.update {
                it.copy(
                    customCharacterName = "",
                    customCharacterAnime = "",
                    customCharacterTraits = "",
                    customCharacterAbilities = ""
                )
            }
        }
    }

    fun deleteCharacter(id: String) {
        viewModelScope.launch {
            characterRepository.deleteCharacter(id)
            soundController.playClick()
            showToast("Character deleted from Codex.")
        }
    }

    // --- Settings ---

    fun toggleSound(enabled: Boolean) {
        soundController.isSoundEnabled = enabled
        _uiState.update { it.copy(isSoundEnabled = enabled) }
    }

    fun toggleHaptics(enabled: Boolean) {
        soundController.isHapticsEnabled = enabled
        _uiState.update { it.copy(isHapticsEnabled = enabled) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        botTurnJob?.cancel()
        soundController.release()
    }
}
