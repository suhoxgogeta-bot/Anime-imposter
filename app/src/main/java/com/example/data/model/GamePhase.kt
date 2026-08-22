package com.example.data.model

enum class GamePhase {
    LOBBY,
    ROLE_REVEAL,
    CHARACTER_REVEAL,
    CLUE_ROUND,
    CLUE_FEED,
    VOTING,
    VOTE_RESULTS,
    ELIMINATION,
    IMPOSTER_GUESS,
    GAME_OVER
}

enum class TieBreakerRule {
    REVOTE, RANDOM
}

enum class WinnerTeam {
    NONE, PLAYERS, IMPOSTER
}
