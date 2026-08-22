package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AnimeCharacter
import com.example.data.model.GamePhase
import com.example.data.model.Player
import com.example.data.model.WinnerTeam
import com.example.engine.GameState
import com.example.ui.components.AvatarView
import com.example.ui.components.BadgeChip
import com.example.ui.components.CircularTimer
import com.example.ui.components.ClueCardView
import com.example.ui.components.GlassCard
import com.example.ui.components.HeaderPillTimer
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonOutlineButton
import com.example.ui.components.SuspectVoteCard
import com.example.ui.components.TimerIndicator
import com.example.ui.theme.ArtisticGold
import com.example.ui.theme.ArtisticGradientGold
import com.example.ui.theme.ArtisticGradientMint
import com.example.ui.theme.ArtisticGradientPrimary
import com.example.ui.theme.ArtisticGreen
import com.example.ui.theme.ArtisticLilac
import com.example.ui.theme.ArtisticMint
import com.example.ui.theme.ArtisticOrchid
import com.example.ui.theme.ArtisticRed
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderCyanGlow
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderPinkGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CyberGradientAmber
import com.example.ui.theme.CyberGradientCyan
import com.example.ui.theme.CyberGradientPrimary
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardDeep
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceGlass
import com.example.ui.theme.SurfaceInput
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.UiState

@Composable
fun GameplayScreen(
    uiState: UiState,
    onClueInputChanged: (String) -> Unit,
    onSubmitClue: () -> Unit,
    onSelectSuspect: (String) -> Unit,
    onSubmitVote: () -> Unit,
    onImposterGuessInputChanged: (String) -> Unit,
    onSubmitImposterGuess: () -> Unit,
    onSkipImposterGuess: () -> Unit,
    onPlayAgain: () -> Unit,
    onReturnToLobby: () -> Unit
) {
    val gameState = uiState.gameState
    val myId = uiState.userProfile.id
    val myPlayer = gameState.players.firstOrNull { it.id == myId }
    val isMyTurn = gameState.currentPlayerTurn?.id == myId
    val isImposter = (gameState.imposterPlayerId == myId)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top Phase Header & In-Game Status
            GameTopHeader(
                gameState = gameState,
                myPlayer = myPlayer,
                onLeave = onReturnToLobby
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Main Phase Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (gameState.phase) {
                    GamePhase.ROLE_REVEAL -> {
                        RoleRevealPhase(isImposter = isImposter, secondsLeft = gameState.timerSecondsRemaining)
                    }
                    GamePhase.CHARACTER_REVEAL -> {
                        CharacterRevealPhase(
                            isImposter = isImposter,
                            character = gameState.character,
                            secondsLeft = gameState.timerSecondsRemaining
                        )
                    }
                    GamePhase.CLUE_ROUND -> {
                        ClueRoundPhase(
                            gameState = gameState,
                            myId = myId,
                            isMyTurn = isMyTurn,
                            clueInput = uiState.currentClueInput,
                            clueError = uiState.clueError,
                            onClueInputChanged = onClueInputChanged,
                            onSubmitClue = onSubmitClue
                        )
                    }
                    GamePhase.CLUE_FEED -> {
                        ClueFeedPhase(gameState = gameState)
                    }
                    GamePhase.VOTING -> {
                        VotingPhase(
                            gameState = gameState,
                            myId = myId,
                            selectedSuspectId = uiState.selectedSuspectId,
                            onSelectSuspect = onSelectSuspect,
                            onSubmitVote = onSubmitVote
                        )
                    }
                    GamePhase.VOTE_RESULTS -> {
                        VoteResultsPhase(gameState = gameState)
                    }
                    GamePhase.ELIMINATION -> {
                        EliminationPhase(gameState = gameState)
                    }
                    GamePhase.IMPOSTER_GUESS -> {
                        ImposterGuessPhase(
                            isImposter = isImposter,
                            guessInput = uiState.imposterGuessInput,
                            secondsLeft = gameState.timerSecondsRemaining,
                            onGuessInputChanged = onImposterGuessInputChanged,
                            onSubmitGuess = onSubmitImposterGuess,
                            onSkipGuess = onSkipImposterGuess
                        )
                    }
                    GamePhase.GAME_OVER -> {
                        VictoryPhase(
                            gameState = gameState,
                            myId = myId,
                            onPlayAgain = onPlayAgain,
                            onReturnToLobby = onReturnToLobby
                        )
                    }
                    GamePhase.LOBBY -> {}
                }
            }
        }
    }
}

