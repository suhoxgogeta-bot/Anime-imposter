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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameDifficulty
import com.example.data.model.GameSettings
import com.example.ui.components.BadgeChip
import com.example.ui.components.GlassCard
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonOutlineButton
import com.example.ui.components.SectionHeader
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
import com.example.ui.viewmodel.AppScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateRoomScreen(
    onBack: () -> Unit,
    onCreateRoom: (GameSettings) -> Unit
) {
    var playerLimit by remember { mutableIntStateOf(8) }
    var clueRounds by remember { mutableIntStateOf(2) }
    var clueTimer by remember { mutableIntStateOf(30) }
    var votingTimer by remember { mutableIntStateOf(25) }
    var imposterGuessing by remember { mutableStateOf(true) }
    var revealRoleOnElimination by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf("All Categories") }
    var selectedDifficulty by remember { mutableStateOf(GameDifficulty.ALL) }

    val categories = listOf(
        "All Categories", "Shonen", "Action", "Fantasy",
        "Psychological", "Isekai", "Romance", "Villains", "Legends"
    )

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
                        text = "Create Multiplayer Room",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                }
            }

            // Player Count Setting
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Group, contentDescription = null, tint = ArtisticLilac, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Player Limit", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            BadgeChip(text = "$playerLimit Players", color = ArtisticLilac, backgroundColor = SurfaceCardDeep)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { if (playerLimit > 4) playerLimit-- },
                                enabled = playerLimit > 4,
                                modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(SurfaceCardDeep)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = if (playerLimit > 4) TextPrimary else TextMuted)
                            }

                            Text(
                                text = "4 (Min) ─── $playerLimit ─── 12 (Max)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )

                            IconButton(
                                onClick = { if (playerLimit < 12) playerLimit++ },
                                enabled = playerLimit < 12,
                                modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(SurfaceCardDeep)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", tint = if (playerLimit < 12) TextPrimary else TextMuted)
                            }
                        }
                    }
                }
            }

            // Clue Rounds & Timers
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Clue Rounds before Voting", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(1, 2, 3).forEach { rounds ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (clueRounds == rounds) ArtisticLilac else SurfaceCardDeep)
                                            .border(1.dp, if (clueRounds == rounds) ArtisticLilac else BorderSubtle, RoundedCornerShape(10.dp))
                                            .clickable { clueRounds = rounds }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("$rounds", fontWeight = FontWeight.Black, color = if (clueRounds == rounds) TextDark else TextPrimary, fontSize = 13.sp)
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Clue Time Limit", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(15, 30, 45, 60).forEach { sec ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (clueTimer == sec) ArtisticMint else SurfaceCardDeep)
                                            .border(1.dp, if (clueTimer == sec) ArtisticMint else BorderSubtle, RoundedCornerShape(10.dp))
                                            .clickable { clueTimer = sec }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text("${sec}s", fontWeight = FontWeight.Black, color = if (clueTimer == sec) TextDark else TextPrimary, fontSize = 12.sp)
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Voting Time Limit", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(15, 25, 40).forEach { sec ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (votingTimer == sec) ArtisticGold else SurfaceCardDeep)
                                            .border(1.dp, if (votingTimer == sec) ArtisticGold else BorderSubtle, RoundedCornerShape(10.dp))
                                            .clickable { votingTimer = sec }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text("${sec}s", fontWeight = FontWeight.Black, color = if (votingTimer == sec) TextDark else TextPrimary, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Anime Category Selection
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Category, contentDescription = null, tint = ArtisticMint, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Anime Category", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { cat ->
                                val isSelected = selectedCategory == cat
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) ArtisticLilac else SurfaceCardDeep)
                                        .border(1.dp, if (isSelected) ArtisticLilac else BorderSubtle, RoundedCornerShape(10.dp))
                                        .clickable { selectedCategory = cat }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                        color = if (isSelected) TextDark else TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Special Gameplay Toggles
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Imposter Guess Mechanic", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                Text("Allows Imposter to attempt guessing character upon surviving voting.", fontSize = 11.sp, color = TextSecondary)
                            }
                            Switch(
                                checked = imposterGuessing,
                                onCheckedChange = { imposterGuessing = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ArtisticLilac,
                                    checkedTrackColor = ArtisticLilac.copy(alpha = 0.4f),
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = SurfaceCardDeep
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Reveal Role on Elimination", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                Text("Shows whether eliminated player was Normal or Imposter.", fontSize = 11.sp, color = TextSecondary)
                            }
                            Switch(
                                checked = revealRoleOnElimination,
                                onCheckedChange = { revealRoleOnElimination = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ArtisticMint,
                                    checkedTrackColor = ArtisticMint.copy(alpha = 0.4f),
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = SurfaceCardDeep
                                )
                            )
                        }
                    }
                }
            }

            // Create Lobby Action Button
            item {
                NeonButton(
                    text = "CREATE LOBBY",
                    onClick = {
                        onCreateRoom(
                            GameSettings(
                                playerLimit = playerLimit,
                                clueRoundsBeforeVoting = clueRounds,
                                clueTimerSeconds = clueTimer,
                                votingTimerSeconds = votingTimer,
                                imposterGuessingEnabled = imposterGuessing,
                                revealRoleOnElimination = revealRoleOnElimination,
                                category = selectedCategory,
                                difficulty = selectedDifficulty
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    gradient = ArtisticGradientPrimary,
                    testTag = "confirm_create_room_button"
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun JoinRoomScreen(
    onBack: () -> Unit,
    onJoinCode: (String) -> Unit
) {
    var roomCode by remember { mutableStateOf("") }

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
            Column {
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
                        text = "Join Game Room",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = null,
                            tint = ArtisticLilac,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Enter 6-Digit Room Code",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Ask the room host for their unique room code.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = roomCode,
                            onValueChange = { if (it.length <= 6) roomCode = it.uppercase() },
                            placeholder = { Text("e.g. KAGE88", color = TextMuted, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center,
                                color = ArtisticLilac,
                                letterSpacing = 4.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ArtisticLilac,
                                unfocusedBorderColor = BorderSubtle,
                                focusedContainerColor = SurfaceInput,
                                unfocusedContainerColor = SurfaceInput,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Characters,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { if (roomCode.length >= 4) onJoinCode(roomCode) }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("room_code_input")
                        )
                    }
                }
            }

            Column {
                NeonButton(
                    text = "ENTER ROOM",
                    onClick = { onJoinCode(roomCode) },
                    enabled = roomCode.length >= 4,
                    modifier = Modifier.fillMaxWidth(),
                    gradient = ArtisticGradientPrimary,
                    testTag = "submit_join_room_button"
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
