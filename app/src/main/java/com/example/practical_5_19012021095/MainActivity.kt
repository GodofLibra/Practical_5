package com.example.practical_5_19012021095

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.practical_5_19012021095.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var flag = false // false = play and true = pause
    private lateinit var btnPlay: FloatingActionButton
    private lateinit var imageSong: ImageView
    private lateinit var songName: TextView
    private lateinit var songView: TextView
    private lateinit var imageFileArray: IntArray
    private lateinit var songNameArray: ArrayList<String>
    private lateinit var songViewArray: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStatusBarTransparent()
        imageSong = findViewById(R.id.iv_song)
        songName = findViewById(R.id.song_name)
        songView = findViewById(R.id.song_views)

        var playingIndex = 0

        imageFileArray = intArrayOf(R.drawable.doremon, R.drawable.kitretsu, R.drawable.shinchan)
        songNameArray = arrayListOf("Doremon", "Kitretsu", "Shinchan")
        songViewArray = arrayListOf("358M", "400M", "665M")

        btnPlay = findViewById(R.id.btn_play)
        btnPlay.setOnClickListener {
            Intent(this, MusicService::class.java).apply{
                putExtra("service", "play/pause")
                putExtra("playingIndex", playingIndex)
                startService(this)
            }
            flag = if (flag) {
                setPlay()
                false
            } else {
                setPause()
                true
            }
        }
        val btnStop = findViewById<FloatingActionButton>(R.id.btn_stop)
        btnStop.setOnClickListener {
            setPlay()
            flag = false
            Intent(this, MusicService::class.java).apply {
                stopService(this)
            }
        }
        val btnNext = findViewById<FloatingActionButton>(R.id.btn_next)
        btnNext.setOnClickListener {
            setPause()
            flag = true
            playingIndex++
            if (playingIndex >= imageFileArray.size) {
                playingIndex = 0
            }
            Intent(this, MusicService::class.java).apply {
                putExtra("service", "next/prev")
                putExtra("playingIndex", playingIndex)
                startService(this)
            }
            updateUI(playingIndex)
        }
        val btnPrev = findViewById<FloatingActionButton>(R.id.btn_previous)
        btnPrev.setOnClickListener {
            setPause()
            flag = true
            playingIndex--
            if (playingIndex < 0) {
                playingIndex = imageFileArray.size - 1
            }
            Intent(this, MusicService::class.java).apply {
                putExtra("service", "next/prev")
                putExtra("playingIndex", playingIndex)
                startService(this)
            }
            updateUI(playingIndex)
        }
    }
    private fun updateUI(playingIndex: Int) {
        imageSong.setImageDrawable(
            ResourcesCompat.getDrawable(resources, imageFileArray[playingIndex], null)
        )
        songName.text = songNameArray[playingIndex]
        songView.text = songViewArray[playingIndex]
    }
    private fun setPause() {
        btnPlay.setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_pause_24, null)
        )
    }
    private fun setPlay() {
        btnPlay.setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_play_arrow_24, null)
        )
    }
    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT in 19..20){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,true)
            }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }
    private fun setWindowFlag(bits: Int, on: Boolean) {
        val winParameters = window.attributes
        if (on) {
            winParameters.flags = winParameters.flags or bits
        } else {
            winParameters.flags = winParameters.flags and bits.inv()
        }
        window.attributes = winParameters
    }
}