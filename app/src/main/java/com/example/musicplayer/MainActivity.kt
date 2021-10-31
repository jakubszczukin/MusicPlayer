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
import android.widget.*
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

    private lateinit var listView: ListView

    private lateinit var musicFinder: MusicFinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.songListView)

        musicFinder = MusicFinder(contentResolver)
        musicFinder.prepare()

        val songs = musicFinder.allSongs

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
        if(songs.isEmpty()) Log.d("TEST", "PUSTO")
        for(song in songs){
            Log.d("TEST", song.title)
            Log.d("TEST", song.artist)
            Log.d("TEST", song.uri.toString())
            songTitles.add(song.title)
        }

        val songsAdapter = SongsAdapter(this, songTitles)
        listView.adapter = songsAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener{
                _, _, position, _ ->
            val intent = Intent(this, PlayerActivity::class.java)
            Log.d("URITEST", songs.elementAt(position).uri.toString())
            intent.putExtra(PLAYER_INTENT_MEDIA_ID, songs.elementAt(position).uri.toString())
            startActivity(intent)
        }
    }

/*
   */

}

