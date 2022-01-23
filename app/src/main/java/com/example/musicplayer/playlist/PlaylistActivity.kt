package com.example.musicplayer.playlist

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.MainActivity
import com.example.musicplayer.PLAYER_INTENT_MEDIA_ID
import com.example.musicplayer.R
import com.example.musicplayer.interfaces.OnItemClickListener

private lateinit var playlistsRecyclerView: RecyclerView

class PlaylistActivity : AppCompatActivity(), OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener{
            val replyIntent = Intent()
            setResult(Activity.RESULT_CANCELED, replyIntent)
            finish()
        }

        val songId = intent.getStringExtra(PLAYER_INTENT_MEDIA_ID)!!.toLong()

        playlistsRecyclerView = findViewById(R.id.playlistsRecyclerView)
        val playlistsAdapter = PlaylistAdapter(applicationContext, this)
        playlistsRecyclerView.layoutManager = LinearLayoutManager(this)
        playlistsRecyclerView.adapter = playlistsAdapter
        loadPlaylists(playlistsAdapter)

        val dividerItemDecoration = DividerItemDecoration(
            playlistsRecyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        playlistsRecyclerView.addItemDecoration(dividerItemDecoration)

        val addPlaylistButton = findViewById<Button>(R.id.newPlaylistButton)
        addPlaylistButton.setOnClickListener{
            showDialog()
        }
    }

    private fun loadPlaylists(playlistsAdapter: PlaylistAdapter){
        PlaylistObserver.getInstance(this).findPlaylists()
            .observe(this, Observer{ playlists ->
                    playlistsAdapter.setData(playlists)
            })
    }

    private fun createPlaylist(name: String){
        val resolver = contentResolver
        val values = ContentValues(1)
        // Stop depracating stuff that work please
        values.put(MediaStore.Audio.Playlists.NAME, name)
        val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        resolver.insert(uri, values)
    }

    private fun showDialog(){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Name your playlist")

        // Set up the input
        val input = EditText(this)

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.requestFocus()
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Create", DialogInterface.OnClickListener { _, _ ->
            // Here you get get input text from the Edittext
            val m_Text = input.text.toString()
            if(m_Text.isNotBlank()) {
                createPlaylist(m_Text)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else
                input.error = "Name cannot be left empty!"
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    override fun onItemClick(id: Long, uri: Uri, name: String?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(contentUri: Uri, contentName: String?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(id: Long) {
        TODO("Not yet implemented")
    }

    override fun onItemDeleteClick(id: Long) {
        TODO("Not yet implemented")
    }

}