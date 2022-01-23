package com.example.musicplayer.radio

import RadioRepository
import android.app.Application
import com.example.musicplayer.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RadioApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        AppDatabase.getDatabase(this, applicationScope)
    }
    val repository by lazy {
        RadioRepository(database.radioDao())
    }
}