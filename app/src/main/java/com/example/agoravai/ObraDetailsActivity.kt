package com.example.agoravai

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.util.*

class ObraDetailsActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

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

        // Inicializa o TextToSpeech
        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Define o idioma para o TTS
            val localeBR = Locale("pt", "BR")
            val result = tts.setLanguage(localeBR)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Idioma não suportado ou dados ausentes
                println("Idioma não suportado")
            } else {
                // TTS pronto para uso, inicia a fala
                speakObraDetails()
            }
        } else {
            println("Falha na inicialização do TTS")
        }
    }

    private fun speakObraDetails() {
        val obra = intent.getSerializableExtra("obra") as Obra
        val textToSpeak = "Nome da obra: ${obra.nome}, Ano: ${obra.ano}, Autor: ${obra.autor}"
        tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        // Libera os recursos do TTS quando a atividade for destruída
        if (tts != null) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}