package com.examples.robootto

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.content.Intent
import android.widget.Button
import androidx.core.view.WindowInsetsCompat
import android.media.MediaPlayer


class PistaBailable : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pista_bailable)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //val startButton = findViewById<Button>(R.id.startButton)
        //val stopButton = findViewById<Button>(R.id.stopButton)

        val mediaPlayer = MediaPlayer.create(this, R.raw.music_file)

        //val backButton = findViewById<Button>(R.id.volverButton)
        /**
        backButton.setOnClickListener{
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            val intent = Intent(this, MenuSelector::class.java)
            startActivity(intent)
        }
        startButton.setOnClickListener {
            mediaPlayer.start()
            startButton.visibility = Button.GONE
            stopButton.visibility = Button.VISIBLE
        }

        stopButton.setOnClickListener {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            startButton.visibility = Button.VISIBLE
            stopButton.visibility = Button.GONE
        }

         **/
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}