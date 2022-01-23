package com.example.musicplayer.song

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.song.SongObserver.Companion.instance

class SongObserver constructor(private val context: Context) {

    private val uri = MediaStore.Audio.Media.IS_MUSIC + " != 0"

    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.DURATION
    )

    fun findSongs(albumId: Long): LiveData<List<Song>>{
        val songsLiveData = MutableLiveData<List<Song>>()
        val songList = mutableListOf<Song>()

        val selection = "${MediaStore.Audio.Albums.ALBUM_ID} == $albumId"

        context.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            "${MediaStore.Video.Media.DISPLAY_NAME} ASC" // Sort in alphabetical order
        ).use { cursor ->
            if (cursor?.moveToFirst() == true) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val artworkUri = Uri.parse("content://media/external/audio/albumart")
                    val albumArtUri = ContentUris.withAppendedId(artworkUri, albumId)
                    songList.add(
                        Song(
                            id = id,
                            title = title,
                            artist = artist,
                            album = album,
                            albumId = albumId,
                            artUri = albumArtUri,
                            duration = duration
                        )
                    )
                } while (cursor.moveToNext())
                cursor.close()
            }
        }

        return songsLiveData.apply{ value = songList}
    }

    fun findSongs(): LiveData<List<Song>> {

        val songsLiveData = MutableLiveData<List<Song>>()
        val songList = mutableListOf<Song>()

        // Get songs from provider
        context.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            uri,
            null,
            "${MediaStore.Video.Media.DISPLAY_NAME} ASC" // Sort in alphabetical order
        ).use { cursor ->
            if (cursor?.moveToFirst() == true) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val artworkUri = Uri.parse("content://media/external/audio/albumart")
                    val albumArtUri = ContentUris.withAppendedId(artworkUri, albumId)
                    songList.add(
                        Song(
                            id = id,
                            title = title,
                            artist = artist,
                            album = album,
                            albumId = albumId,
                            artUri = albumArtUri,
                            duration = duration
                        )
                    )
                } while (cursor.moveToNext())
                cursor.close()
            }
        }
        return songsLiveData.apply { value = songList }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SongObserver? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: SongObserver(context)
                    .also { instance = it }
            }
    }
}