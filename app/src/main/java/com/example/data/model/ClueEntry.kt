package com.example.data.model

data class ClueEntry(
    val id: String,
    val playerId: String,
    val playerName: String,
    val playerAvatarId: String,
    val roundNumber: Int, // Overall cycle
    val clueRoundIndex: Int, // e.g. Round 1 or Round 2
    val clueText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val wasAutoSubmitted: Boolean = false
)

data class VoteEntry(
    val voterId: String,
    val voterName: String,
    val targetPlayerId: String,
    val targetPlayerName: String,
    val roundNumber: Int
)

data class EliminationEvent(
    val eliminatedPlayer: Player,
    val votesReceived: Int,
    val isImposter: Boolean,
    val wasTieBroken: Boolean = false,
    val reason: String = ""
)
