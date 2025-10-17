package edu.wcupa.awilliams.booktracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<Book?>

    @Insert
    suspend fun insert(book: Book): Long

    @Update
    suspend fun update(book: Book)

    @Delete
    suspend fun delete(book: Book)
}
