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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class CrudObras : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var obraAdapter: ObraAdapter
    private lateinit var etSearch: EditText
    private val db = FirebaseFirestore.getInstance()
    private var obrasListener: ListenerRegistration? = null
    private var allObras: List<Obra> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crud_obras)

        recyclerView = findViewById(R.id.recyclerView)
        etSearch = findViewById(R.id.etSearch)
        recyclerView.layoutManager = LinearLayoutManager(this)
        obraAdapter = ObraAdapter(
            onDeleteClick = { obraId -> deleteObra(obraId) },
            onEditClick = { obra -> showEditObraDialog(obra) }
        )
        recyclerView.adapter = obraAdapter

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, AddEditObraActivity::class.java))
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterObras(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        loadObrasInRealTime()
    }

    private fun loadObrasInRealTime() {
        obrasListener = db.collection("Obras")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Erro ao carregar obras", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                allObras = snapshot?.map { document ->
                    Obra(
                        document.id,
                        document.getString("nome") ?: "",
                        document.getString("ano") ?: "",
                        document.getString("autor") ?: "",
                        document.getString("imagem") ?: ""
                    )
                } ?: emptyList()

                obraAdapter.submitList(allObras)
            }
    }

    private fun filterObras(query: String) {
        val filteredList = if (query.isEmpty()) {
            allObras
        } else {
            allObras.filter { it.nome.contains(query, ignoreCase = true) }
        }
        obraAdapter.submitList(filteredList)
    }

    private fun deleteObra(obraId: String) {
        db.collection("Obras").document(obraId).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Obra excluída com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao excluir obra", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showEditObraDialog(obra: Obra) {
        val dialog = AddEditObraDialogFragment.newInstance(obra)
        dialog.show(supportFragmentManager, "AddEditObraDialogFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        obrasListener?.remove()
    }
}