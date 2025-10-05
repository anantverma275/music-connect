package com.anantverma.musicplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anantverma.musicplayer.nsd.NsdHelper

class MainActivity : AppCompatActivity() {
    private lateinit var nsdHelper: NsdHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nsdHelper = NsdHelper(this)

        // Register this device
        nsdHelper.registerService()

        // Discover others
        nsdHelper.discoverServices()
    }

    override fun onDestroy() {
        super.onDestroy()
        nsdHelper.tearDown()
    }
}
