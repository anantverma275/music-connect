package com.anantverma.musicplayer

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar

class MusicPlayer(private val context: Context, private val seekBar: SeekBar) {

    private var mediaPlayer: MediaPlayer? = null
    private var handler = Handler(Looper.getMainLooper())

    fun play(url: String, onPrepared: (() -> Unit)? = null, onComplete: (() -> Unit)? = null) {
        stop() // release any existing playback
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                seekBar.max = it.duration
                it.start()
                updateSeekBar()
                onPrepared?.invoke()
            }
            setOnCompletionListener {
                onComplete?.invoke()
            }
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
        updateSeekBar()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        seekBar.progress = 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    private fun updateSeekBar() {
        mediaPlayer?.let { mp ->
            seekBar.progress = mp.currentPosition
            if (mp.isPlaying) {
                handler.postDelayed({ updateSeekBar() }, 500)
            }
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
