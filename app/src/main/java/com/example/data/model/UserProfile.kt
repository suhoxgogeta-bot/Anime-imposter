package com.example.data.model

data class UserProfile(
    val id: String = "user_me",
    val username: String = "ShadowDetective",
    val avatarId: String = "avatar_1",
    val level: Int = 1,
    val xp: Int = 0,
    val gamesPlayed: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val imposterWins: Int = 0,
    val playerWins: Int = 0,
    val timesAsImposter: Int = 0,
    val timesAsPlayer: Int = 0,
    val favoriteCategory: String = "Shonen",
    val currentWinStreak: Int = 0,
    val bestWinStreak: Int = 0
) {
    val winRatePercent: Int
        get() = if (gamesPlayed > 0) (wins * 100) / gamesPlayed else 0

    val xpForNextLevel: Int
        get() = level * 200

    val currentLevelXpProgress: Int
        get() = xp % 200
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean = false,
    val currentProgress: Int = 0,
    val maxProgress: Int = 1
)
