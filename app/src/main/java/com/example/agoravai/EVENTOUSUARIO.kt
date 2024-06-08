package com.example.agoravai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EVENTOUSUARIO : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eventousuario)
        val home = findViewById<Button>(R.id.home_func)
        val obras = findViewById<Button>(R.id.obras_func)
        val conta = findViewById<Button>(R.id.Conta)
        home.setOnClickListener {
            val intent = Intent(this,MainActivity3::class.java)
            startActivity(intent)
        }
        obras.setOnClickListener {
            val intent = Intent(this,obrasVisitante::class.java)
            startActivity(intent)
        }
        conta.setOnClickListener {
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }
    }
}