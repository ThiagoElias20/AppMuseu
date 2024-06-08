package com.example.agoravai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class obrasVisitante : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_obras_visitante)
        val home = findViewById<Button>(R.id.home)
        val obras = findViewById<Button>(R.id.obras)
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