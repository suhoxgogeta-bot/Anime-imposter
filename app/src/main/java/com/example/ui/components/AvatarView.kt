package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArtisticGold
import com.example.ui.theme.ArtisticGreen
import com.example.ui.theme.ArtisticLilac
import com.example.ui.theme.ArtisticMint
import com.example.ui.theme.ArtisticOrchid
import com.example.ui.theme.ArtisticRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardDeep
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextPrimary

private val AVATAR_PALETTES = mapOf(
    "avatar_1" to Pair(listOf(ArtisticLilac, ArtisticOrchid), "🦊"),
    "avatar_2" to Pair(listOf(ArtisticMint, Color(0xFF38BDF8)), "⚡"),
    "avatar_3" to Pair(listOf(ArtisticGold, Color(0xFFFF9E7D)), "🔥"),
    "avatar_4" to Pair(listOf(ArtisticGreen, Color(0xFF059669)), "🐉"),
    "avatar_5" to Pair(listOf(Color(0xFFC084FC), Color(0xFF7C3AED)), "👁️"),
    "avatar_6" to Pair(listOf(Color(0xFFF472B6), Color(0xFFDB2777)), "🌸"),
    "avatar_7" to Pair(listOf(Color(0xFF67E8F9), Color(0xFF0284C7)), "⚔️"),
    "avatar_8" to Pair(listOf(ArtisticGold, Color(0xFFEA580C)), "🎭")
)

@Composable
fun AvatarView(
    avatarId: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    isHost: Boolean = false,
    isBot: Boolean = false,
    isReady: Boolean = false,
    isEliminated: Boolean = false,
    showReadyBadge: Boolean = false,
    level: Int? = null
) {
    val (gradientColors, emoji) = AVATAR_PALETTES[avatarId]
        ?: Pair(listOf(ArtisticLilac, ArtisticOrchid), "🕵️")

    val borderBrush = when {
        isEliminated -> Brush.linearGradient(listOf(ArtisticRed, Color.DarkGray))
        isHost -> Brush.linearGradient(listOf(ArtisticGold, ArtisticLilac))
        else -> Brush.linearGradient(listOf(SurfaceCard, SurfaceCard))
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Main Avatar Circle
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    if (isEliminated) Brush.linearGradient(listOf(Color(0xFF2A151B), Color(0xFF1A1A24)))
                    else Brush.linearGradient(gradientColors)
                )
                .border(2.dp, borderBrush, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isEliminated) "💀" else emoji,
                fontSize = (size.value * 0.45f).sp
            )
        }

        // Host Crown Badge
        if (isHost && !isEliminated) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(ArtisticGold),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Host",
                    tint = TextDark,
                    modifier = Modifier.size(10.dp)
                )
            }
        }

        // Bot Indicator Badge
        if (isBot && !isHost && !isEliminated) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(ArtisticMint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "AI Bot",
                    tint = TextDark,
                    modifier = Modifier.size(10.dp)
                )
            }
        }

        // Ready Status Badge
        if (showReadyBadge && !isEliminated) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (isReady) ArtisticGreen else Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isReady) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = if (isReady) "Ready" else "Not Ready",
                    tint = TextDark,
                    modifier = Modifier.size(10.dp)
                )
            }
        }

        // Level pill
        if (level != null && !showReadyBadge && !isEliminated) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 5.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(SurfaceCardDeep)
                    .border(1.dp, ArtisticLilac.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 1.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Lv.$level",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    color = ArtisticLilac
                )
            }
        }
    }
}

