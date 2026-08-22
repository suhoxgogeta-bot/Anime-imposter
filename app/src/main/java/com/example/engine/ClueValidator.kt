package com.example.engine

import com.example.data.model.AnimeCharacter

object ClueValidator {

    sealed class ValidationResult {
        data object Valid : ValidationResult()
        data class Rejected(val reason: String) : ValidationResult()
    }

    private val BANNED_PATTERNS = listOf(
        "character is",
        "the name is",
        "name is",
        "its name",
        "this is",
        "the character is"
    )

    fun validateClue(
        rawClue: String,
        character: AnimeCharacter?
    ): ValidationResult {
        val trimmed = rawClue.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Rejected("Clue cannot be empty.")
        }
        if (trimmed.length < 2) {
            return ValidationResult.Rejected("Clue is too short.")
        }
        if (trimmed.length > 120) {
            return ValidationResult.Rejected("Clue must be under 120 characters.")
        }

        if (character == null) {
            return ValidationResult.Valid
        }

        val lowerClue = trimmed.lowercase()

        // 1. Check if character full name or distinct parts are in clue
        val forbiddenNames = mutableListOf<String>()
        forbiddenNames.add(character.name.lowercase())
        character.aliases.forEach { forbiddenNames.add(it.lowercase()) }

        // Split names by spaces (ignoring tiny words like 'd', 'of', 'the', 'von')
        val nameWords = character.name.lowercase().split(Regex("[\\s-]+")).filter { it.length > 2 }
        forbiddenNames.addAll(nameWords)

        for (forbidden in forbiddenNames) {
            if (forbidden.length >= 3 && containsWord(lowerClue, forbidden)) {
                return ValidationResult.Rejected("Spoiler alert! You cannot use the character's name or alias ('$forbidden') in your clue.")
            }
        }

        // 2. Check anime title
        val animeMain = character.anime.lowercase().split(":")[0].trim()
        val animeWords = animeMain.split(Regex("[\\s-]+")).filter { it.length > 3 && it !in setOf("dragon", "piece", "death", "hunter") }
        if (animeWords.isNotEmpty()) {
            for (word in animeWords) {
                if (containsWord(lowerClue, word)) {
                    return ValidationResult.Rejected("You cannot mention the anime title directly ('$word').")
                }
            }
        }

        return ValidationResult.Valid
    }

    private fun containsWord(text: String, word: String): Boolean {
        // Regex word boundary match
        val regex = Regex("\\b${Regex.escape(word)}\\b", RegexOption.IGNORE_CASE)
        return regex.containsMatchIn(text)
    }
}
