package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClueEntry
import com.example.ui.theme.ArtisticGold
import com.example.ui.theme.ArtisticLilac
import com.example.ui.theme.ArtisticMint
import com.example.ui.theme.ArtisticOrchid
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceCardDeep
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ClueCardView(
    clue: ClueEntry,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false,
    isSelf: Boolean = false
) {
    val nameColor = when (clue.clueRoundIndex % 3) {
        1 -> ArtisticLilac
        2 -> ArtisticGold
        else -> ArtisticMint
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isSelf) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isSelf) {
            AvatarView(
                avatarId = clue.playerAvatarId,
                size = 36.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = if (isSelf) 16.dp else 4.dp,
                topEnd = if (isSelf) 4.dp else 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            color = if (isHighlighted) SurfaceCardElevated else SurfaceCardElevated,
            border = BorderStroke(1.dp, if (isHighlighted) ArtisticLilac.copy(alpha = 0.5f) else BorderSubtle),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = clue.playerName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = nameColor
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    BadgeChip(
                        text = "R${clue.clueRoundIndex}",
                        color = ArtisticLilac,
                        backgroundColor = SurfaceCardDeep
                    )
                    if (clue.wasAutoSubmitted) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(Auto)",
                            fontSize = 10.sp,
                            color = ArtisticGold,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "« ${clue.clueText} »",
                    fontSize = 13.sp,
                    color = TextPrimary,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 18.sp
                )
            }
        }

        if (isSelf) {
            Spacer(modifier = Modifier.width(10.dp))
            AvatarView(
                avatarId = clue.playerAvatarId,
                size = 36.dp
            )
        }
    }
}

