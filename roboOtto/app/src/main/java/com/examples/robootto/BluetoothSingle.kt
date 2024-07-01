package com.examples.robootto

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.UUID

object BluetoothSingle {
    private lateinit var bluetoothSocket: BluetoothSocket
    private lateinit var BTS: BluetoothSocket
    fun initialize(nuevo: BluetoothSocket) {
        BTS = nuevo

    }


    fun GetSocket(): BluetoothSocket {
        return BTS;
    }

    fun SetSocket(nuevo: BluetoothSocket){
        BTS = nuevo
    }


    fun disconnect() {
        try {
            BTS.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}
