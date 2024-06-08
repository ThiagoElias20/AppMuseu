package com.example.agoravai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeFuncionario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_funcionario)
        val home = findViewById<Button>(R.id.home_func)
        val obras = findViewById<Button>(R.id.obras_func)
        val conta = findViewById<Button>(R.id.Conta)
        val evento = findViewById<ImageButton>(R.id.imageView)
        home.setOnClickListener {
            val intent = Intent(this,HomeFuncionario::class.java)
            startActivity(intent)
        }
        obras.setOnClickListener {
            val intent = Intent(this,CrudObras::class.java)
            startActivity(intent)
        }
        conta.setOnClickListener {
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }
        evento.setOnClickListener {
            val intent = Intent(this,EVENTOFUNCIONARIO::class.java)
            startActivity(intent)
        }
        }
}