package com.example.musicplayer.song

import android.net.Uri

data class Song(
	var id: Long,
	var title: String,
	var artist: String,
	var album: String,
	var albumId: Long,
	var duration: String,
	var artUri: Uri
)
