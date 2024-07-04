package com.examples.robootto

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class BlueConfirm : AppCompatActivity() {


    lateinit var BTS: BluetoothSocket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_blue_confirm)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val startConnection = findViewById<Button>(R.id.startBlue)

        val listaNombreBT = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1)
        val listaDireccionesBT = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1)

        val listaSpinner = findViewById<Spinner>(R.id.spinner)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bta = bluetoothManager.adapter as BluetoothAdapter
        val btnEna =  Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

        startConnection.isEnabled = true
        PedirPermiso(listaNombreBT,listaDireccionesBT,listaSpinner,bta)

        startConnection.setOnClickListener{
            startConnection.isEnabled = false
            CoroutineScope(Dispatchers.Main).launch {
                try {

                    PedirPermisoVincular(listaDireccionesBT, listaSpinner, bta)
                    val intent = Intent(this@BlueConfirm,MenuSelector::class.java)
                    intent.putExtra("valor", listaDireccionesBT.getItem(listaSpinner.selectedItemPosition));
                    Toast.makeText(this@BlueConfirm,"se ha podido conectar", Toast.LENGTH_SHORT).show()
                    startActivity(intent)

                } catch (e: Exception) {
                    startConnection.isEnabled = true
                    Toast.makeText(this@BlueConfirm, "no se ha podido conectar", Toast.LENGTH_SHORT).show()

                }
            }

        }
    }

    private fun PedirPermiso(listaNombreBT: ArrayAdapter<String>, listaDireccionesBT: ArrayAdapter<String>, ListaBT: Spinner, bta: BluetoothAdapter) {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
            // permiso no aceptado
            requestBT()
        }else{
            //permiso aceptado
            if(!bta.bondedDevices.isEmpty()){
                bta.bondedDevices.forEach {
                    Toast.makeText(this,it.name + " | "+it.address, Toast.LENGTH_SHORT).show()
                    listaNombreBT.add(it.name)
                    listaDireccionesBT.add((it.address))

                }
                ListaBT.adapter = listaNombreBT

            }
        }
    }

    private fun PedirPermisoVincular(listaDireccionesBT: ArrayAdapter<String>, ListaBT: Spinner, bta: BluetoothAdapter) {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
            // permiso no aceptado
            requestBT()
        }else{
            //permiso aceptado
            if(!bta.bondedDevices.isEmpty()){

                val ui: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                //val BTS:BluetoothSocket

                val instalacion: BluetoothDevice = bta.getRemoteDevice(listaDireccionesBT.getItem(ListaBT.selectedItemPosition))



                BTS =  instalacion.createRfcommSocketToServiceRecord(ui)
                BTS.connect()
                Toast.makeText(this,"paso", Toast.LENGTH_SHORT).show()
                BluetoothSingle.initialize(BTS)

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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 777){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permiso aceptado", Toast.LENGTH_SHORT).show()

            }else{
                // no se acepto
            }
        }
    }

}