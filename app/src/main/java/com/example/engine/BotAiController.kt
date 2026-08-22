package com.example.engine

import com.example.data.model.AnimeCharacter
import com.example.data.model.ClueEntry
import com.example.data.model.Player
import kotlin.random.Random

object BotAiController {

    private val GENERIC_IMPOSTER_CLUES = listOf(
        "Fights with immense passion and never backs down.",
        "Has a memorable weapon and distinctive hair.",
        "Has a legendary rivalry with their counterpart.",
        "Underwent brutal training to protect their friends.",
        "Has a signature powerful technique everyone remembers.",
        "Has a traumatic backstory that drives their entire mission.",
        "Often seen smiling even in the face of impossible danger.",
        "Possesses a supernatural power feared by their enemies.",
        "Known for a famous transformation or awakened mode.",
        "Wears a very recognizable colored outfit or coat.",
        "Extremely determined to reach their ultimate life goal.",
        "Fought against corrupt authorities and dark organizations.",
        "Has a loyal companion that supports them through thick and thin.",
        "Known for eating huge amounts of food after intense battles.",
        "Possesses sharp tactical instincts when pushed into a corner."
    )

    private val BOT_NAMES = listOf(
        "Kenji_Sensei", "SakuraBlade", "NeonOtaku", "CyberNinja",
        "RamenLover99", "ShadowKage", "AkiraGhost", "ZeroRequiem",
        "MangaDuo", "ValkyrieX", "TitanSlayer", "GigaChadGojo"
    )

    private val AVATARS = listOf(
        "avatar_1", "avatar_2", "avatar_3", "avatar_4",
        "avatar_5", "avatar_6", "avatar_7", "avatar_8"
    )

    fun createBot(index: Int, existingNames: List<String>): Player {
        val availableNames = BOT_NAMES.filter { it !in existingNames }
        val name = if (availableNames.isNotEmpty()) availableNames.random() else "AnimeBot_${index + 1}"
        val avatar = AVATARS[index % AVATARS.size]
        val personalities = listOf("Subtle", "Bold", "Analytical", "Deceptive")
        return Player(
            id = "bot_${System.currentTimeMillis()}_$index",
            username = name,
            avatarId = avatar,
            level = Random.nextInt(2, 12),
            isHost = false,
            isReady = true,
            isBot = true,
            botPersonality = personalities.random()
        )
    }

    fun generateClueForBot(
        bot: Player,
        character: AnimeCharacter?,
        previousClues: List<ClueEntry>,
        clueRoundIndex: Int
    ): String {
        if (bot.isImposter || character == null) {
            // Imposter Bot AI: Generate a sneaky, believable generic clue or adapt to previous clues
            if (previousClues.isNotEmpty() && Random.nextFloat() > 0.35f) {
                // Adaptive imitation
                val lastClue = previousClues.last().clueText.lowercase()
                return when {
                    lastClue.contains("power") || lastClue.contains("technique") || lastClue.contains("mode") ->
                        "Wields an awe-inspiring ability that shakes the surrounding battlefield."
                    lastClue.contains("friend") || lastClue.contains("companion") || lastClue.contains("team") ->
                        "Cherishes their comrades above all else and refuses to let them fall."
                    lastClue.contains("dark") || lastClue.contains("curse") || lastClue.contains("evil") ->
                        "Has crossed paths with ancient darkness and forged their own destiny."
                    lastClue.contains("hair") || lastClue.contains("coat") || lastClue.contains("eyes") ->
                        "Their iconic visual silhouette is instantly recognizable across anime."
                    else -> GENERIC_IMPOSTER_CLUES.random()
                }
            } else {
                return GENERIC_IMPOSTER_CLUES.random()
            }
        } else {
            // Normal Player Bot AI: Select from traits, abilities, appearance or safe hints!
            val pool = mutableListOf<String>()

            // Traits
            character.traits.forEach { trait ->
                pool.add("Known because they $trait.")
                pool.add("A key aspect is: $trait.")
            }

            // Abilities (abstracted)
            character.abilities.forEach { ability ->
                // Avoid direct ability leaks if they have character name
                pool.add("Wields the power related to $ability.")
            }

            // Appearance
            if (character.appearance.isNotBlank()) {
                pool.add("Notable for: ${character.appearance}.")
            }

            // Spoiler-safe hints
            character.spoilerSafeHints.forEach { hint ->
                pool.add(hint)
            }

            // Category flavor
            pool.add("A legendary figure in ${character.category} anime lore.")

            // Filter out any accidentally banned names using ClueValidator
            val validClues = pool.filter { candidate ->
                ClueValidator.validateClue(candidate, character) is ClueValidator.ValidationResult.Valid
            }

            return if (validClues.isNotEmpty()) {
                validClues.random()
            } else {
                "An iconic protagonist with unforgettable resolve."
            }
        }
    }

    fun pickVoteForBot(
        bot: Player,
        eligibleSuspects: List<Player>,
        allClues: List<ClueEntry>,
        actualImposterId: String?
    ): Player {
        // Exclude self
        val candidates = eligibleSuspects.filter { it.id != bot.id }
        if (candidates.isEmpty()) return eligibleSuspects.first()

        if (bot.isImposter) {
            // Imposter bot votes for any active player with high confidence to blend in!
            return candidates.random()
        }

        // Normal bot evaluates candidates. Shorter or more generic clues might seem suspicious!
        val suspectScores = mutableMapOf<String, Float>()
        for (candidate in candidates) {
            var suspicion = Random.nextFloat() * 1.5f

            // Check candidate's clues
            val candidateClues = allClues.filter { it.playerId == candidate.id }
            for (clue in candidateClues) {
                // If candidate clue is super short or matches generic imposter pattern
                if (clue.clueText.length < 25) {
                    suspicion += 1.8f
                }
                if (GENERIC_IMPOSTER_CLUES.any { clue.clueText.contains(it.take(20), ignoreCase = true) }) {
                    suspicion += 2.5f
                }
            }

            // True imposter has slight natural suspicion bias to make bots capable of winning
            if (candidate.id == actualImposterId) {
                suspicion += 1.5f
            }

            suspectScores[candidate.id] = suspicion
        }

        val chosenId = suspectScores.maxByOrNull { it.value }?.key ?: candidates.random().id
        return candidates.firstOrNull { it.id == chosenId } ?: candidates.random()
    }
}
