package com.examples.robootto

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
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
import android.content.pm.PackageManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.atan2

class ControlBot : AppCompatActivity() {
    private lateinit var joystickBall: ImageView
    private lateinit var joystickLayout: RelativeLayout
    private lateinit var directionText: TextView

    lateinit var BTS: BluetoothSocket

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

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val bta = bluetoothManager.adapter as BluetoothAdapter
        val btnEna =  Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)


        CoroutineScope(Dispatchers.Main).launch {
            try {
               // PedirPermisoVincular(intent.getStringExtra("valor").toString(), bta)
            } catch (e: Exception) {
                Toast.makeText(this@ControlBot, "no se ha podido conectar", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ControlBot, BlueConfirm::class.java)
                startActivity(intent)

            }
        }

        joystickBall = findViewById(R.id.joystickBall)
        joystickLayout = findViewById(R.id.joystickLayout)
        directionText = findViewById(R.id.textView12)
        directionText.text = ""

        joystickLayout.post {
            centerX = (joystickLayout.width / 2).toFloat()
            centerY = (joystickLayout.height / 2).toFloat()
            radius = (joystickLayout.width / 2).toFloat() - (joystickBall.width / 2).toFloat()
            joystickBall.x = centerX - joystickBall.width / 2
            joystickBall.y = centerY - joystickBall.height / 2
        }
        var movimientoactual = -1
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
                        if(movimientoactual != 0) {
                            movimientoactual = 0

                            CoroutineScope(Dispatchers.Main).launch {
                                try {
                                    BluetoothSingle.GetSocket().outputStream.write(("s").toByteArray());
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@ControlBot,
                                        "no se ha podido conectar",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@ControlBot, BlueConfirm::class.java)
                                    startActivity(intent)

                                }
                            }
                        }
                    }
                    else if (angle > 135 && angle <= 225){
                        direction = "Izquierda"
                        if(movimientoactual != 1) {
                            movimientoactual = 1
                            CoroutineScope(Dispatchers.Main).launch {
                                try {
                                    BluetoothSingle.GetSocket().outputStream.write(("a").toByteArray());
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@ControlBot,
                                        "no se ha podido conectar",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@ControlBot, BlueConfirm::class.java)
                                    startActivity(intent)

                                }
                            }
                        }
                    }
                    else if (angle > 225 && angle < 315){

                        direction = "Arriba"
                        if(movimientoactual != 2) {
                            movimientoactual = 2

                            CoroutineScope(Dispatchers.Main).launch {
                                try {
                                    BluetoothSingle.GetSocket().outputStream.write(("w").toByteArray());
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@ControlBot,
                                        "no se ha podido conectar",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@ControlBot, BlueConfirm::class.java)
                                    startActivity(intent)

                                }
                            }
                        }
                    }
                    else{
                        direction = "Derecha"
                        if(movimientoactual != 3) {
                            movimientoactual = 3

                            CoroutineScope(Dispatchers.Main).launch {
                                try {
                                    BluetoothSingle.GetSocket().outputStream.write(("d").toByteArray());
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@ControlBot,
                                        "no se ha podido conectar",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@ControlBot, BlueConfirm::class.java)
                                    startActivity(intent)

                                }
                            }
                        }
                    }
                    if(magnitude == 0.0){
                        if(movimientoactual == 4){
                            movimientoactual = 4
                            CoroutineScope(Dispatchers.Main).launch {
                                try {
                                    BluetoothSingle.GetSocket().outputStream.write(("q").toByteArray());
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@ControlBot,
                                        "no se ha podido conectar",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this@ControlBot, BlueConfirm::class.java)
                                    startActivity(intent)

                                }
                            }
                        }
                    }

                    directionText.text = direction

                    Log.d("Joystick", "X: $relativeX, Y: $relativeY, Angle: $angle, Magnitude: $magnitude")
                    Log.d("Direction", direction)

                }
                MotionEvent.ACTION_UP -> {

                    joystickBall.x = centerX - joystickBall.width / 2
                    joystickBall.y = centerY - joystickBall.height / 2


                    Log.d("Joystick", "Joystick reset to center")
                    directionText.text = ""
                }
            }
            true
        }
    }
}
