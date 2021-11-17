package com.example.musicplayer.album

import android.net.Uri

data class Album(
    var id: Long,
    var name: String,
    var artist: String,
    var coverUri: Uri
)
