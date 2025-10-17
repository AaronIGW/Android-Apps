package edu.wcupa.awilliams.booktracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.wcupa.awilliams.booktracker.data.Book
import edu.wcupa.awilliams.booktracker.data.BookDatabase
import edu.wcupa.awilliams.booktracker.data.BookRepository
import edu.wcupa.awilliams.booktracker.data.BookStatus
import edu.wcupa.awilliams.booktracker.data.OfflineBookRepository
import kotlinx.coroutines.launch

class BookEntryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository =
        OfflineBookRepository(BookDatabase.getInstance(application).bookDao())

    fun add(title: String, author: String, genre: String, status: BookStatus, progress: Int) {
        viewModelScope.launch {
            repository.add(
                Book(
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
}
