package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Player
import com.example.ui.theme.ArtisticGold
import com.example.ui.theme.ArtisticLilac
import com.example.ui.theme.ArtisticMint
import com.example.ui.theme.ArtisticRed
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardDeep
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SuspectVoteCard(
    player: Player,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSelf: Boolean = false,
    votesCount: Int? = null,
    totalVoters: Int = 1,
    isTied: Boolean = false
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected -> ArtisticLilac
            isTied -> ArtisticGold
            votesCount != null && votesCount > 0 -> ArtisticLilac.copy(alpha = 0.5f)
            else -> BorderSubtle
        },
        label = "border_anim"
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> SurfaceCardElevated
            isSelf -> SurfaceCard.copy(alpha = 0.5f)
            else -> SurfaceCard
        },
        label = "bg_anim"
    )

    Surface(
        onClick = { if (enabled && !isSelf) onSelect() },
        enabled = enabled && !isSelf,
        modifier = modifier
            .testTag("suspect_card_${player.id}")
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(if (isSelected || isTied) 1.5.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarView(
                    avatarId = player.avatarId,
                    size = 42.dp,
                    isBot = player.isBot,
                    isHost = player.isHost,
                    isEliminated = player.isEliminated,
                    level = player.level
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = player.username,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (isSelf) TextMuted else TextPrimary
                        )
                        if (isSelf) {
                            Spacer(modifier = Modifier.width(6.dp))
                            BadgeChip(text = "YOU", color = TextMuted, backgroundColor = SurfaceCardDeep)
                        }
                        if (isTied) {
                            Spacer(modifier = Modifier.width(6.dp))
                            BadgeChip(text = "TIED", color = ArtisticGold, backgroundColor = SurfaceCardDeep)
                        }
                    }

                    if (player.isBot) {
                        Text(
                            text = "AI • ${player.botPersonality}",
                            fontSize = 11.sp,
                            color = ArtisticMint
                        )
                    }
                }

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = ArtisticLilac,
                        modifier = Modifier.size(24.dp)
                    )
                } else if (votesCount != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (votesCount > 0) ArtisticLilac.copy(alpha = 0.2f) else SurfaceCardDeep)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$votesCount ${if (votesCount == 1) "vote" else "votes"}",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = if (votesCount > 0) ArtisticLilac else TextMuted
                        )
                    }
                }
            }

            // Vote percentage bar if displaying results
            if (votesCount != null && totalVoters > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                val ratio = (votesCount.toFloat() / totalVoters.toFloat()).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(SurfaceCardDeep)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(ratio)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isTied) ArtisticGold else ArtisticLilac)
                    )
                }
            }
        }
    }
}

