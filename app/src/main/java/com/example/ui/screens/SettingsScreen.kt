package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BadgeChip
import com.example.ui.components.GlassCard
import com.example.ui.theme.ArtisticGold
import com.example.ui.theme.ArtisticLilac
import com.example.ui.theme.ArtisticMint
import com.example.ui.theme.ArtisticRed
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardDeep
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
    isSoundEnabled: Boolean,
    isHapticsEnabled: Boolean,
    onToggleSound: (Boolean) -> Unit,
    onToggleHaptics: (Boolean) -> Unit,
    onBack: () -> Unit
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SurfaceCard)
                            .border(1.dp, BorderSubtle, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Settings & How to Play",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                }
            }

            // Audio & Haptics Preferences
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "AUDIO & FEEDBACK",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = ArtisticLilac,
                            letterSpacing = 1.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = ArtisticLilac, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Sound Effects & Tones", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text("Timer beeps, lobby pings, elimination SFX", fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                            Switch(
                                checked = isSoundEnabled,
                                onCheckedChange = onToggleSound,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ArtisticLilac,
                                    checkedTrackColor = ArtisticLilac.copy(alpha = 0.4f),
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = SurfaceCardDeep
                                ),
                                modifier = Modifier.testTag("sound_toggle")
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Vibration, contentDescription = null, tint = ArtisticMint, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Haptic Feedback", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text("Tactile vibrations on clicks & votes", fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                            Switch(
                                checked = isHapticsEnabled,
                                onCheckedChange = onToggleHaptics,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ArtisticMint,
                                    checkedTrackColor = ArtisticMint.copy(alpha = 0.4f),
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = SurfaceCardDeep
                                ),
                                modifier = Modifier.testTag("haptics_toggle")
                            )
                        }
                    }
                }
            }

            // How to Play Rules Guide
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = ArtisticGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "HOW TO PLAY • GAME RULES",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = ArtisticGold,
                                letterSpacing = 1.sp
                            )
                        }

                        RuleSection(
                            emoji = "🔍",
                            title = "Normal Detective Role",
                            desc = "You receive the secret anime character's dossier (traits, abilities, quotes). Submit subtle clues that other knowing players will recognize, but avoid naming the character or anime directly."
                        )

                        RuleSection(
                            emoji = "🕵️",
                            title = "The Imposter Role",
                            desc = "You do NOT know the secret anime character! Listen intently to what others say, deduce the theme or power system, and provide believable clues to blend in without exposing yourself."
                        )

                        RuleSection(
                            emoji = "🚫",
                            title = "Clue Restrictions",
                            desc = "Directly typing the character's exact name, nickname, or series title in your clue is automatically blocked by the validator."
                        )

                        RuleSection(
                            emoji = "🗳️",
                            title = "Voting & Elimination",
                            desc = "After the clue rounds, all active players vote for who they suspect is the Imposter. The player with the most votes is eliminated."
                        )

                        RuleSection(
                            emoji = "🏆",
                            title = "Win Conditions",
                            desc = "• Normal Players win by voting out the Imposter.\n• Imposter wins by surviving until 2 players remain or by successfully executing a Character Guess upon surviving voting."
                        )
                    }
                }
            }

            // App Credits
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "ANIME IMPOSTER v1.0", fontSize = 13.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Multiplayer Social Deduction Game for Anime Fans", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun RuleSection(
    emoji: String,
    title: String,
    desc: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCardDeep)
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = desc, fontSize = 11.sp, color = TextSecondary, lineHeight = 16.sp)
    }
}