@Composable
private fun GameTopHeader(
    gameState: GameState,
    myPlayer: Player?,
    onLeave: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderGlow)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "ROOM: ${gameState.roomId}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisticLilac,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ANIME IMPOSTER",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = TextPrimary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BadgeChip(
                        text = "R${gameState.activeRoundNumber}",
                        color = ArtisticLilac,
                        backgroundColor = SurfaceCardDeep
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                HeaderPillTimer(secondsRemaining = gameState.timerSecondsRemaining)

                if (myPlayer?.isEliminated == true) {
                    Spacer(modifier = Modifier.width(6.dp))
                    BadgeChip(text = "SPECTATING", color = ArtisticGold, backgroundColor = SurfaceCardDeep)
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onLeave,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Leave Game",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// 1. Role Reveal Screen
@Composable
private fun RoleRevealPhase(
    isImposter: Boolean,
    secondsLeft: Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(0.92f),
            showGlowOrb = true,
            borderColor = if (isImposter) ArtisticRed.copy(alpha = 0.5f) else ArtisticLilac.copy(alpha = 0.5f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isImposter) "🕵️" else "🔍",
                    fontSize = 54.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "YOUR SECRET ROLE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = ArtisticLilac,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isImposter) "THE IMPOSTER" else "TRUSTED DETECTIVE",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isImposter) ArtisticRed else ArtisticLilac,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceInput,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isImposter)
                            "You do NOT know the anime character! Listen carefully to other players' clues, blend in seamlessly, and avoid detection!"
                        else
                            "You will receive the secret anime character. Give clever, subtle clues without saying the name directly. Unmask the imposter!",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                CircularTimer(secondsRemaining = secondsLeft, totalSeconds = 6)
            }
        }
    }
}

// 2. Character Reveal Screen
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CharacterRevealPhase(
    isImposter: Boolean,
    character: AnimeCharacter?,
    secondsLeft: Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimerIndicator(secondsRemaining = secondsLeft, totalSeconds = 8)

        Spacer(modifier = Modifier.height(12.dp))

        if (isImposter) {
            // Imposter View (Zero character info!)
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                showGlowOrb = true,
                borderColor = ArtisticRed.copy(alpha = 0.4f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Hidden",
                        tint = ArtisticRed,
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "CHARACTER UNKNOWN",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = ArtisticRed,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "You are the shadow in the room.\nPay attention to traits, abilities, and themes mentioned by other detectives to deduce the anime character!",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BadgeChip(text = "STRATEGY: GIVE AMBIGUOUS CLUES", color = ArtisticGold, backgroundColor = SurfaceCardDeep)
                }
            }
        } else if (character != null) {
            // Normal Player View (Rich Character Card!)
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                showGlowOrb = true,
                borderColor = ArtisticLilac.copy(alpha = 0.35f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BadgeChip(text = character.category.uppercase(), color = ArtisticMint, backgroundColor = SurfaceCardDeep)
                            BadgeChip(text = "DIFFICULTY: ${character.difficulty}", color = ArtisticLilac, backgroundColor = SurfaceCardDeep)
                        }
                    }

                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SurfaceInput,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "TARGET ANIME CHARACTER",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = ArtisticLilac,
                                    letterSpacing = 1.5.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = character.name,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Series: ${character.anime}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ArtisticGold
                                )
                            }
                        }
                    }

                    if (character.appearance.isNotBlank()) {
                        item {
                            Text(
                                text = character.appearance,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                lineHeight = 19.sp
                            )
                        }
                    }

                    if (character.traits.isNotEmpty()) {
                        item {
                            Text(
                                text = "Key Lore & Traits:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArtisticLilac
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                character.traits.forEach { trait ->
                                    BadgeChip(text = trait, color = ArtisticMint, backgroundColor = SurfaceCardDeep)
                                }
                            }
                        }
                    }

                    if (character.abilities.isNotEmpty()) {
                        item {
                            Text(
                                text = "Iconic Abilities:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArtisticLilac
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                character.abilities.forEach { ab ->
                                    BadgeChip(text = ab, color = ArtisticGold, backgroundColor = SurfaceCardDeep)
                                }
                            }
                        }
                    }

                    if (character.famousQuote.isNotBlank()) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SurfaceCardElevated,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "« ${character.famousQuote} »",
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Clue phase will begin as soon as timer reaches zero...",
            fontSize = 12.sp,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

