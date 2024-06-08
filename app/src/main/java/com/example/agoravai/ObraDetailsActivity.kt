package com.example.agoravai

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ObraDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obra_details)

        val imgObra: ImageView = findViewById(R.id.imgObra)
        val tvNome: TextView = findViewById(R.id.tvNome)
        val tvAno: TextView = findViewById(R.id.tvAno)
        val tvAutor: TextView = findViewById(R.id.tvAutor)

        val obra = intent.getSerializableExtra("obra") as Obra

        tvNome.text = obra.nome
        tvAno.text = obra.ano
        tvAutor.text = obra.autor
        Glide.with(this).load(obra.imagem).into(imgObra)
    }
}
