package com.example.data.model

data class GameSettings(
    val playerLimit: Int = 8,
    val clueRoundsBeforeVoting: Int = 2,
    val clueTimerSeconds: Int = 30,
    val votingTimerSeconds: Int = 25,
    val imposterGuessingEnabled: Boolean = true,
    val revealRoleOnElimination: Boolean = true,
    val tieBreakerRule: TieBreakerRule = TieBreakerRule.REVOTE,
    val category: String = "All Categories",
    val difficulty: GameDifficulty = GameDifficulty.ALL
)
