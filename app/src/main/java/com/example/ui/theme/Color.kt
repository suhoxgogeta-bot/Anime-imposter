package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Artistic Flair Theme Palette
val BackgroundDark = Color(0xFF0F0F12) // Inky artistic obsidian
val SurfaceDark = Color(0xFF161620)    // Dark slate canvas
val SurfaceCard = Color(0xFF1A1A24)    // Artistic card background
val SurfaceCardElevated = Color(0xFF232330) // Elevated card surface
val SurfaceCardDeep = Color(0xFF2D2D3F)     // Pill & chip surface
val SurfaceGlass = Color(0x991A1A24)        // Translucent card glass
val SurfaceInput = Color(0x66000000)        // Translucent deep input bg

// Artistic Flair Accents
val ArtisticLilac = Color(0xFFB4A9FF)  // Hero primary lilac / lavender
val ArtisticOrchid = Color(0xFFE0C3FC) // Pastel soft orchid
val ArtisticGold = Color(0xFFF3D17C)   // Warm pastel amber
val ArtisticMint = Color(0xFF82E9DE)   // Pastel cyan / mint
val ArtisticCoral = Color(0xFFFF7B93)  // Soft vivid coral
val ArtisticRed = Color(0xFFFF4B4B)    // Alert red / elimination pulse
val ArtisticGreen = Color(0xFF4ADE80)  // Verified / trusted green

// Compatibility aliases
val NeonPink = ArtisticCoral
val NeonCyan = ArtisticMint
val NeonPurple = ArtisticLilac
val NeonAmber = ArtisticGold
val NeonEmerald = ArtisticGreen
val NeonRed = ArtisticRed

// Typography / Text Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)
val TextDark = Color(0xFF0F0F12)

// Borders & Glows
val BorderGlow = Color(0x33B4A9FF)     // Delicate lilac border
val BorderSubtle = Color(0x1AFFFFFF)   // 10% white border
val BorderCyanGlow = Color(0x4D82E9DE) // 30% mint glow
val BorderPinkGlow = Color(0x4DB4A9FF) // 30% lilac glow

// Artistic Gradients
val ArtisticGradientPrimary = Brush.horizontalGradient(
    colors = listOf(ArtisticLilac, ArtisticOrchid)
)

val ArtisticGradientGold = Brush.horizontalGradient(
    colors = listOf(ArtisticGold, ArtisticCoral)
)

val ArtisticGradientMint = Brush.horizontalGradient(
    colors = listOf(ArtisticMint, ArtisticLilac)
)

val ArtisticGradientHeader = Brush.verticalGradient(
    colors = listOf(Color(0xFF1A1A24), Color(0x000F0F12))
)

val CyberGradientPrimary = ArtisticGradientPrimary
val CyberGradientCyan = ArtisticGradientMint
val CyberGradientAmber = ArtisticGradientGold
val CyberGradientDark = Brush.verticalGradient(
    colors = listOf(SurfaceCard, BackgroundDark)
)

