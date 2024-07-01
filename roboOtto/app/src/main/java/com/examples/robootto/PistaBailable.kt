package com.examples.robootto

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.media.MediaPlayer
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class PistaBailable : AppCompatActivity() {

    lateinit var BTS: BluetoothSocket

    private var mediaPlayer: MediaPlayer? = null
    private val songMap = mapOf(
        R.id.baile1 to R.raw.salsa,
        R.id.baile2 to R.raw.tango,
        R.id.baile3 to R.raw.rap,
        R.id.baile4 to R.raw.pop,
        R.id.baile5 to R.raw.cumbia,
        R.id.baile6 to R.raw.breakdance,
        R.id.baile7 to R.raw.rock,
        R.id.baile8 to R.raw.jazz,
        R.id.baile9 to R.raw.reggaeton
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

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        val bta = bluetoothManager.adapter as BluetoothAdapter
        val btnEna =  Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                PedirPermisoVincular(intent.getStringExtra("valor").toString(), bta)
            } catch (e: Exception) {
                Toast.makeText(this@PistaBailable, "no se ha podido conectar", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@PistaBailable, BlueConfirm::class.java)
                startActivity(intent)

            }
        }

        val startButton = findViewById<ImageButton>(R.id.startButton)
        val stopButton = findViewById<ImageButton>(R.id.stopButton)
        val selectedDanceLabel = findViewById<TextView>(R.id.selectedDanceLabel)

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
        val lista = listOf('e','r','c','t','u','j','m','o','l') // comando para enviar a bailar

        for (frame in frameLayouts) {

            frame.setOnClickListener {
                frameLayouts.forEach { it.background = null }
                frame.background = ContextCompat.getDrawable(this, R.drawable.border)




                mediaPlayer?.stop()
                mediaPlayer?.release()

                val songResId = songMap[frame.id]
                if (songResId != null) {
                    mediaPlayer = MediaPlayer.create(this, songResId)
                    mediaPlayer?.start()
                    startButton.visibility = ImageButton.GONE
                    stopButton.visibility = ImageButton.VISIBLE
                }

                val danceName = danceNames[frame.id]
                selectedDanceLabel.text = danceName ?: "Seleccione un baile"

                Toast.makeText(this,lista.get(frameLayouts.indexOf(frame)).toString(),Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        BTS.outputStream.write((lista.get(frameLayouts.indexOf(frame)).toString()).toByteArray());
                    } catch (e: Exception) {
                        Toast.makeText(this@PistaBailable, "no se ha podido conectar", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@PistaBailable, BlueConfirm::class.java)
                        startActivity(intent)

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    private fun PedirPermisoVincular(DireccionesBT: String, bta: BluetoothAdapter) {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
            // permiso no aceptado
            requestBT()
        }else{
            //permiso aceptado
            if(!bta.bondedDevices.isEmpty()){

                val ui: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                //val BTS:BluetoothSocket

                val instalacion: BluetoothDevice = bta.getRemoteDevice(DireccionesBT)
                BTS =  instalacion.createRfcommSocketToServiceRecord(ui)
                BTS.connect()
                Toast.makeText(this,"se ha podido conectar", Toast.LENGTH_SHORT).show()


            }
        }
    }

    private fun requestBT() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_CONNECT)){
            // el usuario rechazo el bluetooth\
            Toast.makeText(this,"Permiso rechazado", Toast.LENGTH_SHORT).show()
        }else{
            // pedir permiso
            ActivityCompat.requestPermissions(this,
                arrayOf( Manifest.permission.BLUETOOTH_CONNECT),777)
        }
    }
}
