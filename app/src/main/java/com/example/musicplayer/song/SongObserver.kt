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

    fun getPlaylistSongs(id: Long): LiveData<List<Song>>{
        val arrayLiveData = MutableLiveData<List<Song>>()
        val array = mutableListOf<Song>()

        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}  != 0"
        val sortOrder = "${MediaStore.Audio.Playlists.Members.PLAY_ORDER} ASC"

        val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        if(cursor != null) {
            cursor.moveToFirst()
            while(!cursor.isAfterLast) {
                val songId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                val duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val artworkUri = Uri.parse("content://media/external/audio/albumart")
                val albumArtUri = ContentUris.withAppendedId(artworkUri, albumId)

                array.add(
                    Song(
                        id = songId,
                        title = title,
                        artist = artist,
                        album = album,
                        albumId = albumId,
                        artUri = albumArtUri,
                        duration = duration
                    )
                )

                cursor.moveToNext()
            }

            cursor.close()
        }

        return arrayLiveData.apply{ value = array }
    }

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