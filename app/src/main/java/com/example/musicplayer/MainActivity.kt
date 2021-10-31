package com.example.musicplayer

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.mtechviral.mplaylib.MusicFinder

class MainActivity : AppCompatActivity() {

    private var mPlayer: SimpleExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var listView: ListView
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private val hlsUrl = "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"
    private lateinit var button : Button

    private lateinit var musicFinder: MusicFinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //playerView = findViewById(R.id.playerView)
        listView = findViewById(R.id.songListView)

        musicFinder = MusicFinder(contentResolver)
        musicFinder.prepare()

        val songs = musicFinder.allSongs


        //button = findViewById(R.id.kliknij)

        //button.setOnClickListener{
          //  permissionHandler(this)
        //}
        permissionHandler(this, songs)
    }

    private fun permissionHandler(context : Context, songs : List<MusicFinder.Song>){

        Dexter.withContext(context)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Toast.makeText(context, "All permissions are granted", Toast.LENGTH_LONG).show()
                    displaySongs(songs)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    showSettingsDialog(context)
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    token.continuePermissionRequest()
                    TODO("USER DENIED THE PERMISSIONS PERMANENTLY")
                }
            }).onSameThread().check()
    }

    private fun showSettingsDialog(context : Context){
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Need Permissions")

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, _ ->
            dialog!!.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            @Suppress("DEPRECATION")
            startActivityForResult(intent, 101)
        }
        //builder.setNegativeButton("Cancel"){ dialog, _ ->
        //    dialog!!.cancel()
        //}
        builder.show()
    }

    private fun displaySongs(songs : List<MusicFinder.Song>){
        val songTitles = mutableListOf<String>()
        for(song in songs){
            Log.d("TEST", song.title)
            Log.d("TEST", song.artist)
            Log.d("TEST", song.uri.toString())
            songTitles.add(song.title)
        }

        //for(song in songTitles){
        //    Log.d("TEST2", song)
        //}

        //val songsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, songTitles)
        //listView.adapter = songsAdapter

        val songsAdapter = SongsAdapter(this, songTitles)
        listView.adapter = songsAdapter
    }

/*
    private fun initPlayer() {
        mPlayer = SimpleExoPlayer.Builder(this).build()
        // Bind the player to the view.
        playerView.player = mPlayer
        mPlayer!!.playWhenReady = true
        mPlayer!!.seekTo(playbackPosition)
        mPlayer!!.prepare(buildMediaSource(), false, false)

    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || mPlayer == null) {
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }


    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        playWhenReady = mPlayer!!.playWhenReady
        playbackPosition = mPlayer!!.currentPosition
        currentWindow = mPlayer!!.currentWindowIndex
        mPlayer!!.release()
        mPlayer = null
    }

    private fun buildMediaSource(): MediaSource {
        val userAgent = Util.getUserAgent(playerView.context, playerView.context.getString(R.string.app_name))

        val dataSourceFactory = DefaultHttpDataSource.Factory()

        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(hlsUrl))
    }*/

}

