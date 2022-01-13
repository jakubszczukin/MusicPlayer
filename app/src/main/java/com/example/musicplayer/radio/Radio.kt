package com.example.musicplayer.radio

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Radio(
    //@PrimaryKey(autoGenerate = true) val id: Long = Long.MIN_VALUE,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "uri") var uri: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
