package com.example.engine

import com.example.data.model.AnimeCharacter
import com.example.data.model.ClueEntry
import com.example.data.model.EliminationEvent
import com.example.data.model.GameDifficulty
import com.example.data.model.GamePhase
import com.example.data.model.GameSettings
import com.example.data.model.Player
import com.example.data.model.TieBreakerRule
import com.example.data.model.VoteEntry
import com.example.data.model.WinnerTeam
import kotlin.random.Random

data class GameState(
    val roomId: String = "LOBBY1",
    val phase: GamePhase = GamePhase.LOBBY,
    val settings: GameSettings = GameSettings(),
    val character: AnimeCharacter? = null,
    val players: List<Player> = emptyList(),
    val imposterPlayerId: String = "",
    val activeRoundNumber: Int = 1,
    val clueRoundIndex: Int = 1,
    val currentPlayerTurnIndex: Int = 0,
    val clues: List<ClueEntry> = emptyList(),
    val currentVotes: List<VoteEntry> = emptyList(),
    val voteTallies: Map<String, Int> = emptyMap(),
    val eliminatedHistory: List<EliminationEvent> = emptyList(),
    val tiedPlayerIds: List<String> = emptyList(),
    val winnerTeam: WinnerTeam = WinnerTeam.NONE,
    val timerSecondsRemaining: Int = 0,
    val imposterGuessedCorrectly: Boolean = false,
    val statusMessage: String = ""
) {
    val activeAlivePlayers: List<Player>
        get() = players.filter { !it.isEliminated && it.isConnected }

    val currentPlayerTurn: Player?
        get() = activeAlivePlayers.getOrNull(currentPlayerTurnIndex)

    val imposterPlayer: Player?
        get() = players.firstOrNull { it.id == imposterPlayerId }
}

class GameEngine {

    var state: GameState = GameState()

    fun createRoom(
        hostPlayer: Player,
        settings: GameSettings = GameSettings(),
        customRoomCode: String? = null
    ): GameState {
        val code = customRoomCode ?: generateRoomCode()
        val host = hostPlayer.copy(isHost = true, isReady = true)
        state = GameState(
            roomId = code,
            phase = GamePhase.LOBBY,
            settings = settings,
            players = listOf(host),
            statusMessage = "Room created. Waiting for players to join..."
        )
        return state
    }

    fun joinRoom(player: Player): Pair<Boolean, String> {
        if (state.phase != GamePhase.LOBBY) {
            return Pair(false, "Cannot join: Game has already started.")
        }
        if (state.players.size >= state.settings.playerLimit) {
            return Pair(false, "Room is full (max ${state.settings.playerLimit} players).")
        }
        if (state.players.any { it.id == player.id }) {
            return Pair(true, "Reconnected to room.")
        }
        val updatedPlayers = state.players + player.copy(isHost = false, isReady = false)
        state = state.copy(
            players = updatedPlayers,
            statusMessage = "${player.username} joined the lobby!"
        )
        return Pair(true, "Joined successfully.")
    }

    fun removePlayer(playerId: String): GameState {
        val remaining = state.players.filter { it.id != playerId }
        val updatedHost = if (remaining.isNotEmpty() && remaining.none { it.isHost }) {
            remaining.mapIndexed { idx, p -> if (idx == 0) p.copy(isHost = true) else p }
        } else {
            remaining
        }
        state = state.copy(
            players = updatedHost,
            statusMessage = "Player left the room."
        )
        return state
    }

    fun togglePlayerReady(playerId: String): GameState {
        val updated = state.players.map {
            if (it.id == playerId) it.copy(isReady = !it.isReady) else it
        }
        state = state.copy(players = updated)
        return state
    }

    fun updateSettings(newSettings: GameSettings): GameState {
        state = state.copy(settings = newSettings)
        return state
    }

    fun addBotPlayer(): GameState {
        if (state.players.size >= state.settings.playerLimit) return state
        val existingNames = state.players.map { it.username }
        val bot = BotAiController.createBot(state.players.size, existingNames)
        state = state.copy(
            players = state.players + bot,
            statusMessage = "Added AI player: ${bot.username}"
        )
        return state
    }

