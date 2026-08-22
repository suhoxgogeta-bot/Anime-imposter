package com.example.data.model

data class Player(
    val id: String,
    val username: String,
    val avatarId: String, // e.g. "avatar_1", "avatar_2"
    val level: Int = 1,
    val isHost: Boolean = false,
    val isReady: Boolean = false,
    val isBot: Boolean = false,
    val isEliminated: Boolean = false,
    val isImposter: Boolean = false,
    val votesReceived: Int = 0,
    val isConnected: Boolean = true,
    val botPersonality: String = "Balanced" // Subtle, Bold, Analytical, Deceptive
)
