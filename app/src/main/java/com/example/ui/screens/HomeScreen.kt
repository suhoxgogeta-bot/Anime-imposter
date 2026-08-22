package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.components.AvatarView
import com.example.ui.components.BadgeChip
import com.example.ui.components.GlassCard
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonOutlineButton
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
import com.example.ui.viewmodel.AppScreen

@Composable
fun HomeScreen(
    profile: UserProfile,
    onNavigate: (AppScreen) -> Unit,
    onQuickMatch: () -> Unit,
    onPracticeMode: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Navigation Bar
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Profile Chip
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(SurfaceCard)
                            .border(1.dp, BorderGlow, RoundedCornerShape(24.dp))
                            .clickable { onNavigate(AppScreen.PROFILE) }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AvatarView(
                            avatarId = profile.avatarId,
                            size = 34.dp,
                            level = profile.level
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = profile.username,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Lvl ${profile.level} • ${profile.xp} XP",
                                fontSize = 11.sp,
                                color = ArtisticLilac
                            )
                        }
                    }

                    // Action Icons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onNavigate(AppScreen.CODEX) },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SurfaceCard)
                                .border(1.dp, BorderSubtle, CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "Character Codex",
                                tint = ArtisticLilac,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { onNavigate(AppScreen.SETTINGS) },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SurfaceCard)
                                .border(1.dp, BorderSubtle, CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Hero Visual Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, BorderGlow)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(185.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_hero_banner),
                            contentDescription = "Anime Imposter Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Gradient overlay for readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xEE0F0F12))
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(18.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BadgeChip(text = "SOCIAL DEDUCTION", color = ArtisticLilac, backgroundColor = SurfaceCardDeep)
                                Spacer(modifier = Modifier.width(8.dp))
                                BadgeChip(text = "4–12 PLAYERS", color = ArtisticMint, backgroundColor = SurfaceCardDeep)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "ANIME IMPOSTER",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = TextPrimary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Deceive. Deduce. Unmask the Shadow.",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Quick Match Hero Button
            item {
                NeonButton(
                    text = "QUICK MATCH",
                    onClick = onQuickMatch,
                    icon = Icons.Default.FlashOn,
                    gradient = ArtisticGradientPrimary,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "quick_match_button"
                )
            }

            // Multiplayer Room Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NeonOutlineButton(
                        text = "Create Room",
                        onClick = { onNavigate(AppScreen.CREATE_ROOM) },
                        icon = Icons.Default.AddCircle,
                        accentColor = ArtisticLilac,
                        modifier = Modifier.weight(1f),
                        testTag = "create_room_button"
                    )

                    NeonOutlineButton(
                        text = "Join Room",
                        onClick = { onNavigate(AppScreen.JOIN_ROOM) },
                        icon = Icons.Default.QrCode,
                        accentColor = ArtisticMint,
                        modifier = Modifier.weight(1f),
                        testTag = "join_room_button"
                    )
                }
            }

            // Practice Mode Card
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPracticeMode() },
                    borderColor = ArtisticGold.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(ArtisticGold.copy(alpha = 0.15f))
                                .border(1.dp, ArtisticGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "Practice Mode",
                                tint = ArtisticGold,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Practice / Solo Mode",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                BadgeChip(text = "AI BOTS", color = ArtisticGold, backgroundColor = SurfaceCardDeep)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Hone your clue-giving and deduction skills against clever AI players.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = ArtisticGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Player Stats Overview
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatBox(
                        title = "Win Rate",
                        value = "${profile.winRatePercent}%",
                        color = ArtisticMint,
                        modifier = Modifier.weight(1f)
                    )
                    StatBox(
                        title = "Matches",
                        value = "${profile.gamesPlayed}",
                        color = ArtisticLilac,
                        modifier = Modifier.weight(1f)
                    )
                    StatBox(
                        title = "Streak",
                        value = "${profile.currentWinStreak} 🔥",
                        color = ArtisticGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun StatBox(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}
