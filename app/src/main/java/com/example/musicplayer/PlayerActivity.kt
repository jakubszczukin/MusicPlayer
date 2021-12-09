package com.example.musicplayer

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat.stopForeground
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.offline.DownloadService.startForeground
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util

const val PLAYER_INTENT_MEDIA_ID = "mediaId"

class PlayerActivity : AppCompatActivity() {

    private val notificationId = 1
    private val channelId = "channel_id"

    private var mPlayer: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var listView: ListView
    private lateinit var playerNotificationManager: PlayerNotificationManager.Builder
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private val hlsUrl = "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"
    private lateinit var songUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.playerView)

        songUri = Uri.parse(intent.getStringExtra(PLAYER_INTENT_MEDIA_ID))
    }

    private fun initPlayerNotificationManager(){
        playerNotificationManager = PlayerNotificationManager.Builder(this, notificationId, channelId)
        playerNotificationManager.setMediaDescriptionAdapter(object: PlayerNotificationManager.MediaDescriptionAdapter{
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return "Title"
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                val intent = Intent(applicationContext, PlayerActivity::class.java)
                return PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            }

            override fun getCurrentContentText(player: Player): CharSequence? {
                return "Description"
            }

            override fun getCurrentSubText(player: Player): CharSequence { return ""}

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                return null
            }

        })

        playerNotificationManager.setNotificationListener(object: PlayerNotificationManager.NotificationListener{})
        playerNotificationManager.setChannelNameResourceId(R.string.channel_name)
        playerNotificationManager.setChannelDescriptionResourceId(R.string.channel_desc)
        playerNotificationManager.build()
    }


    private fun initPlayer() {
        mPlayer = ExoPlayer.Builder(this).build()
        // Bind the player to the view.
        playerView.player = mPlayer

        val mediaItem = MediaItem.fromUri(songUri)

        mPlayer!!.setMediaItem(mediaItem)
        mPlayer!!.prepare()
        mPlayer!!.play()
        //mPlayer!!.prepare(buildMediaSource(), false, false)

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

}