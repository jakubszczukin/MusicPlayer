package com.example.musicplayer.album

import android.net.Uri

data class Album(
    var id: Long,
    var name: String? = null,
    var artist: String? = null,
    var coverUri: Uri
)
