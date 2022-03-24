package com.example.musicplayer.playlist

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
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
import com.example.musicplayer.song.Song

private lateinit var playlistsRecyclerView: RecyclerView
private var songId: Long = 0

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

        songId = intent.getStringExtra(PLAYER_INTENT_MEDIA_ID)!!.toLong()

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
            val mText = input.text.toString()
            if(mText.isNotBlank()) {
                createPlaylist(mText)
            }
            else
                input.error = "Name cannot be left empty!"
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private fun addSongToPlaylist(id: Long, songId: Long){
        val contentValues = ContentValues(1)
        val count = getPlaylistSize(id)

        Log.d("COUNT", "count : $count")

        contentValues.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, count + 1)
        contentValues.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, songId)

        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)
        val resolver = contentResolver
        resolver.insert(uri, contentValues)
        resolver.notifyChange(Uri.parse("content://media"), null)
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
        addSongToPlaylist(id, songId)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onItemDeleteClick(id: Long) {
        TODO("Not yet implemented")
    }

}