package com.example.agoravai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class edicaoObra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edicao_obra)
        val home = findViewById<Button>(R.id.home_func)
        val obras = findViewById<Button>(R.id.obras_func)
        val conta = findViewById<Button>(R.id.Conta)
        val cancelar = findViewById<Button>(R.id.cancelar_edicao_obra_btn)
        val confirmar = findViewById<Button>(R.id.confirmar_edicao_obra_btn)
        home.setOnClickListener {
            val intent = Intent(this,HomeFuncionario::class.java)
            startActivity(intent)
        }
        obras.setOnClickListener {
            val intent = Intent(this,obrasEspaco::class.java)
            startActivity(intent)
        }
        conta.setOnClickListener {
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }
        cancelar.setOnClickListener {
            val intent = Intent(this,obrasEspaco::class.java)
            startActivity(intent)
        }
        confirmar.setOnClickListener {
            val intent = Intent(this,obrasEspaco::class.java)
            startActivity(intent)
        }
    }
}