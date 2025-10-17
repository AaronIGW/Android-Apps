package edu.wcupa.awilliams.booktracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.wcupa.awilliams.booktracker.data.Book
import edu.wcupa.awilliams.booktracker.data.BookDatabase
import edu.wcupa.awilliams.booktracker.data.BookRepository
import edu.wcupa.awilliams.booktracker.data.BookStatus
import edu.wcupa.awilliams.booktracker.data.OfflineBookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository =
        OfflineBookRepository(BookDatabase.getInstance(application).bookDao())

    private val search = MutableStateFlow("")
    private val status = MutableStateFlow<BookStatus?>(null)
    private val genre = MutableStateFlow<String?>(null)

    val books: StateFlow<List<Book>> =
        combine(repository.getAll(), search, status, genre) { list, q, s, g ->
            list.filter {
                (q.isBlank() || it.title.contains(q, true) || it.author.contains(q, true)) &&
                        (s == null || it.status == s) &&
                        (g.isNullOrBlank() || it.genre.equals(g, true))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearch(value: String) { search.value = value }
    fun setStatus(value: BookStatus?) { status.value = value }
    fun setGenre(value: String?) { genre.value = value }
}
