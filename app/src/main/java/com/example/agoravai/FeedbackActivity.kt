package com.example.agoravai

import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FeedbackActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var enviarButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        ratingBar = findViewById(R.id.ratingBar)
        enviarButton = findViewById(R.id.enviarButton)

        enviarButton.setOnClickListener {
            val nota = ratingBar.rating.toInt()
            enviarFeedback(nota)
        }
    }

    private fun enviarFeedback(nota: Int) {
        db.collection("feedback")
            .document("feedback_principal")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val notaAtual = document.getLong("notaFeedback") ?: 0
                    val quantidadeAtual = document.getLong("quantidadeNotas") ?: 0

                    val novaNota = notaAtual + nota
                    val novaQuantidade = quantidadeAtual + 1

                    val novoFeedback = hashMapOf(
                        "notaFeedback" to novaNota,
                        "quantidadeNotas" to novaQuantidade
                    )

                    db.collection("feedback")
                        .document("feedback_principal")
                        .set(novoFeedback)
                        .addOnSuccessListener {
                            // Sucesso ao enviar feedback
                            finish()
                        }
                        .addOnFailureListener {
                            // Falha ao enviar feedback
                        }
                }
            }
    }
}