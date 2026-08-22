package com.example.data.model

data class AnimeCharacter(
    val id: String,
    val name: String,
    val anime: String,
    val aliases: List<String> = emptyList(),
    val difficulty: String = "Medium", // Easy, Medium, Hard
    val category: String = "Shonen", // Shonen, Action, Fantasy, Psychological, Isekai, Romance, Villains, Legends
    val traits: List<String> = emptyList(),
    val abilities: List<String> = emptyList(),
    val famousQuote: String = "",
    val appearance: String = "",
    val spoilerSafeHints: List<String> = emptyList(),
    val isCustom: Boolean = false
)

enum class GameDifficulty {
    EASY, MEDIUM, HARD, ALL
}
