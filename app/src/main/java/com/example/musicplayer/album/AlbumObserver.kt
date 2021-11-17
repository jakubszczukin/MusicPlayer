package com.example.musicplayer.album

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AlbumObserver constructor(private val context: Context) {

    private val projection = arrayOf(
        MediaStore.Audio.Albums._ID,
        MediaStore.Audio.Albums.ALBUM,
        MediaStore.Audio.Albums.ARTIST
    )

    fun findAlbums(): LiveData<List<Album>>{

        val albumsLiveData = MutableLiveData<List<Album>>()
        val albumList = mutableListOf<Album>()

        // Get albums from provider
        context.contentResolver?.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Audio.Media.ALBUM} ASC" // Sort in alphabetical order
        ).use{ cursor ->
            if(cursor?.moveToFirst() == true){
                do{
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
                    val name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                    val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
                    val artworkUri = Uri.parse("content://media/external/audio/albumart")
                    val albumUri = ContentUris.withAppendedId(artworkUri, id)
                    albumList.add(
                        Album(
                            id = id,
                            name = name,
                            artist = artist,
                            coverUri = albumUri
                        )
                    )
                } while(cursor.moveToNext())
            }
        }
        return albumsLiveData.apply { value = albumList}
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: AlbumObserver? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: AlbumObserver(context)
                    .also { instance = it }
            }
    }

}