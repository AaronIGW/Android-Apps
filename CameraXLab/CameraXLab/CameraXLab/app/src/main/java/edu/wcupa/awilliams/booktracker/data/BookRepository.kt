package edu.wcupa.awilliams.booktracker.data

import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getAll(): Flow<List<Book>>
    fun getById(id: Int): Flow<Book?>
    suspend fun add(book: Book)
    suspend fun update(book: Book)
    suspend fun delete(book: Book)
}
