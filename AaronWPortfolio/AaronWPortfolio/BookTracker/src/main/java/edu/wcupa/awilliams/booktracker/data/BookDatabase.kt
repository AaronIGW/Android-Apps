package edu.wcupa.awilliams.booktracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Book::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    companion object {
        @Volatile private var INSTANCE: BookDatabase? = null
        fun getInstance(context: Context): BookDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "booktracker.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}
