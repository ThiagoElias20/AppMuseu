package com.example.agoravai

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ObraVisitanteActivity : AppCompatActivity() {

    private lateinit var obraVisitanteAdapter: ObraVisitanteAdapter
    private lateinit var etSearch: EditText
    private val db = FirebaseFirestore.getInstance()
    private var allObras: List<Obra> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obra_visitante)

        etSearch = findViewById(R.id.etSearch)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        obraVisitanteAdapter = ObraVisitanteAdapter { obra ->
            val intent = Intent(this, ObraDetailsActivity::class.java).apply {
                putExtra("obra", obra)
            }
            startActivity(intent)
        }
        recyclerView.adapter = obraVisitanteAdapter

        fetchObras()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun fetchObras() {
        db.collection("Obras").get().addOnSuccessListener { result ->
            allObras = result.map { document ->
                Obra(
                    id = document.id,
                    nome = document.getString("nome") ?: "",
                    ano = document.getString("ano") ?: "",
                    autor = document.getString("autor") ?: "",
                    imagem = document.getString("imagem") ?: ""
                )
            }
            obraVisitanteAdapter.submitList(allObras)
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Erro ao carregar obras", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filter(text: String) {
        val filteredList = if (text.isEmpty()) {
            allObras
        } else {
            allObras.filter { it.nome.contains(text, ignoreCase = true) }
        }
        obraVisitanteAdapter.submitList(filteredList)
    }
}