package com.example.musicplayer.playlist

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.album.AlbumObserver
import com.example.musicplayer.song.Song

class PlaylistObserver constructor(private val context: Context) {

    fun findPlaylists(): LiveData<List<Playlist>>{
        val arrayLiveData = MutableLiveData<List<Playlist>>()
        val array = mutableListOf<Playlist>()

        val uri  = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Playlists._ID,
            MediaStore.Audio.Playlists.NAME
        )

        val sortOrder = "${MediaStore.Audio.Playlists.NAME} ASC"

        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        if(cursor != null) {
            cursor.moveToFirst()
            while(!cursor.isAfterLast) {
                val id  = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID)).toLong()
                val name: String = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME))

                cursor.moveToNext()

                array.add(Playlist(id, name))
            }

            cursor.close()
        }

        return arrayLiveData.apply{ value = array }
    }

    fun createPlaylist(name: String){
        val resolver = context.contentResolver
        val values = ContentValues(1)
        // Stop depracating stuff that work
        values.put(MediaStore.Audio.Playlists.NAME, name)
        val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        resolver.insert(uri, values)
    }

    fun deletePlaylist(id: Long){
        val resolver = context.contentResolver
        val location = MediaStore.Audio.Playlists._ID + "=?"
        val locationVal = arrayOf(id.toString())
        resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, location, locationVal)
    }

    fun addSongToPlaylist(id: Long, song: Song){
        val contentValues = ContentValues()
        val count = getPlaylistSize(id)

        contentValues.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, count + 1)
        contentValues.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, song.id)

        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)
        val resolver = context.contentResolver
        resolver.insert(uri, contentValues)
        resolver.notifyChange(Uri.parse("content://media"), null)
    }

    fun getPlaylistSize(id: Long): Int{
        var count = 0
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)

        val projection = arrayOf(MediaStore.Audio.Playlists.Members._ID)
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val cursor: Cursor? = context.contentResolver.query(uri, projection, selection, null, null)

        if (cursor != null) {
            cursor.moveToFirst()

            while (!cursor.isAfterLast) {
                count++
                cursor.moveToNext()
            }

            cursor.close()
        }

        return count
    }

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

    fun deletePlaylistSong(playlistId: Long, songId: Long){
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId)
        val where = MediaStore.Audio.Playlists.Members._ID + "=?"
        val whereVal = arrayOf(songId.toString())
        context.contentResolver.delete(uri, where, whereVal)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: PlaylistObserver? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: PlaylistObserver(context)
                    .also { instance = it }
            }
    }
}