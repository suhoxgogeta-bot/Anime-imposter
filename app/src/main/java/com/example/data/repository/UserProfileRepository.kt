package com.example.data.repository

import com.example.data.database.dao.MatchHistoryDao
import com.example.data.database.dao.UserProfileDao
import com.example.data.database.entities.MatchHistoryEntity
import com.example.data.database.entities.UserProfileEntity
import com.example.data.model.Achievement
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepository(
    private val userProfileDao: UserProfileDao,
    private val matchHistoryDao: MatchHistoryDao
) {
    val userProfile: Flow<UserProfile> = userProfileDao.getUserProfile("user_me").map { entity ->
        entity?.toDomain() ?: UserProfile()
    }

    val recentMatches: Flow<List<MatchHistoryEntity>> = matchHistoryDao.getRecentMatches()

    suspend fun getProfileOnce(): UserProfile {
        return userProfileDao.getUserProfileOnce("user_me")?.toDomain() ?: UserProfile()
    }

    suspend fun updateUsernameAndAvatar(username: String, avatarId: String) {
        val current = getProfileOnce()
        val updated = current.copy(username = username.trim(), avatarId = avatarId)
        userProfileDao.insertOrUpdateProfile(updated.toEntity())
    }

    suspend fun recordGameResult(
        wasImposter: Boolean,
        didWin: Boolean,
        characterName: String,
        animeName: String,
        category: String,
        survivedRounds: Int
    ): Pair<UserProfile, Int> {
        val current = getProfileOnce()

        val baseXP = if (didWin) 120 else 50
        val bonusXP = (survivedRounds * 20) + (if (wasImposter && didWin) 100 else 0)
        val earnedXP = baseXP + bonusXP

        val newTotalXP = current.xp + earnedXP
        val newLevel = (newTotalXP / 200) + 1

        val newStreak = if (didWin) current.currentWinStreak + 1 else 0
        val bestStreak = maxOf(current.bestWinStreak, newStreak)

        val updated = current.copy(
            level = newLevel,
            xp = newTotalXP,
            gamesPlayed = current.gamesPlayed + 1,
            wins = if (didWin) current.wins + 1 else current.wins,
            losses = if (!didWin) current.losses + 1 else current.losses,
            imposterWins = if (wasImposter && didWin) current.imposterWins + 1 else current.imposterWins,
            playerWins = if (!wasImposter && didWin) current.playerWins + 1 else current.playerWins,
            timesAsImposter = if (wasImposter) current.timesAsImposter + 1 else current.timesAsImposter,
            timesAsPlayer = if (!wasImposter) current.timesAsPlayer + 1 else current.timesAsPlayer,
            currentWinStreak = newStreak,
            bestWinStreak = bestStreak,
            favoriteCategory = category
        )

        userProfileDao.insertOrUpdateProfile(updated.toEntity())

        matchHistoryDao.insertMatch(
            MatchHistoryEntity(
                timestamp = System.currentTimeMillis(),
                characterName = characterName,
                animeName = animeName,
                wasImposter = wasImposter,
                winnerTeam = if (didWin) (if (wasImposter) "IMPOSTER" else "PLAYERS") else (if (wasImposter) "PLAYERS" else "IMPOSTER"),
                survivedRounds = survivedRounds,
                xpEarned = earnedXP
            )
        )

        return Pair(updated, earnedXP)
    }

    fun computeAchievements(profile: UserProfile): List<Achievement> {
        return listOf(
            Achievement(
                id = "ach_first_blood",
                title = "First Deduction",
                description = "Win your first game as Normal Player or Imposter.",
                iconEmoji = "🏆",
                isUnlocked = profile.wins >= 1,
                currentProgress = minOf(profile.wins, 1),
                maxProgress = 1
            ),
            Achievement(
                id = "ach_master_imposter",
                title = "Master of Shadows",
                description = "Win 3 games as the secret Imposter without getting caught.",
                iconEmoji = "🕵️",
                isUnlocked = profile.imposterWins >= 3,
                currentProgress = minOf(profile.imposterWins, 3),
                maxProgress = 3
            ),
            Achievement(
                id = "ach_detective_prodigy",
                title = "Detective Prodigy",
                description = "Win 5 games as a Normal Player through keen deduction.",
                iconEmoji = "🔍",
                isUnlocked = profile.playerWins >= 5,
                currentProgress = minOf(profile.playerWins, 5),
                maxProgress = 5
            ),
            Achievement(
                id = "ach_veteran_weeb",
                title = "Anime Sage",
                description = "Play 10 multiplayer or solo practice matches.",
                iconEmoji = "🎌",
                isUnlocked = profile.gamesPlayed >= 10,
                currentProgress = minOf(profile.gamesPlayed, 10),
                maxProgress = 10
            ),
            Achievement(
                id = "ach_on_fire",
                title = "Unstoppable Wits",
                description = "Achieve a 3-win streak in competitive matches.",
                iconEmoji = "🔥",
                isUnlocked = profile.bestWinStreak >= 3,
                currentProgress = minOf(profile.bestWinStreak, 3),
                maxProgress = 3
            ),
            Achievement(
                id = "ach_level_five",
                title = "Grand Tactician",
                description = "Reach Level 5 in player progression.",
                iconEmoji = "👑",
                isUnlocked = profile.level >= 5,
                currentProgress = minOf(profile.level, 5),
                maxProgress = 5
            )
        )
    }
}

fun UserProfileEntity.toDomain(): UserProfile {
    return UserProfile(
        id = id,
        username = username,
        avatarId = avatarId,
        level = level,
        xp = xp,
        gamesPlayed = gamesPlayed,
        wins = wins,
        losses = losses,
        imposterWins = imposterWins,
        playerWins = playerWins,
        timesAsImposter = timesAsImposter,
        timesAsPlayer = timesAsPlayer,
        favoriteCategory = favoriteCategory,
        currentWinStreak = currentWinStreak,
        bestWinStreak = bestWinStreak
    )
}

fun UserProfile.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = id,
        username = username,
        avatarId = avatarId,
        level = level,
        xp = xp,
        gamesPlayed = gamesPlayed,
        wins = wins,
        losses = losses,
        imposterWins = imposterWins,
        playerWins = playerWins,
        timesAsImposter = timesAsImposter,
        timesAsPlayer = timesAsPlayer,
        favoriteCategory = favoriteCategory,
        currentWinStreak = currentWinStreak,
        bestWinStreak = bestWinStreak
    )
}
