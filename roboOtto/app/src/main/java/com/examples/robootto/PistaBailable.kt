package com.examples.robootto

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.content.Intent
import android.widget.Button
import androidx.core.view.WindowInsetsCompat
import android.media.MediaPlayer
import android.widget.FrameLayout
import android.widget.ImageView


class PistaBailable : AppCompatActivity() {

    private var selectedFrame: FrameLayout? = null
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

        val baile1 = findViewById<FrameLayout>(R.id.baile1)
        val baile2 = findViewById<FrameLayout>(R.id.baile2)
        val baile3 = findViewById<FrameLayout>(R.id.baile3)



        baile1.setOnClickListener{onImageClick(baile1)}
        baile2.setOnClickListener{onImageClick(baile2)}
        baile3.setOnClickListener{onImageClick(baile3)}




    }

    private fun onImageClick(frameLayout: FrameLayout) {
        if (frameLayout.id == selectedFrame?.id) {
            frameLayout.setBackgroundResource(android.R.color.transparent)
            selectedFrame = null
        }
        else {
            selectedFrame?.setBackgroundResource(android.R.color.transparent)
            frameLayout.setBackgroundResource(R.drawable.border)
            selectedFrame = frameLayout
        }
    }

}