// 3. Clue Round Screen
@Composable
private fun ClueRoundPhase(
    gameState: GameState,
    myId: String,
    isMyTurn: Boolean,
    clueInput: String,
    clueError: String?,
    onClueInputChanged: (String) -> Unit,
    onSubmitClue: () -> Unit
) {
    val currentTurnPlayer = gameState.currentPlayerTurn

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TimerIndicator(
                secondsRemaining = gameState.timerSecondsRemaining,
                totalSeconds = gameState.settings.clueTimerSeconds
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Turn Announcement Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = if (isMyTurn) SurfaceCardElevated else SurfaceCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isMyTurn) ArtisticLilac else BorderSubtle)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarView(
                        avatarId = currentTurnPlayer?.avatarId ?: "avatar_1",
                        size = 42.dp,
                        isBot = currentTurnPlayer?.isBot == true
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isMyTurn) "YOUR TURN TO GIVE A CLUE!" else "${currentTurnPlayer?.username}'s Turn",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isMyTurn) ArtisticLilac else TextPrimary
                        )
                        Text(
                            text = if (isMyTurn) "Enter a clever clue without revealing the name" else "Waiting for clue submission...",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    if (isMyTurn) {
                        BadgeChip(text = "ACTIVE", color = TextDark, backgroundColor = ArtisticLilac)
                    } else if (currentTurnPlayer?.isBot == true) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = ArtisticMint, strokeWidth = 2.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CLUE ROUND ${gameState.clueRoundIndex} OF ${gameState.settings.clueRoundsBeforeVoting}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "${gameState.clues.size}/${gameState.activeAlivePlayers.size} Done",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisticMint
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Clues List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .height(280.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (gameState.clues.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No clues given yet in this round.", color = TextMuted, fontSize = 13.sp)
                        }
                    }
                } else {
                    items(gameState.clues) { clue ->
                        ClueCardView(
                            clue = clue,
                            isHighlighted = clue.playerId == currentTurnPlayer?.id,
                            isSelf = clue.playerId == myId
                        )
                    }
                }
            }
        }

        // Bottom Input Area (If my turn)
        Column {
            if (isMyTurn) {
                Surface(
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                    color = SurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderGlow),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "YOUR TURN TO HINT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = ArtisticLilac,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Avoid character names!",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }

                        if (clueError != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = ArtisticRed, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = clueError, color = ArtisticRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = clueInput,
                                onValueChange = onClueInputChanged,
                                placeholder = { Text("e.g. He loves meat and wears a straw hat...", color = TextMuted, fontSize = 13.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ArtisticLilac,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedContainerColor = SurfaceInput,
                                    unfocusedContainerColor = SurfaceInput,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(onSend = { onSubmitClue() }),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("clue_input_field")
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = onSubmitClue,
                                enabled = clueInput.isNotBlank(),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (clueInput.isNotBlank()) ArtisticLilac else SurfaceCardDeep)
                                    .size(50.dp)
                                    .testTag("submit_clue_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Submit Clue",
                                    tint = if (clueInput.isNotBlank()) TextDark else TextMuted
                                )
                            }
                        }
                    }
                }
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCardDeep,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = ArtisticMint, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Listen closely to everyone's clues to spot inconsistencies!",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

// 4. Clue Feed Review Screen
@Composable
private fun ClueFeedPhase(
    gameState: GameState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TimerIndicator(
                secondsRemaining = gameState.timerSecondsRemaining,
                totalSeconds = 6
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "CLUE ROUND COMPLETE",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = ArtisticLilac,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Review all submitted clues before advancing to deliberation.",
                fontSize = 12.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(gameState.clues) { clue ->
                    ClueCardView(clue = clue)
                }
            }
        }
    }
}