    fun fillWithBotsUpTo(targetCount: Int): GameState {
        var current = state
        while (current.players.size < targetCount && current.players.size < current.settings.playerLimit) {
            current = addBotPlayer()
        }
        return current
    }

    fun startGame(
        availableCharacters: List<AnimeCharacter>
    ): Pair<Boolean, String> {
        if (state.players.size < 4) {
            return Pair(false, "At least 4 players are required to play (Add AI bots or invite friends).")
        }

        // 1. Select character based on category and difficulty
        val filtered = availableCharacters.filter { char ->
            val matchCat = state.settings.category == "All Categories" || char.category.equals(state.settings.category, ignoreCase = true)
            val matchDiff = state.settings.difficulty == GameDifficulty.ALL || char.difficulty.equals(state.settings.difficulty.name, ignoreCase = true)
            matchCat && matchDiff
        }

        val selectedChar = if (filtered.isNotEmpty()) {
            filtered.random()
        } else if (availableCharacters.isNotEmpty()) {
            availableCharacters.random()
        } else {
            return Pair(false, "No anime characters available.")
        }

        // 2. Assign Exactly ONE Imposter
        val randomImposterIndex = Random.nextInt(state.players.size)
        val imposterPlayer = state.players[randomImposterIndex]

        val initializedPlayers = state.players.mapIndexed { idx, p ->
            p.copy(
                isEliminated = false,
                isImposter = (idx == randomImposterIndex),
                votesReceived = 0,
                isReady = true
            )
        }

        state = state.copy(
            phase = GamePhase.ROLE_REVEAL,
            character = selectedChar,
            players = initializedPlayers,
            imposterPlayerId = imposterPlayer.id,
            activeRoundNumber = 1,
            clueRoundIndex = 1,
            currentPlayerTurnIndex = 0,
            clues = emptyList(),
            currentVotes = emptyList(),
            voteTallies = emptyMap(),
            eliminatedHistory = emptyList(),
            tiedPlayerIds = emptyList(),
            winnerTeam = WinnerTeam.NONE,
            timerSecondsRemaining = 6,
            imposterGuessedCorrectly = false,
            statusMessage = "Roles have been secretly assigned!"
        )

        return Pair(true, "Game started successfully.")
    }

    fun advanceFromRoleReveal(): GameState {
        state = state.copy(
            phase = GamePhase.CHARACTER_REVEAL,
            timerSecondsRemaining = 8,
            statusMessage = "Study your character instructions..."
        )
        return state
    }

    fun advanceFromCharacterReveal(): GameState {
        val firstAliveIndex = 0
        state = state.copy(
            phase = GamePhase.CLUE_ROUND,
            currentPlayerTurnIndex = firstAliveIndex,
            timerSecondsRemaining = state.settings.clueTimerSeconds,
            statusMessage = "Clue Round ${state.clueRoundIndex} has begun!"
        )
        return state
    }

