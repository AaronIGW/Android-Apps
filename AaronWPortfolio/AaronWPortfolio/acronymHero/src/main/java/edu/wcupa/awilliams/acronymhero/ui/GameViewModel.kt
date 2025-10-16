package edu.wcupa.awilliams.acronymhero.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.wcupa.awilliams.acronymhero.data.WordsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {
    private val data = WordsData.items
    private val used = mutableSetOf<Int>()
    private var currentIndex = -1
    private var categoryRevealed = false
    private var firstLettersRevealed = false

    private val _uiState = MutableStateFlow(GameUIState(totalRounds = 10))
    val uiState: StateFlow<GameUIState> = _uiState.asStateFlow()

    init {
        nextWord()
    }

    fun submitGuess(guess: String) {
        if (_uiState.value.isGameOver) return
        val target = data[currentIndex].expansion
        val ok = normalize(guess) == normalize(target)
        if (ok) {
            val s = _uiState.value.score + 10
            viewModelScope.launch { _uiState.emit(_uiState.value.copy(isGuessWrong = false, score = s)) }
            nextWord()
        } else {
            viewModelScope.launch { _uiState.emit(_uiState.value.copy(isGuessWrong = true)) }
        }
    }

    fun skip() {
        if (_uiState.value.isGameOver) return
        nextWord()
    }

    fun revealCategory() {
        if (_uiState.value.isGameOver) return
        categoryRevealed = true
        pushHints()
    }

    fun revealFirstLetters() {
        if (_uiState.value.isGameOver) return
        firstLettersRevealed = true
        pushHints()
    }

    fun resetGame() {
        used.clear()
        currentIndex = -1
        categoryRevealed = false
        firstLettersRevealed = false
        viewModelScope.launch { _uiState.emit(GameUIState(totalRounds = _uiState.value.totalRounds)) }
        nextWord()
    }

    private fun nextWord() {
        if (used.size >= _uiState.value.totalRounds || used.size >= data.size) {
            viewModelScope.launch { _uiState.emit(_uiState.value.copy(isGameOver = true)) }
            return
        }
        var i: Int
        do { i = Random.nextInt(data.size) } while (used.contains(i))
        used.add(i)
        currentIndex = i
        categoryRevealed = false
        firstLettersRevealed = false
        val item = data[i]
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    currentAcronym = item.short,
                    categoryHint = null,
                    firstLettersHint = null,
                    isGuessWrong = false,
                    wordCount = used.size
                )
            )
        }
    }

    private fun pushHints() {
        val item = data[currentIndex]
        val firstLetters = item.expansion.split(" ").filter { it.isNotBlank() }.joinToString(" ") { it.first().uppercase() }
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    categoryHint = if (categoryRevealed) item.category else null,
                    firstLettersHint = if (firstLettersRevealed) firstLetters else null
                )
            )
        }
    }

    private fun normalize(s: String): String =
        s.lowercase().filter { it.isLetterOrDigit() || it.isWhitespace() }.split(Regex("\\s+")).filter { it.isNotBlank() }.joinToString(" ")
}
