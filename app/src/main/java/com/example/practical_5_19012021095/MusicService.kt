package com.example.practical_5_19012021095

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.practical_5_19012021095.R

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private val audioFileArray = intArrayOf(R.raw.doraemonhindi, R.raw.kiteretsu, R.raw.shinchan)
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val playingIndex = intent?.getIntExtra("playingIndex", 0) ?: 0
        if (!this::mediaPlayer.isInitialized) {
            mediaPlayer = MediaPlayer.create(this, audioFileArray[playingIndex])
        }
        if (intent != null) {
            val data: String? = intent.getStringExtra("service")
            if (data == "play/pause") {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                } else {
                    mediaPlayer.pause()
                }
            } else if (data == "next/prev") {
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer = MediaPlayer.create(this, audioFileArray[playingIndex])
                mediaPlayer.start()
            }
        } else {
            mediaPlayer.start()
        }
        return START_STICKY
    }
    override fun onDestroy() {
        mediaPlayer.stop()
        super.onDestroy()
    }
}