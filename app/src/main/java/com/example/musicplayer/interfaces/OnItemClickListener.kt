package com.example.musicplayer.interfaces

import android.net.Uri

interface OnItemClickListener {
    fun onItemClick(id: Long, uri: Uri, name: String?)
    fun onItemClick(contentUri: Uri, contentName: String?)
    fun onItemClick(id: Long)
    fun onItemDeleteClick(id: Long)
}