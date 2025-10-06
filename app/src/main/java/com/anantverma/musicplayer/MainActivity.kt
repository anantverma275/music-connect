package com.anantverma.musicplayer

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anantverma.musicplayer.nsd.NsdHelper

class MainActivity : AppCompatActivity() {
    private lateinit var nsdHelper: NsdHelper

    private lateinit var songTitle: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var btnPlay: Button
    private lateinit var btnPause: Button
    private lateinit var btnStop: Button
    private lateinit var btnNext: Button
    private lateinit var btnPrev: Button

    private lateinit var musicPlayer: MusicPlayer

    // Playlist of public domain songs
    private val playlist = listOf(
        Pair("SoundHelix Song 1", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
        Pair("SoundHelix Song 2", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"),
        Pair("SoundHelix Song 3", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3")
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nsdHelper = NsdHelper(this)

        // Register this device
        nsdHelper.registerService()

        // Discover others
        nsdHelper.discoverServices()
        setContentView(R.layout.activity_main)

        songTitle = findViewById(R.id.songTitle)
        seekBar = findViewById(R.id.seekBar)
        btnPlay = findViewById(R.id.btnPlay)
        btnPause = findViewById(R.id.btnPause)
        btnStop = findViewById(R.id.btnStop)

        // Add Next/Prev in your XML (if not, replace with two more buttons)
        btnNext = Button(this).apply { text = "Next" }
        btnPrev = Button(this).apply { text = "Prev" }
        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)


        musicPlayer = MusicPlayer(this, seekBar)

        btnPlay.setOnClickListener { playCurrentSong() }
        btnPause.setOnClickListener { musicPlayer.pause() }
        btnStop.setOnClickListener { musicPlayer.stop() }

        btnNext.setOnClickListener {
            currentIndex = (currentIndex + 1) % playlist.size
            playCurrentSong()
        }

        btnPrev.setOnClickListener {
            currentIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
            playCurrentSong()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) musicPlayer.seekTo(progress)
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }

    private fun playCurrentSong() {
        val (title, url) = playlist[currentIndex]
        songTitle.text = title
        musicPlayer.play(url, onComplete = { nextSong() })
    }

    private fun nextSong() {
        currentIndex = (currentIndex + 1) % playlist.size
        playCurrentSong()
    }
    override fun onDestroy() {
        super.onDestroy()
        nsdHelper.tearDown()
        musicPlayer.release()
    }
}
