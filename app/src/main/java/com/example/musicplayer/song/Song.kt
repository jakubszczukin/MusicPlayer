package com.example.musicplayer.song

import android.net.Uri
import java.time.Duration

data class Song(
    var id: Long,
    var title: String,
    var artist: String,
    var albumId: Long,
    var album: String,
    var artUri: Uri,
    var duration: String
){
    companion object{
        fun convertDuration(duration: Long): String {
            val hours = duration / 3600000
            val minutes = duration / 60000 % 60000
            val seconds = duration % 60000 / 1000

        return when (hours){
            0L -> String.format("%02d:%02d", minutes, seconds)
            else -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }
        }
    }
}
