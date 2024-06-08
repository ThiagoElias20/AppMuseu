package com.example.agoravai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        val visitanteBtn = findViewById<Button>(R.id.visitante_btn)
        val funcionarioBtn = findViewById<Button>(R.id.funcionario_btn)
        visitanteBtn.setOnClickListener {
            val intent = Intent(this,MainActivity3::class.java)
            startActivity(intent)
        }
        funcionarioBtn.setOnClickListener {
            val intent = Intent(this,loginfuncionario::class.java)
            startActivity(intent)
        }

    }
}