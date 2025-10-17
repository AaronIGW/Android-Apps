package edu.wcupa.awilliams.acronymhero.ui

data class GameUIState(
    val currentAcronym: String = "",
    val categoryHint: String? = null,
    val firstLettersHint: String? = null,
    val isGuessWrong: Boolean = false,
    val isGameOver: Boolean = false,
    val score: Int = 0,
    val wordCount: Int = 0,
    val totalRounds: Int = 10
)
