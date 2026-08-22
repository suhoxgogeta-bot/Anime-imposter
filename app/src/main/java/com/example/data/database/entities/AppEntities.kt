package com.example.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: String,
    val name: String,
    val anime: String,
    val aliasesJson: String, // stored as json or comma list
    val difficulty: String,
    val category: String,
    val traitsJson: String,
    val abilitiesJson: String,
    val famousQuote: String,
    val appearance: String,
    val hintsJson: String,
    val isCustom: Boolean
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val username: String,
    val avatarId: String,
    val level: Int,
    val xp: Int,
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val imposterWins: Int,
    val playerWins: Int,
    val timesAsImposter: Int,
    val timesAsPlayer: Int,
    val favoriteCategory: String,
    val currentWinStreak: Int,
    val bestWinStreak: Int
)

@Entity(tableName = "match_history")
data class MatchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val matchId: Long = 0,
    val timestamp: Long,
    val characterName: String,
    val animeName: String,
    val wasImposter: Boolean,
    val winnerTeam: String,
    val survivedRounds: Int,
    val xpEarned: Int
)
