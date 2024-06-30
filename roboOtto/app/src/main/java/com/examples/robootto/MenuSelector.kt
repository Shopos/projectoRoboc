package com.examples.robootto

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuSelector : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_selector)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val option1 = findViewById<ImageView>(R.id.option1)
        val option2 = findViewById<ImageView>(R.id.option2)
        val option3 = findViewById<ImageView>(R.id.option3)

        val direccionBT = intent.getStringExtra("valor").toString()

        Toast.makeText(this,intent.getStringExtra("valor").toString(),Toast.LENGTH_SHORT).show()

        option1.setOnClickListener{
            val intent = Intent(this, PistaBailable::class.java)
            intent.putExtra("valor", direccionBT);
            startActivity(intent)
        }

        option2.setOnClickListener {

            startActivity(Intent(this, Building::class.java))
        }

        option3.setOnClickListener{
            val intent = Intent(this, ControlBot::class.java)
            intent.putExtra("valor", direccionBT);
            startActivity(intent)
        }

    }
}
