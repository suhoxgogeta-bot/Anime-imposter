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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnimeCharacter
import com.example.ui.components.BadgeChip
import com.example.ui.components.GlassCard
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonOutlineButton
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
fun CodexScreen(
    characters: List<AnimeCharacter>,
    selectedCategory: String,
    customName: String,
    customAnime: String,
    customCategory: String,
    customTraits: String,
    customAbilities: String,
    onCategoryFilter: (String) -> Unit,
    onCustomFieldsChanged: (String, String, String, String, String) -> Unit,
    onAddCustomCharacter: () -> Unit,
    onDeleteCharacter: (String) -> Unit,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf(
        "All Categories", "Shonen", "Action", "Fantasy",
        "Psychological", "Isekai", "Romance", "Villains", "Legends"
    )

    val filtered = characters.filter { char ->
        val matchesCat = selectedCategory == "All Categories" || char.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                char.name.contains(searchQuery, ignoreCase = true) ||
                char.anime.contains(searchQuery, ignoreCase = true)
        matchesCat && matchesSearch
    }

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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Header
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
                Column {
                    Text(
                        text = "Anime Codex & Database",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${characters.size} Characters in Database",
                        fontSize = 12.sp,
                        color = ArtisticLilac
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tabs: Browse vs Admin Add
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceCardDeep,
                contentColor = ArtisticLilac,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = ArtisticLilac
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Browse Codex", fontWeight = FontWeight.Bold, color = if (selectedTab == 0) ArtisticLilac else TextSecondary) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("+ Admin / Add Custom", fontWeight = FontWeight.Bold, color = if (selectedTab == 1) ArtisticLilac else TextSecondary) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedTab == 0) {
                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search character or anime series...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ArtisticLilac) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ArtisticLilac,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = SurfaceInput,
                        unfocusedContainerColor = SurfaceInput,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Filter Pills
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ArtisticLilac else SurfaceCardDeep)
                                .border(1.dp, if (isSelected) ArtisticLilac else BorderSubtle, RoundedCornerShape(10.dp))
                                .clickable { onCategoryFilter(cat) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                color = if (isSelected) TextDark else TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Character List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered) { character ->
                        CharacterCodexCard(
                            character = character,
                            onDelete = { onDeleteCharacter(character.id) }
                        )
                    }
                }
            } else {
                // Admin / Custom Character Form
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = ArtisticLilac)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Add Custom Anime Character", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                }

                                Text("Add any anime character to your local database for multiplayer and practice matches.", fontSize = 12.sp, color = TextSecondary)

                                OutlinedTextField(
                                    value = customName,
                                    onValueChange = { onCustomFieldsChanged(it, customAnime, customCategory, customTraits, customAbilities) },
                                    label = { Text("Character Full Name (e.g. Satoru Gojo)") },
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
                                    modifier = Modifier.fillMaxWidth().testTag("custom_char_name_input")
                                )

                                OutlinedTextField(
                                    value = customAnime,
                                    onValueChange = { onCustomFieldsChanged(customName, it, customCategory, customTraits, customAbilities) },
                                    label = { Text("Anime / Series Title (e.g. Jujutsu Kaisen)") },
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
                                    modifier = Modifier.fillMaxWidth().testTag("custom_char_anime_input")
                                )

                                Text("Category: $customCategory", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ArtisticMint)
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    categories.filter { it != "All Categories" }.forEach { cat ->
                                        val isSel = customCategory == cat
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSel) ArtisticMint else SurfaceCardDeep)
                                                .border(1.dp, if (isSel) ArtisticMint else BorderSubtle, RoundedCornerShape(10.dp))
                                                .clickable { onCustomFieldsChanged(customName, customAnime, cat, customTraits, customAbilities) }
                                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                        ) {
                                            Text(
                                                text = cat,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSel) TextDark else TextPrimary
                                            )
                                        }
                                    }
                                }

                                OutlinedTextField(
                                    value = customTraits,
                                    onValueChange = { onCustomFieldsChanged(customName, customAnime, customCategory, it, customAbilities) },
                                    label = { Text("Key Traits (comma separated: Blindfolded, Sweet tooth)") },
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
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = customAbilities,
                                    onValueChange = { onCustomFieldsChanged(customName, customAnime, customCategory, customTraits, it) },
                                    label = { Text("Abilities (comma separated: Limitless, Hollow Purple)") },
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
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                NeonButton(
                                    text = "SAVE CHARACTER TO CODEX",
                                    onClick = onAddCustomCharacter,
                                    enabled = customName.isNotBlank() && customAnime.isNotBlank(),
                                    gradient = ArtisticGradientPrimary,
                                    modifier = Modifier.fillMaxWidth(),
                                    testTag = "save_custom_character_button"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CharacterCodexCard(
    character: AnimeCharacter,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = character.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    if (character.isCustom) {
                        Spacer(modifier = Modifier.width(6.dp))
                        BadgeChip(text = "CUSTOM", color = ArtisticGold, backgroundColor = SurfaceCardDeep)
                    }
                }

                if (character.isCustom) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ArtisticRed, modifier = Modifier.size(16.dp))
                    }
                } else {
                    BadgeChip(text = character.difficulty, color = ArtisticLilac, backgroundColor = SurfaceCardDeep)
                }
            }

            Text(
                text = "${character.anime} • ${character.category}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisticMint
            )

            if (character.traits.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    character.traits.take(4).forEach { trait ->
                        BadgeChip(text = trait, color = TextSecondary, backgroundColor = SurfaceCardDeep)
                    }
                }
            }

            if (character.famousQuote.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "« ${character.famousQuote} »",
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    color = TextMuted
                )
            }
        }
    }
}
