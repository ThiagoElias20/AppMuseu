package com.example.agoravai

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var imgEvent: ImageView
    private lateinit var tvNome: TextView
    private lateinit var tvDescricao: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        imgEvent = findViewById(R.id.imgEvent)
        tvNome = findViewById(R.id.tvNome)
        tvDescricao = findViewById(R.id.tvDescricao)

        val event = intent.getSerializableExtra("event") as Event

        tvNome.text = event.nome
        tvDescricao.text = event.descricao
        Glide.with(this).load(event.imagem).into(imgEvent)
    }
}