    fun submitClue(
        playerId: String,
        rawClue: String,
        isAutoSubmitted: Boolean = false
    ): Pair<Boolean, String> {
        if (state.phase != GamePhase.CLUE_ROUND) {
            return Pair(false, "Not in clue phase.")
        }
        val currentTurnPlayer = state.currentPlayerTurn
        if (currentTurnPlayer == null || currentTurnPlayer.id != playerId) {
            return Pair(false, "Not your turn to give a clue.")
        }
        if (currentTurnPlayer.isEliminated) {
            return Pair(false, "Eliminated players cannot give clues.")
        }

        val clueText = if (isAutoSubmitted && rawClue.isBlank()) {
            "..."
        } else {
            rawClue.trim()
        }

        if (!isAutoSubmitted) {
            val validation = ClueValidator.validateClue(clueText, state.character)
            if (validation is ClueValidator.ValidationResult.Rejected) {
                return Pair(false, validation.reason)
            }
        }

        val newClue = ClueEntry(
            id = "clue_${System.currentTimeMillis()}_$playerId",
            playerId = currentTurnPlayer.id,
            playerName = currentTurnPlayer.username,
            playerAvatarId = currentTurnPlayer.avatarId,
            roundNumber = state.activeRoundNumber,
            clueRoundIndex = state.clueRoundIndex,
            clueText = clueText,
            wasAutoSubmitted = isAutoSubmitted
        )

        val updatedClues = state.clues + newClue
        val nextTurnIndex = state.currentPlayerTurnIndex + 1

        if (nextTurnIndex < state.activeAlivePlayers.size) {
            // Next player's turn in current clue round
            state = state.copy(
                clues = updatedClues,
                currentPlayerTurnIndex = nextTurnIndex,
                timerSecondsRemaining = state.settings.clueTimerSeconds,
                statusMessage = "${state.activeAlivePlayers[nextTurnIndex].username}'s turn to give a clue."
            )
        } else {
            // All alive players gave a clue in this clue round!
            if (state.clueRoundIndex < state.settings.clueRoundsBeforeVoting) {
                // Next Clue Round (e.g. Round 2)
                state = state.copy(
                    clues = updatedClues,
                    clueRoundIndex = state.clueRoundIndex + 1,
                    currentPlayerTurnIndex = 0,
                    phase = GamePhase.CLUE_FEED,
                    timerSecondsRemaining = 6,
                    statusMessage = "All players gave a clue! Preparing Clue Round ${state.clueRoundIndex + 1}..."
                )
            } else {
                // Ready for Voting Phase!
                state = state.copy(
                    clues = updatedClues,
                    phase = GamePhase.CLUE_FEED,
                    timerSecondsRemaining = 6,
                    statusMessage = "All clue rounds complete! Voting will begin shortly..."
                )
            }
        }
        return Pair(true, "Clue submitted.")
    }

    fun advanceFromClueFeed(): GameState {
        if (state.clueRoundIndex < state.settings.clueRoundsBeforeVoting) {
            state = state.copy(
                phase = GamePhase.CLUE_ROUND,
                currentPlayerTurnIndex = 0,
                timerSecondsRemaining = state.settings.clueTimerSeconds,
                statusMessage = "Clue Round ${state.clueRoundIndex} in progress."
            )
        } else {
            // Enter voting phase
            state = state.copy(
                phase = GamePhase.VOTING,
                currentVotes = emptyList(),
                voteTallies = emptyMap(),
                tiedPlayerIds = emptyList(),
                timerSecondsRemaining = state.settings.votingTimerSeconds,
                statusMessage = "Who is the Imposter? Cast your vote!"
            )
        }
        return state
    }

    fun submitVote(voterId: String, targetPlayerId: String): Pair<Boolean, String> {
        if (state.phase != GamePhase.VOTING) {
            return Pair(false, "Not in voting phase.")
        }
        val voter = state.players.firstOrNull { it.id == voterId }
            ?: return Pair(false, "Voter not found.")
        if (voter.isEliminated) {
            return Pair(false, "Eliminated players cannot vote.")
        }
        if (voterId == targetPlayerId) {
            return Pair(false, "You cannot vote for yourself.")
        }
        if (state.currentVotes.any { it.voterId == voterId }) {
            return Pair(false, "You have already cast your vote this round.")
        }

        val target = state.players.firstOrNull { it.id == targetPlayerId }
            ?: return Pair(false, "Target suspect not found.")
        if (target.isEliminated) {
            return Pair(false, "Cannot vote for an already eliminated player.")
        }

        val vote = VoteEntry(
            voterId = voter.id,
            voterName = voter.username,
            targetPlayerId = target.id,
            targetPlayerName = target.username,
            roundNumber = state.activeRoundNumber
        )

        val updatedVotes = state.currentVotes + vote
        state = state.copy(currentVotes = updatedVotes)

        // Check if all alive players have voted
        if (updatedVotes.size >= state.activeAlivePlayers.size) {
            evaluateVoteResults()
        }

        return Pair(true, "Vote cast successfully.")
    }

