package com.example.musicplayer.album

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.PLAYER_INTENT_MEDIA_ID
import com.example.musicplayer.PLAYER_INTENT_MEDIA_NAME
import com.example.musicplayer.PlayerActivity
import com.example.musicplayer.R
import com.example.musicplayer.album.AlbumListFragment.Companion.EXTRA_INTENT_ALBUM_COVER
import com.example.musicplayer.album.AlbumListFragment.Companion.EXTRA_INTENT_ALBUM_ID
import com.example.musicplayer.album.AlbumListFragment.Companion.EXTRA_INTENT_ALBUM_NAME
import com.example.musicplayer.interfaces.OnItemClickListener
import com.example.musicplayer.song.SongListAdapter
import com.example.musicplayer.song.SongObserver
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial

private lateinit var albumSongsRecyclerView: RecyclerView

class AlbumActivity : AppCompatActivity(), OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener{
            val replyIntent = Intent()
            setResult(Activity.RESULT_CANCELED, replyIntent)
            finish()
        }

        val currentAlbumId = intent.getStringExtra(EXTRA_INTENT_ALBUM_ID)
        val currentAlbumCover = intent.getStringExtra(EXTRA_INTENT_ALBUM_COVER)
        val currentAlbumName = intent.getStringExtra(EXTRA_INTENT_ALBUM_NAME)

        val albumCoverImageView = findViewById<ImageView>(R.id.albumCoverImageView)
        albumCoverImageView.setImageURI(Uri.parse(currentAlbumCover))

        val albumName = findViewById<TextView>(R.id.albumNameTextView)
        albumName.text = currentAlbumName

        albumSongsRecyclerView = findViewById(R.id.albumSongsRecyclerView)
        val albumSongsListAdapter = SongListAdapter(this, this)
        albumSongsRecyclerView.layoutManager = LinearLayoutManager(this)
        albumSongsRecyclerView.adapter = albumSongsListAdapter

        loadSongs(albumSongsListAdapter, currentAlbumId!!.toLong())

        val dividerItemDecoration = DividerItemDecoration(
            albumSongsRecyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        albumSongsRecyclerView.addItemDecoration(dividerItemDecoration)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Initialize and inflate menu for action bar
        menuInflater.inflate(R.menu.action_bar_menu, menu)

        val themeSwitch = findViewById<SwitchMaterial>(R.id.dayNightSwitch)
        themeSwitch.setOnCheckedChangeListener{ _, id ->
            when(id){
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun loadSongs(listAdapter: SongListAdapter, currentAlbumId: Long) {
        SongObserver.getInstance(this).findSongs(currentAlbumId)
            .observe(this, Observer { songs ->
                listAdapter.setData(songs)
            })
    }

    override fun onItemClick(id: Long, uri: Uri, name: String?) {
        return
    }

    override fun onItemClick(contentUri: Uri, contentName: String?) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(PLAYER_INTENT_MEDIA_ID, contentUri.toString())
        intent.putExtra(PLAYER_INTENT_MEDIA_NAME, contentName)
        startActivity(intent)
    }

    override fun onItemClick(id: Long) {
        return
    }

    override fun onItemDeleteClick(id: Long) {
        return
    }
}