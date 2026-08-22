package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArtisticGold
import com.example.ui.theme.ArtisticLilac
import com.example.ui.theme.ArtisticRed
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceCardDeep
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TimerIndicator(
    secondsRemaining: Int,
    totalSeconds: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (totalSeconds > 0) {
        (secondsRemaining.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val animatedProgress by animateFloatAsState(targetValue = progress, label = "timer_progress")

    val barColor by animateColorAsState(
        targetValue = when {
            secondsRemaining <= 5 -> ArtisticRed
            secondsRemaining <= 10 -> ArtisticGold
            else -> ArtisticLilac
        },
        label = "timer_color"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardDeep,
        border = BorderStroke(1.dp, ArtisticLilac.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pulsing dot
            PillTimerDot(isUrgent = secondsRemaining <= 5)
            Spacer(modifier = Modifier.width(8.dp))
            val mins = secondsRemaining / 60
            val secs = secondsRemaining % 60
            val timeString = String.format("%02d:%02d", mins, secs)
            Text(
                text = timeString,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                color = barColor,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Progress Track
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(SurfaceCardElevated)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(barColor)
                )
            }
        }
    }
}

@Composable
fun HeaderPillTimer(
    secondsRemaining: Int,
    modifier: Modifier = Modifier
) {
    val mins = secondsRemaining / 60
    val secs = secondsRemaining % 60
    val timeFormatted = String.format("%02d:%02d", mins, secs)
    val isUrgent = secondsRemaining <= 5

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(SurfaceCardDeep)
            .border(1.dp, ArtisticLilac.copy(alpha = 0.3f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PillTimerDot(isUrgent = isUrgent)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = timeFormatted,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = if (isUrgent) ArtisticRed else TextPrimary,
                letterSpacing = (-0.5).sp
            )
        }
    }
}

@Composable
fun PillTimerDot(isUrgent: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isUrgent) 400 else 800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )

    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (isUrgent) ArtisticRed.copy(alpha = alpha) else ArtisticRed)
    )
}

@Composable
fun CircularTimer(
    secondsRemaining: Int,
    totalSeconds: Int,
    modifier: Modifier = Modifier,
    size: Int = 48
) {
    val progress = if (totalSeconds > 0) {
        (secondsRemaining.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val barColor = when {
        secondsRemaining <= 5 -> ArtisticRed
        secondsRemaining <= 10 -> ArtisticGold
        else -> ArtisticLilac
    }

    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(size.dp),
            color = barColor,
            strokeWidth = 3.5.dp,
            trackColor = SurfaceCardElevated
        )
        Text(
            text = "$secondsRemaining",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )
    }
}

