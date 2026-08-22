package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Player
import com.example.engine.GameState
import com.example.ui.components.AvatarView
import com.example.ui.components.BadgeChip
import com.example.ui.components.GlassCard
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonOutlineButton
import com.example.ui.theme.ArtisticGold
import com.example.ui.theme.ArtisticGradientMint
import com.example.ui.theme.ArtisticGradientPrimary
import com.example.ui.theme.ArtisticLilac
import com.example.ui.theme.ArtisticMint
import com.example.ui.theme.ArtisticRed
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardDeep
import com.example.ui.theme.SurfaceInput
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LobbyScreen(
    gameState: GameState,
    currentUserId: String,
    onStartGame: () -> Unit,
    onToggleReady: () -> Unit,
    onAddBot: () -> Unit,
    onRemovePlayer: (String) -> Unit,
    onLeaveRoom: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current
    val isHost = gameState.players.firstOrNull { it.id == currentUserId }?.isHost == true
    val myPlayer = gameState.players.firstOrNull { it.id == currentUserId }
    val isReady = myPlayer?.isReady == true
    val canStart = isHost && gameState.players.size >= 4

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
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header & Room Code
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onLeaveRoom,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SurfaceCard)
                            .border(1.dp, BorderSubtle, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Leave Lobby",
                            tint = TextPrimary
                        )
                    }

                    // Room Code Box
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceCardDeep)
                            .border(1.dp, ArtisticLilac.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Anime Imposter Room", gameState.roomId)
                                clipboard.setPrimaryClip(clip)
                                onShowToast("Room Code copied: ${gameState.roomId}")
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ROOM: ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Text(
                            text = gameState.roomId,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = ArtisticLilac,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Code",
                            tint = ArtisticLilac,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    BadgeChip(
                        text = "${gameState.players.size}/${gameState.settings.playerLimit}",
                        color = if (gameState.players.size >= 4) ArtisticMint else ArtisticGold,
                        backgroundColor = SurfaceCardDeep
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Game Rules Summary Bar
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BorderSubtle
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Category: ${gameState.settings.category}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${gameState.settings.clueRoundsBeforeVoting} Clue Rounds • ${gameState.settings.clueTimerSeconds}s Clue Timer",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        BadgeChip(
                            text = if (gameState.players.size < 4) "Need ${4 - gameState.players.size} More" else "Ready to Launch",
                            color = if (gameState.players.size < 4) ArtisticGold else ArtisticMint,
                            backgroundColor = SurfaceCardDeep
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "LOBBY PLAYERS (${gameState.players.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = ArtisticLilac,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Player List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(gameState.players) { player ->
                        LobbyPlayerItem(
                            player = player,
                            isSelf = player.id == currentUserId,
                            canKick = isHost && player.id != currentUserId,
                            onKick = { onRemovePlayer(player.id) }
                        )
                    }
                }
            }

            // Bottom Actions & Host Controls
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Add AI Bot Button
                if (isHost && gameState.players.size < gameState.settings.playerLimit) {
                    NeonOutlineButton(
                        text = "+ Add AI Player (${gameState.settings.playerLimit - gameState.players.size} slots left)",
                        onClick = onAddBot,
                        icon = Icons.Default.SmartToy,
                        accentColor = ArtisticLilac,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "add_bot_button"
                    )
                }

                // Ready toggle for non-host
                if (!isHost) {
                    NeonButton(
                        text = if (isReady) "READY ✓" else "TAP WHEN READY",
                        onClick = onToggleReady,
                        gradient = if (isReady) ArtisticGradientMint else ArtisticGradientPrimary,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "ready_toggle_button"
                    )
                }

                // Host Start Button
                if (isHost) {
                    NeonButton(
                        text = if (gameState.players.size < 4) "NEED AT LEAST 4 PLAYERS" else "START GAME",
                        onClick = onStartGame,
                        enabled = canStart,
                        icon = Icons.Default.PlayArrow,
                        gradient = ArtisticGradientPrimary,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "start_game_button"
                    )
                }
            }
        }
    }
}

@Composable
private fun LobbyPlayerItem(
    player: Player,
    isSelf: Boolean,
    canKick: Boolean,
    onKick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelf) SurfaceCard else SurfaceCardDeep,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelf) ArtisticLilac.copy(alpha = 0.5f) else BorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarView(
                avatarId = player.avatarId,
                size = 38.dp,
                isHost = player.isHost,
                isBot = player.isBot,
                isReady = player.isReady,
                showReadyBadge = true
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = player.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    if (isSelf) {
                        Spacer(modifier = Modifier.width(6.dp))
                        BadgeChip(text = "YOU", color = ArtisticLilac, backgroundColor = SurfaceCardDeep)
                    }
                    if (player.isHost) {
                        Spacer(modifier = Modifier.width(6.dp))
                        BadgeChip(text = "HOST", color = ArtisticGold, backgroundColor = SurfaceCardDeep)
                    }
                }
                Text(
                    text = if (player.isBot) "AI Detective • Lv.${player.level}" else "Player • Lv.${player.level}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            if (canKick) {
                IconButton(
                    onClick = onKick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = ArtisticRed,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