    fun handleVotingTimeout(): GameState {
        // Auto-vote for remaining players who haven't voted
        val eligibleVoters = state.activeAlivePlayers.filter { alive ->
            state.currentVotes.none { it.voterId == alive.id }
        }

        val additionalVotes = mutableListOf<VoteEntry>()
        for (voter in eligibleVoters) {
            val otherAlive = state.activeAlivePlayers.filter { it.id != voter.id }
            if (otherAlive.isNotEmpty()) {
                val target = otherAlive.random()
                additionalVotes.add(
                    VoteEntry(
                        voterId = voter.id,
                        voterName = voter.username,
                        targetPlayerId = target.id,
                        targetPlayerName = target.username,
                        roundNumber = state.activeRoundNumber
                    )
                )
            }
        }

        state = state.copy(currentVotes = state.currentVotes + additionalVotes)
        evaluateVoteResults()
        return state
    }

    private fun evaluateVoteResults() {
        val tallies = mutableMapOf<String, Int>()
        state.activeAlivePlayers.forEach { tallies[it.id] = 0 }
        state.currentVotes.forEach { vote ->
            tallies[vote.targetPlayerId] = (tallies[vote.targetPlayerId] ?: 0) + 1
        }

        val maxVotes = tallies.values.maxOrNull() ?: 0
        val highestVotedIds = tallies.filter { it.value == maxVotes && maxVotes > 0 }.keys.toList()

        if (highestVotedIds.size > 1) {
            // TIE DETECTED
            if (state.settings.tieBreakerRule == TieBreakerRule.REVOTE && state.tiedPlayerIds.isEmpty()) {
                // First tie: Trigger Revote
                state = state.copy(
                    phase = GamePhase.VOTE_RESULTS,
                    voteTallies = tallies,
                    tiedPlayerIds = highestVotedIds,
                    timerSecondsRemaining = 6,
                    statusMessage = "A tie has occurred between ${highestVotedIds.size} players! Preparing revote..."
                )
                return
            } else {
                // Randomly eliminate one of the tied players
                val chosenEliminatedId = highestVotedIds.random()
                finalizeElimination(chosenEliminatedId, tallies, wasTieBroken = true)
            }
        } else if (highestVotedIds.size == 1) {
            finalizeElimination(highestVotedIds.first(), tallies, wasTieBroken = false)
        } else {
            // Nobody got votes: eliminate random alive player
            val randomId = state.activeAlivePlayers.random().id
            finalizeElimination(randomId, tallies, wasTieBroken = true)
        }
    }

    fun triggerRevote(): GameState {
        state = state.copy(
            phase = GamePhase.VOTING,
            currentVotes = emptyList(),
            voteTallies = emptyMap(),
            timerSecondsRemaining = state.settings.votingTimerSeconds,
            statusMessage = "Revote in progress! Vote between the tied suspects!"
        )
        return state
    }

    private fun finalizeElimination(
        eliminatedId: String,
        tallies: Map<String, Int>,
        wasTieBroken: Boolean
    ) {
        val targetPlayer = state.players.firstOrNull { it.id == eliminatedId } ?: return
        val isImposter = (targetPlayer.id == state.imposterPlayerId)

        val updatedPlayers = state.players.map {
            if (it.id == eliminatedId) it.copy(isEliminated = true) else it
        }

        val event = EliminationEvent(
            eliminatedPlayer = targetPlayer,
            votesReceived = tallies[eliminatedId] ?: 0,
            isImposter = isImposter,
            wasTieBroken = wasTieBroken,
            reason = if (isImposter) "The secret Imposter was caught!" else "An innocent Normal Player was eliminated."
        )

        state = state.copy(
            phase = GamePhase.ELIMINATION,
            players = updatedPlayers,
            voteTallies = tallies,
            eliminatedHistory = state.eliminatedHistory + event,
            timerSecondsRemaining = 6,
            statusMessage = "${targetPlayer.username} was eliminated!"
        )
    }

