package com.example.musicplayer.radio

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RadioDao {
    @Query("SELECT * FROM radio ORDER BY name ASC")
    fun getAll(): Flow<List<Radio>>

    @Query("SELECT * FROM radio WHERE id LIKE :radioId")
    fun getById(radioId: Long): Radio

    @Insert
    fun insert(radio: Radio)

    @Delete
    fun delete(radio: Radio)

    @Query("DELETE FROM radio")
    fun deleteAll()

    @Update
    fun update(radio: Radio)
}