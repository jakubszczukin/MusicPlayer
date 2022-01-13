package com.example.musicplayer

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util

const val PLAYER_INTENT_MEDIA_ID = "mediaId"
const val PLAYER_INTENT_MEDIA_NAME = "mediaName"

class PlayerActivity : AppCompatActivity() {

    private val notificationId = 1
    private val channelId = "channel_id"

    private var mPlayer: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private lateinit var songUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.playerView)
        val songName = findViewById<TextView>(R.id.songName)

        songUri = Uri.parse(intent.getStringExtra(PLAYER_INTENT_MEDIA_ID))
        songName.text = intent.getStringExtra(PLAYER_INTENT_MEDIA_NAME)
    }

    private fun initPlayer() {
        mPlayer = ExoPlayer.Builder(this).build()
        // Bind the player to the view.
        playerView.player = mPlayer
        playerView.defaultArtwork = ContextCompat.getDrawable(this, R.drawable.ic_baseline_music_video_24)

        val mediaItem = MediaItem.fromUri(songUri)

        mPlayer!!.setMediaItem(mediaItem)
        mPlayer!!.prepare()
        mPlayer!!.play()

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
        currentWindow = mPlayer!!.currentMediaItemIndex
        mPlayer!!.release()
        mPlayer = null
    }

}