package com.example.musicplayer

import android.net.Uri

data class Song(
    var name: String,
    var artist: String,
    var album: String,
    var playlist: String,
    var uri: Uri

)
