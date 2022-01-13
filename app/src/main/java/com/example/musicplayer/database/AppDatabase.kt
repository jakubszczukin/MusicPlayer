package com.example.musicplayer.database

import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.musicplayer.radio.Radio
import com.example.musicplayer.radio.RadioDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Radio::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun radioDao(): RadioDao

    companion object{

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : AppDatabase{

            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                ).addCallback(RadioDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class RadioDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase){
            super.onCreate(db)
            INSTANCE?.let{ database ->
                scope.launch{
                    //populateDatabase(database.radioDao())
                }
            }
        }

        fun populateDatabase(radioDao: RadioDao){
            radioDao.deleteAll()

            //add sample radio
            var radio = Radio("Radio1", "http://streaming.radio.lublin.pl:8000/128k")
            radioDao.insert(radio)
            radio = Radio("Radio2", "http://streaming.radio.lublin.pl:8000/128k")
            radioDao.insert(radio)
        }
    }

}