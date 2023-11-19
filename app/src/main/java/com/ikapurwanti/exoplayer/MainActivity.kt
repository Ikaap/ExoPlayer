package com.ikapurwanti.exoplayer

import android.annotation.SuppressLint
import android.media.browse.MediaBrowser.MediaItem
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.ikapurwanti.exoplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var exoPlayer : ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = false

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        preparePlayer()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun preparePlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer?.playWhenReady = true
        binding.playerView.player = exoPlayer

        val mediaItem = androidx.media3.common.MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")

        // menggunakan progressiveMediaSource karena untuk file biasa
        val mediaSourceFactory = ProgressiveMediaSource.Factory (
            DefaultDataSource.Factory(this))
            .createMediaSource(mediaItem)

        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.setMediaSource(mediaSourceFactory)
        exoPlayer?.seekTo(playbackPosition)
        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.prepare()
    }

    private fun releasePlayer(){
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
        preparePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
        preparePlayer()
    }

}