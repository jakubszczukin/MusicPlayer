package com.example.musicplayer

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util


const val PLAYER_INTENT_MEDIA_ID = "mediaId"
const val PLAYER_INTENT_MEDIA_NAME = "mediaName"

class PlayerActivity : AppCompatActivity() {

    private val notificationId = 1
    private val channelId = "channel_id"

    private lateinit var mediaSessionCompat: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

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

        // MediaSession stuff
        mediaSessionCompat = MediaSessionCompat(this, "sample")
        mediaSessionConnector = MediaSessionConnector(mediaSessionCompat)
        mediaSessionConnector.setPlayer(mPlayer)

        // Bind the player to the view.
        playerView.player = mPlayer

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
        mediaSessionCompat.isActive = true
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
        mediaSessionCompat.isActive = false
    }


    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }

        // Release the media session if null
        mediaSessionCompat.release()

        playWhenReady = mPlayer!!.playWhenReady
        playbackPosition = mPlayer!!.currentPosition
        currentWindow = mPlayer!!.currentMediaItemIndex
        mPlayer!!.release()
        mPlayer = null
    }

}