package com.examples.robootto

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent

class ControlBot : AppCompatActivity() {
    private lateinit var joystickBall: ImageView
    private lateinit var joystickLayout: RelativeLayout
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_control_bot)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val optionBack = findViewById<Button>(R.id.buttonBackControl)
        optionBack.setOnClickListener {
            val intent = Intent(this, MenuSelector::class.java)
            startActivity(intent)
        }

        joystickBall = findViewById(R.id.joystickBall)

        joystickLayout = findViewById(R.id.joystickLayout)

        joystickLayout.post {
            centerX = (joystickLayout.width / 2).toFloat()
            centerY = (joystickLayout.height / 2).toFloat()
            joystickBall.x = centerX - joystickBall.width / 2
            joystickBall.y = centerY - joystickBall.height / 2
        }

        joystickBall.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    // Obtener las coordenadas del evento
                    val x = event.x
                    val y = event.y

                    val xIn = x.coerceIn(joystickBall.width / 2f, joystickLayout.width - joystickBall.width / 2f)
                    val yIn = y.coerceIn(joystickBall.height / 2f, joystickLayout.height - joystickBall.height / 2f)
                    // Actualizar la posición de la imagen del joystick de acuerdo con el evento
                    joystickBall.x = xIn - joystickBall.width / 2
                    joystickBall.y = yIn - joystickBall.height / 2

                    // Aquí puedes agregar la lógica para procesar el movimiento del joystick
                }
                MotionEvent.ACTION_UP ->{
                    joystickBall.x = centerX - joystickBall.width / 2
                    joystickBall.y = centerY - joystickBall.height / 2
                }
            }
            true
        }


    }
}
