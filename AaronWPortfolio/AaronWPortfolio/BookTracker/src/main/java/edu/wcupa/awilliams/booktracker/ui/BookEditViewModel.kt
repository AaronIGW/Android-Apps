package edu.wcupa.awilliams.booktracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.wcupa.awilliams.booktracker.data.Book
import edu.wcupa.awilliams.booktracker.data.BookDatabase
import edu.wcupa.awilliams.booktracker.data.BookRepository
import edu.wcupa.awilliams.booktracker.data.BookStatus
import edu.wcupa.awilliams.booktracker.data.OfflineBookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BookEditViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository =
        OfflineBookRepository(BookDatabase.getInstance(application).bookDao())

    fun get(id: Int): Flow<Book?> = repository.getById(id)

    fun update(id: Int, title: String, author: String, genre: String, status: BookStatus, progress: Int) {
        viewModelScope.launch {
            repository.update(
                Book(
                    id = id,
                    title = title.trim(),
                    author = author.trim(),
                    genre = genre.trim(),
                    status = status,
                    progress = progress,
                    finishedAt = if (status == BookStatus.FINISHED) System.currentTimeMillis() else null
                )
            )
        }
    }

    fun delete(book: Book) {
        viewModelScope.launch { repository.delete(book) }
    }
}
