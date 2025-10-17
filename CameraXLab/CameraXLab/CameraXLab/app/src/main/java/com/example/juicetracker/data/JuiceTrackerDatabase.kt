package com.example.juicetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Juice::class], version = 1, exportSchema = false)
abstract class JuiceTrackerDatabase : RoomDatabase() {
    abstract fun juiceDao(): JuiceDao

    companion object {
        @Volatile
        private var INSTANCE: JuiceTrackerDatabase? = null

        fun getDatabase(context: Context): JuiceTrackerDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    JuiceTrackerDatabase::class.java,
                    "juice_tracker_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