    fun advanceFromElimination(): GameState {
        val lastElimination = state.eliminatedHistory.lastOrNull()

        // 1. Check if Imposter was eliminated -> Normal Players Win!
        if (lastElimination?.isImposter == true) {
            state = state.copy(
                phase = GamePhase.GAME_OVER,
                winnerTeam = WinnerTeam.PLAYERS,
                statusMessage = "PLAYERS WIN! The Imposter was caught!"
            )
            return state
        }

        // 2. Check if remaining alive count <= 2 (Imposter + 1 Normal Player) -> Imposter Wins!
        if (state.activeAlivePlayers.size <= 2) {
            state = state.copy(
                phase = GamePhase.GAME_OVER,
                winnerTeam = WinnerTeam.IMPOSTER,
                statusMessage = "IMPOSTER WINS! The Imposter survived until the final two!"
            )
            return state
        }

        // 3. If Imposter Guessing is Enabled and Imposter survived voting
        if (state.settings.imposterGuessingEnabled) {
            state = state.copy(
                phase = GamePhase.IMPOSTER_GUESS,
                timerSecondsRemaining = 20,
                statusMessage = "Imposter has survived! They may now attempt a high-stakes character guess."
            )
            return state
        }

        // 4. Continue to next round of clues
        return startNextCycle()
    }

    fun submitImposterGuess(guessedCharacterName: String): GameState {
        val actualChar = state.character
        val cleanGuess = guessedCharacterName.trim().lowercase()

        val isCorrect = if (actualChar != null) {
            val cleanActual = actualChar.name.trim().lowercase()
            val matchesName = cleanGuess == cleanActual || cleanGuess.contains(cleanActual) || cleanActual.contains(cleanGuess)
            val matchesAlias = actualChar.aliases.any { alias ->
                val cleanAlias = alias.trim().lowercase()
                cleanGuess == cleanAlias || cleanGuess.contains(cleanAlias)
            }
            matchesName || matchesAlias
        } else false

        if (isCorrect) {
            state = state.copy(
                phase = GamePhase.GAME_OVER,
                winnerTeam = WinnerTeam.IMPOSTER,
                imposterGuessedCorrectly = true,
                statusMessage = "IMPOSTER GUESS CORRECT! Imposter instantly wins the game!"
            )
        } else {
            // Incorrect guess: Imposter survives but guessing failed -> game continues
            state = state.copy(
                imposterGuessedCorrectly = false,
                statusMessage = "Imposter guess was incorrect! The game continues..."
            )
            startNextCycle()
        }
        return state
    }

    fun skipImposterGuess(): GameState {
        return startNextCycle()
    }

    private fun startNextCycle(): GameState {
        state = state.copy(
            phase = GamePhase.CLUE_ROUND,
            activeRoundNumber = state.activeRoundNumber + 1,
            clueRoundIndex = 1,
            currentPlayerTurnIndex = 0,
            currentVotes = emptyList(),
            voteTallies = emptyMap(),
            tiedPlayerIds = emptyList(),
            timerSecondsRemaining = state.settings.clueTimerSeconds,
            statusMessage = "Round ${state.activeRoundNumber + 1} Clue Phase has begun!"
        )
        return state
    }

    fun sanitizeStateForPlayer(localPlayerId: String): GameState {
        val isLocalImposter = (localPlayerId == state.imposterPlayerId)
        val isGameOver = (state.phase == GamePhase.GAME_OVER)

        // 1. Conceal character if local player is imposter and game is not over
        val sanitizedCharacter = if (isLocalImposter && !isGameOver) {
            null
        } else {
            state.character
        }

        // 2. Conceal player roles from each other (unless game over or revealed upon elimination)
        val sanitizedPlayers = state.players.map { player ->
            val showRole = isGameOver || (player.id == localPlayerId) || (player.isEliminated && state.settings.revealRoleOnElimination)
            if (showRole) {
                player
            } else {
                player.copy(isImposter = false)
            }
        }

        return state.copy(
            character = sanitizedCharacter,
            players = sanitizedPlayers,
            imposterPlayerId = if (isGameOver || isLocalImposter) state.imposterPlayerId else ""
        )
    }

    private fun generateRoomCode(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        return (1..6).map { chars.random() }.joinToString("")
    }
}
