package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AnimeImposterColorScheme = darkColorScheme(
    primary = ArtisticLilac,
    onPrimary = TextDark,
    primaryContainer = SurfaceCardElevated,
    onPrimaryContainer = ArtisticLilac,
    secondary = ArtisticMint,
    onSecondary = TextDark,
    secondaryContainer = SurfaceCardDeep,
    onSecondaryContainer = ArtisticMint,
    tertiary = ArtisticGold,
    onTertiary = TextDark,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = BorderGlow,
    error = ArtisticRed,
    onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AnimeImposterColorScheme,
        typography = Typography,
        content = content
    )
}
