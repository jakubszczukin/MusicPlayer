package com.example.musicplayer.playlist

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.PLAYER_INTENT_MEDIA_ID
import com.example.musicplayer.R
import com.example.musicplayer.interfaces.OnItemClickListener
import com.example.musicplayer.song.SongListAdapter
import com.example.musicplayer.song.SongObserver

private lateinit var playlistsRecyclerView: RecyclerView

class PlaylistSongsActivity : AppCompatActivity(), OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_songs)

        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener{
            val replyIntent = Intent()
            setResult(Activity.RESULT_CANCELED, replyIntent)
            finish()
        }

        val currentPlaylistId = intent.getStringExtra(PLAYER_INTENT_MEDIA_ID)!!.toLong()

        playlistsRecyclerView = findViewById(R.id.playlistsRecyclerView)
        val playlistSongListAdapter = SongListAdapter(applicationContext, this)
        playlistsRecyclerView.layoutManager = LinearLayoutManager(this)
        playlistsRecyclerView.adapter = playlistSongListAdapter
        loadSongs(playlistSongListAdapter, currentPlaylistId)

        val dividerItemDecoration = DividerItemDecoration(
            playlistsRecyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        playlistsRecyclerView.addItemDecoration(dividerItemDecoration)

        if(getPlaylistSize(currentPlaylistId) == 0){
            Toast.makeText(this, "test", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "test2222", Toast.LENGTH_LONG).show()
        }

    }

    private fun loadSongs(listAdapter: SongListAdapter, currentPlaylistId: Long) {
        SongObserver.getInstance(this).getPlaylistSongs(currentPlaylistId)
            .observe(this, Observer { songs ->
                listAdapter.setData(songs)
            })
    }

    private fun getPlaylistSize(id: Long): Int{
        var count = 0
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)

        val projection = arrayOf(MediaStore.Audio.Playlists.Members._ID)
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val cursor: Cursor? = this.contentResolver.query(uri, projection, selection, null, null)

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

    override fun onItemClick(id: Long, uri: Uri, name: String?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(contentUri: Uri, contentName: String?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(id: Long) {
        val intent = Intent(baseContext, PlaylistActivity::class.java)
        intent.putExtra(PLAYER_INTENT_MEDIA_ID, id.toString())
        startActivity(intent)
    }

    override fun onItemDeleteClick(id: Long) {
        TODO("Not yet implemented")
    }
}