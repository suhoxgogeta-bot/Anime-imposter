package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.entities.MatchHistoryEntity
import com.example.data.model.Achievement
import com.example.data.model.UserProfile
import com.example.ui.components.AvatarView
import com.example.ui.components.BadgeChip
import com.example.ui.components.GlassCard
import com.example.ui.components.NeonButton
import com.example.ui.theme.ArtisticGold
import com.example.ui.theme.ArtisticGradientGold
import com.example.ui.theme.ArtisticGradientMint
import com.example.ui.theme.ArtisticGradientPrimary
import com.example.ui.theme.ArtisticLilac
import com.example.ui.theme.ArtisticMint
import com.example.ui.theme.ArtisticOrchid
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    profile: UserProfile,
    achievements: List<Achievement>,
    recentMatches: List<MatchHistoryEntity>,
    onUpdateProfile: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var isEditingName by remember { mutableStateOf(false) }
    var editedUsername by remember { mutableStateOf(profile.username) }
    var selectedAvatar by remember { mutableStateOf(profile.avatarId) }

    val avatars = (1..8).map { "avatar_$it" }

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
                        text = "Detective Dossier & Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                }
            }

            // Profile Header Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    showGlowOrb = true,
                    borderColor = ArtisticLilac.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AvatarView(
                            avatarId = selectedAvatar,
                            size = 72.dp,
                            level = profile.level
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (isEditingName) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                OutlinedTextField(
                                    value = editedUsername,
                                    onValueChange = { editedUsername = it },
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
                                    modifier = Modifier.weight(1f).testTag("edit_username_input")
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = {
                                        onUpdateProfile(editedUsername, selectedAvatar)
                                        isEditingName = false
                                    },
                                    modifier = Modifier.clip(CircleShape).background(ArtisticLilac).size(40.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = "Save", tint = TextDark)
                                }
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = profile.username,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { isEditingName = true },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Name", tint = ArtisticLilac, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Level ${profile.level} Detective • ${profile.xp} Total XP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArtisticMint
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Level Progress Bar
                        val progress = (profile.currentLevelXpProgress.toFloat() / profile.xpForNextLevel.toFloat()).coerceIn(0f, 1f)
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Level Progress", fontSize = 11.sp, color = TextSecondary)
                                Text("${profile.currentLevelXpProgress}/${profile.xpForNextLevel} XP", fontSize = 11.sp, color = ArtisticLilac, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = ArtisticLilac,
                                trackColor = SurfaceCardDeep
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Avatar Picker
                        Text("Choose Avatar:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            avatars.forEach { av ->
                                val isSelected = selectedAvatar == av
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .border(if (isSelected) 2.dp else 0.dp, if (isSelected) ArtisticLilac else Color.Transparent, CircleShape)
                                        .clickable {
                                            selectedAvatar = av
                                            onUpdateProfile(profile.username, av)
                                        }
                                        .padding(2.dp)
                                ) {
                                    AvatarView(avatarId = av, size = 36.dp)
                                }
                            }
                        }
                    }
                }
            }

            // Lifetime Statistics Card
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "LIFETIME STATISTICS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = ArtisticLilac,
                            letterSpacing = 1.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatMini(title = "Matches", value = "${profile.gamesPlayed}", color = TextPrimary, modifier = Modifier.weight(1f))
                            StatMini(title = "Wins", value = "${profile.wins}", color = ArtisticMint, modifier = Modifier.weight(1f))
                            StatMini(title = "Losses", value = "${profile.losses}", color = ArtisticRed, modifier = Modifier.weight(1f))
                            StatMini(title = "Win %", value = "${profile.winRatePercent}%", color = ArtisticLilac, modifier = Modifier.weight(1f))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatMini(title = "Player Wins", value = "${profile.playerWins}", color = ArtisticMint, modifier = Modifier.weight(1f))
                            StatMini(title = "Imposter Wins", value = "${profile.imposterWins}", color = ArtisticRed, modifier = Modifier.weight(1f))
                            StatMini(title = "Best Streak", value = "${profile.bestWinStreak} 🔥", color = ArtisticGold, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Achievements Trophy Gallery
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = ArtisticGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ACHIEVEMENTS & TROPHIES",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        achievements.forEach { ach ->
                            AchievementCard(achievement = ach)
                        }
                    }
                }
            }

            // Recent Matches History
            if (recentMatches.isNotEmpty()) {
                item {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.History, contentDescription = null, tint = ArtisticLilac, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "RECENT MATCH LOGS",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            recentMatches.take(5).forEach { match ->
                                MatchLogCard(match = match)
                            }
                        }
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
private fun StatMini(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceCardDeep,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 10.sp, color = TextSecondary)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = if (achievement.isUnlocked) SurfaceCard else SurfaceCardDeep,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (achievement.isUnlocked) ArtisticGold.copy(alpha = 0.5f) else BorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = achievement.iconEmoji,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = achievement.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (achievement.isUnlocked) TextPrimary else TextMuted
                    )
                    if (achievement.isUnlocked) {
                        Spacer(modifier = Modifier.width(6.dp))
                        BadgeChip(text = "UNLOCKED", color = ArtisticGold, backgroundColor = SurfaceCardDeep)
                    }
                }
                Text(
                    text = achievement.description,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
            Text(
                text = "${achievement.currentProgress}/${achievement.maxProgress}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (achievement.isUnlocked) ArtisticGold else TextMuted
            )
        }
    }
}

@Composable
private fun MatchLogCard(match: MatchHistoryEntity) {
    val didWin = (match.wasImposter && match.winnerTeam == "IMPOSTER") ||
            (!match.wasImposter && match.winnerTeam == "PLAYERS")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        color = SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "${match.characterName} (${match.animeName})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (match.wasImposter) "Role: Imposter 🕵️" else "Role: Normal Player 🔍",
                    fontSize = 11.sp,
                    color = if (match.wasImposter) ArtisticRed else ArtisticMint
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                BadgeChip(
                    text = if (didWin) "VICTORY +${match.xpEarned} XP" else "DEFEAT +${match.xpEarned} XP",
                    color = if (didWin) ArtisticMint else ArtisticRed,
                    backgroundColor = SurfaceCardDeep
                )
            }
        }
    }
}