// 5. Voting Phase Screen
@Composable
private fun VotingPhase(
    gameState: GameState,
    myId: String,
    selectedSuspectId: String?,
    onSelectSuspect: (String) -> Unit,
    onSubmitVote: () -> Unit
) {
    val myPlayer = gameState.players.firstOrNull { it.id == myId }
    val hasVoted = gameState.currentVotes.any { it.voterId == myId }
    val isEliminated = myPlayer?.isEliminated == true

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TimerIndicator(
                secondsRemaining = gameState.timerSecondsRemaining,
                totalSeconds = gameState.settings.votingTimerSeconds
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "WHO IS THE IMPOSTER?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = ArtisticRed,
                letterSpacing = 1.sp
            )
            Text(
                text = if (isEliminated) "You are eliminated. Spectating the vote."
                else if (hasVoted) "Vote registered! Waiting for other detectives..."
                else "Select the player you suspect did not know the secret character.",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Suspect Cards
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(gameState.activeAlivePlayers) { player ->
                    val isSelf = player.id == myId
                    SuspectVoteCard(
                        player = player,
                        isSelected = selectedSuspectId == player.id,
                        onSelect = { onSelectSuspect(player.id) },
                        enabled = !hasVoted && !isEliminated,
                        isSelf = isSelf
                    )
                }
            }
        }

        // Vote Action Button
        Column {
            if (!hasVoted && !isEliminated) {
                NeonButton(
                    text = "CONFIRM VOTE",
                    onClick = onSubmitVote,
                    enabled = selectedSuspectId != null,
                    modifier = Modifier.fillMaxWidth(),
                    gradient = ArtisticGradientPrimary,
                    testTag = "confirm_vote_button"
                )
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCardDeep,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = ArtisticMint, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Counting votes (${gameState.currentVotes.size}/${gameState.activeAlivePlayers.size})...",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// 6. Vote Results Screen
@Composable
private fun VoteResultsPhase(
    gameState: GameState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TimerIndicator(
                secondsRemaining = gameState.timerSecondsRemaining,
                totalSeconds = 6
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "VOTE TALLY",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = ArtisticLilac,
                letterSpacing = 1.sp
            )
            Text(
                text = if (gameState.tiedPlayerIds.isNotEmpty())
                    "Tie detected between suspects! Preparing revote..."
                else "Here are the votes cast by all players.",
                fontSize = 12.sp,
                color = if (gameState.tiedPlayerIds.isNotEmpty()) ArtisticGold else TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(gameState.activeAlivePlayers) { player ->
                    val votes = gameState.voteTallies[player.id] ?: 0
                    val isTied = player.id in gameState.tiedPlayerIds
                    SuspectVoteCard(
                        player = player,
                        isSelected = false,
                        onSelect = {},
                        enabled = false,
                        votesCount = votes,
                        totalVoters = gameState.activeAlivePlayers.size,
                        isTied = isTied
                    )
                }
            }
        }
    }
}

// 7. Elimination Screen
@Composable
private fun EliminationPhase(
    gameState: GameState
) {
    val lastElimination = gameState.eliminatedHistory.lastOrNull()
    val eliminatedPlayer = lastElimination?.eliminatedPlayer

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(0.92f),
            showGlowOrb = true,
            borderColor = ArtisticRed.copy(alpha = 0.5f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚖️",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "ELIMINATED",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = ArtisticRed,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                if (eliminatedPlayer != null) {
                    AvatarView(
                        avatarId = eliminatedPlayer.avatarId,
                        size = 64.dp,
                        isEliminated = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = eliminatedPlayer.username,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    if (gameState.settings.revealRoleOnElimination) {
                        BadgeChip(
                            text = if (lastElimination.isImposter) "WAS THE IMPOSTER 🕵️" else "WAS AN INNOCENT NORMAL PLAYER 🛡️",
                            color = if (lastElimination.isImposter) ArtisticMint else ArtisticGold,
                            backgroundColor = SurfaceCardDeep
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = lastElimination?.reason ?: "",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(18.dp))
                CircularTimer(secondsRemaining = gameState.timerSecondsRemaining, totalSeconds = 6)
            }
        }
    }
}

