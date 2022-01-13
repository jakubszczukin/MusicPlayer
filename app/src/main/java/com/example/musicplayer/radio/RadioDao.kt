package com.example.musicplayer.radio

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/*
    DAO -> Data access object, used to interact with the stored data
    Each DAO includes methods that offer abstract access to app's database
 */

@Dao
interface RadioDao {
    @Query("SELECT * FROM radio ORDER BY name ASC")
    fun getAll(): Flow<List<Radio>>

    @Query("SELECT * FROM radio ORDER BY id LIMIT 1")
    fun getFirst(): Flow<List<Radio>>

    @Query("SELECT * FROM radio WHERE id LIKE :radioId")
    fun getById(radioId: Long): Radio

    @Query("DELETE FROM radio WHERE id LIKE :radioId")
    fun deleteById(radioId: Long)

    @Insert
    fun insert(radio: Radio)

    @Delete
    fun delete(radio: Radio)

    @Query("DELETE FROM radio")
    fun deleteAll()

    @Update
    fun update(radio: Radio)
}