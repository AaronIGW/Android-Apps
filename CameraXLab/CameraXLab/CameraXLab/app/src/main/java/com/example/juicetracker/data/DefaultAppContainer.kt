package com.example.juicetracker.data

import android.content.Context

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val database by lazy { JuiceTrackerDatabase.getDatabase(context) }

    override val trackerRepository: JuiceRepository by lazy {
        RoomJuiceRepository(database.juiceDao())
    }
}