// 8. Imposter High-Stakes Guess Screen
@Composable
private fun ImposterGuessPhase(
    isImposter: Boolean,
    guessInput: String,
    secondsLeft: Int,
    onGuessInputChanged: (String) -> Unit,
    onSubmitGuess: () -> Unit,
    onSkipGuess: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimerIndicator(secondsRemaining = secondsLeft, totalSeconds = 20)

        Spacer(modifier = Modifier.height(12.dp))

        if (isImposter) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                showGlowOrb = true,
                borderColor = ArtisticGold.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "🔥", fontSize = 44.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "YOU SURVIVED THE VOTE!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = ArtisticGold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "High Stakes: Guess the secret anime character now for an instant clutch victory, or skip to continue the deduction rounds!",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = guessInput,
                        onValueChange = onGuessInputChanged,
                        placeholder = { Text("e.g. Naruto, Gojo, Luffy...", color = TextMuted) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ArtisticGold,
                            unfocusedBorderColor = BorderSubtle,
                            focusedContainerColor = SurfaceInput,
                            unfocusedContainerColor = SurfaceInput,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("imposter_guess_input")
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NeonButton(
                    text = "SUBMIT CHARACTER GUESS",
                    onClick = onSubmitGuess,
                    enabled = guessInput.isNotBlank(),
                    gradient = ArtisticGradientGold,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "submit_imposter_guess_button"
                )
                NeonOutlineButton(
                    text = "Skip Guess (Play Next Round)",
                    onClick = onSkipGuess,
                    accentColor = TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                showGlowOrb = true,
                borderColor = ArtisticLilac.copy(alpha = 0.35f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "⏳", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "IMPOSTER SURVIVED",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = ArtisticLilac,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The secret Imposter is currently deciding whether to attempt an instant character guess...",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// 9. Victory Screen
@Composable
private fun VictoryPhase(
    gameState: GameState,
    myId: String,
    onPlayAgain: () -> Unit,
    onReturnToLobby: () -> Unit
) {
    val isPlayersWin = gameState.winnerTeam == WinnerTeam.PLAYERS
    val character = gameState.character
    val imposter = gameState.imposterPlayer

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Victory Banner
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                showGlowOrb = true,
                borderColor = if (isPlayersWin) ArtisticMint.copy(alpha = 0.6f) else ArtisticRed.copy(alpha = 0.6f)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isPlayersWin) "🎉" else "🕵️",
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isPlayersWin) "NORMAL PLAYERS WIN!" else "IMPOSTER WINS!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isPlayersWin) ArtisticMint else ArtisticRed,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isPlayersWin)
                            "The Imposter was successfully detected and eliminated!"
                        else if (gameState.imposterGuessedCorrectly)
                            "The Imposter correctly guessed the secret character!"
                        else
                            "The Imposter deceived the players and survived until the end!",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Unmasked Character Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ArtisticLilac.copy(alpha = 0.4f)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = ArtisticLilac, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SECRET CHARACTER UNMASKED", fontSize = 11.sp, fontWeight = FontWeight.Black, color = ArtisticLilac, letterSpacing = 1.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    if (character != null) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = SurfaceInput,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = character.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${character.anime} • ${character.category}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ArtisticGold
                                )
                            }
                        }
                        if (character.famousQuote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "« ${character.famousQuote} »",
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Unmasked Imposter
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ArtisticRed.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarView(
                        avatarId = imposter?.avatarId ?: "avatar_1",
                        size = 48.dp,
                        isBot = imposter?.isBot == true
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        BadgeChip(text = "THE SECRET IMPOSTER", color = ArtisticRed, backgroundColor = SurfaceCardDeep)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = imposter?.username ?: "Unknown",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        // Action Buttons
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NeonOutlineButton(
                    text = "Return to Lobby",
                    onClick = onReturnToLobby,
                    modifier = Modifier.weight(1f)
                )

                NeonButton(
                    text = "Play Again",
                    onClick = onPlayAgain,
                    gradient = ArtisticGradientPrimary,
                    modifier = Modifier.weight(1f),
                    testTag = "play_again_button"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
