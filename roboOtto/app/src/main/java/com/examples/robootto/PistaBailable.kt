package com.examples.robootto

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.content.Intent
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.media.MediaPlayer
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat

class PistaBailable : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val songMap = mapOf(
        R.id.baile1 to R.raw.dance1,
        R.id.baile2 to R.raw.dance2,
        R.id.baile3 to R.raw.dance3,
        R.id.baile4 to R.raw.dance4,
        R.id.baile5 to R.raw.dance5,
        R.id.baile6 to R.raw.dance6,
        R.id.baile7 to R.raw.dance6,
        R.id.baile8 to R.raw.dance6,
        R.id.baile9 to R.raw.dance6
    )
    private val danceNames = mapOf(
        R.id.baile1 to "Salsa",
        R.id.baile2 to "Tango",
        R.id.baile3 to "Hip-Hop",
        R.id.baile4 to "Pop",
        R.id.baile5 to "Cumbia",
        R.id.baile6 to "Breakdance",
        R.id.baile7 to "Rock",
        R.id.baile8 to "Jazz",
        R.id.baile9 to "Reggaeton"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pista_bailable)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val startButton = findViewById<ImageButton>(R.id.startButton)
        val stopButton = findViewById<ImageButton>(R.id.stopButton)
        val backButton = findViewById<Button>(R.id.volverButton)
        val selectedDanceLabel = findViewById<TextView>(R.id.selectedDanceLabel)

        backButton.setOnClickListener {
            mediaPlayer?.pause()
            mediaPlayer?.seekTo(0)
            val intent = Intent(this, MenuSelector::class.java)
            startActivity(intent)
        }

        startButton.setOnClickListener {
            mediaPlayer?.start()
            startButton.visibility = ImageButton.GONE
            stopButton.visibility = ImageButton.VISIBLE
        }

        stopButton.setOnClickListener {
            mediaPlayer?.pause()
            mediaPlayer?.seekTo(0)
            startButton.visibility = ImageButton.VISIBLE
            stopButton.visibility = ImageButton.GONE
        }

        val frameLayouts = listOf(
            findViewById<FrameLayout>(R.id.baile1),
            findViewById<FrameLayout>(R.id.baile2),
            findViewById<FrameLayout>(R.id.baile3),
            findViewById<FrameLayout>(R.id.baile4),
            findViewById<FrameLayout>(R.id.baile5),
            findViewById<FrameLayout>(R.id.baile6),
            findViewById<FrameLayout>(R.id.baile7),
            findViewById<FrameLayout>(R.id.baile8),
            findViewById<FrameLayout>(R.id.baile9)
        )

        for (frame in frameLayouts) {
            frame.setOnClickListener {
                // Quitar el fondo de todos los FrameLayout
                frameLayouts.forEach { it.background = null }
                // Establecer un borde alrededor del FrameLayout seleccionado
                frame.background = ContextCompat.getDrawable(this, R.drawable.border)

                // Parar la música actual si está reproduciéndose
                mediaPlayer?.stop()
                mediaPlayer?.release()

                // Iniciar la nueva música
                val songResId = songMap[frame.id]
                if (songResId != null) {
                    mediaPlayer = MediaPlayer.create(this, songResId)
                    mediaPlayer?.start()
                    startButton.visibility = ImageButton.GONE
                    stopButton.visibility = ImageButton.VISIBLE
                }

                // Actualizar el label con el nombre del baile seleccionado
                val danceName = danceNames[frame.id]
                selectedDanceLabel.text = danceName ?: "Seleccione un baile"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
