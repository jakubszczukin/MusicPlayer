package com.example.musicplayer.album

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.song.Song

class AlbumObserver constructor(private val context: Context) {

    //private val projection = arrayOf(
    //    MediaStore.Audio.Albums._ID,
    //    MediaStore.Audio.Albums.ALBUM,
    //    MediaStore.Audio.Albums.ARTIST,
    //    //MediaStore.Audio.Albums.ALBUM_ART
    //)

    private val projection = arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ALBUM_ART)
    private val externalUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
    private val selection = null
    private val selectionArgs = null
    private val sortOrder = MediaStore.Audio.Media.ALBUM + " ASC"


    fun findAlbums(): LiveData<List<Album>>{

        val albumsLiveData = MutableLiveData<List<Album>>()
        val albumList = mutableListOf<Album>()

        val cursor = context.contentResolver?.query(
            externalUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        Log.d("ARRAY", projection.toString())

        if(cursor != null){
            while(cursor.moveToNext()){
                val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID))
                val albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM))
                val artistName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST))
                val artworkUri = Uri.parse("content://media/external/audio/albumart")
                val albumArtUri = ContentUris.withAppendedId(artworkUri, albumId)
                albumList.add(
                    Album(
                        id = albumId,
                        name = albumName,
                        artist = artistName,
                        coverUri = albumArtUri
                    )
                )
            }
            cursor.close()
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