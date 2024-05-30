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
import android.util.Log
import kotlin.math.atan2

class ControlBot : AppCompatActivity() {
    private lateinit var joystickBall: ImageView
    private lateinit var joystickLayout: RelativeLayout
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var radius: Float = 0f

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



        joystickBall = findViewById(R.id.joystickBall)
        joystickLayout = findViewById(R.id.joystickLayout)

        joystickLayout.post {
            centerX = (joystickLayout.width / 2).toFloat()
            centerY = (joystickLayout.height / 2).toFloat()
            radius = (joystickLayout.width / 2).toFloat() - (joystickBall.width / 2).toFloat()
            joystickBall.x = centerX - joystickBall.width / 2
            joystickBall.y = centerY - joystickBall.height / 2
        }

        joystickBall.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    val x = event.rawX - joystickLayout.left
                    val y = event.rawY - joystickLayout.top

                    val dx = x - centerX
                    val dy = y - centerY
                    val distance = Math.sqrt((dx * dx + dy * dy).toDouble())

                    if (distance <= radius) {
                        joystickBall.x = x - joystickBall.width / 2
                        joystickBall.y = y - joystickBall.height / 2
                    } else {
                        val ratio = radius / distance
                        val constrainedX = centerX + dx * ratio
                        val constrainedY = centerY + dy * ratio
                        joystickBall.x = constrainedX.toFloat() - joystickBall.width / 2
                        joystickBall.y = constrainedY.toFloat() - joystickBall.height / 2
                    }

                    val relativeX = (joystickBall.x  + joystickBall.width / 2) / radius
                    val relativeY = (joystickBall.y  + joystickBall.height / 2) / radius
                    var angle = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble()))

                    if (angle < 0) {
                        angle += 360
                    }

                    val magnitude = (distance / radius).coerceIn(0.0, 1.0)

                    val direction: String

                    if (angle >= 45 && angle <= 135) {
                        direction = "Abajo"
                    }
                    else if (angle > 135 && angle <= 225){
                        direction = "Izquierda"
                    }
                    else if (angle > 225 && angle < 315){
                        direction = "Arriba"
                    }
                    else{
                        direction = "Derecha"
                    }

                    Log.d("Joystick", "X: $relativeX, Y: $relativeY, Angle: $angle, Magnitude: $magnitude")
                    Log.d("Direction", direction)

                }
                MotionEvent.ACTION_UP -> {

                    joystickBall.x = centerX - joystickBall.width / 2
                    joystickBall.y = centerY - joystickBall.height / 2


                    Log.d("Joystick", "Joystick reset to center")
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
