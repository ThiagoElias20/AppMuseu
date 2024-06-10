package com.example.agoravai

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class CrudMapa : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mapaAdapter: MapaAdapter
    private val db = FirebaseFirestore.getInstance()
    private var mapaListener: ListenerRegistration? = null
    private var allMapas: List<Mapa> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.crud_mapa)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mapaAdapter = MapaAdapter(emptyList()) { mapa, action ->
            when (action) {
                "edit" -> showEditMapaDialog(mapa)
                "delete" -> deleteMapa(mapa)
            }
        }

        recyclerView.adapter = mapaAdapter

        val fabAddMapa: FloatingActionButton = findViewById(R.id.fabAddMapa)
        fabAddMapa.setOnClickListener {
            val intent = Intent(this, AddEditMapaActivity::class.java)
            startActivity(intent)
        }

        fetchMapas()
    }

    private fun fetchMapas() {
        mapaListener = db.collection("Mapas").addSnapshotListener { snapshots, e ->
            if (e != null) {
                Toast.makeText(this, "Erro ao carregar mapas", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (snapshots != null) {
                allMapas = snapshots.map { it.toObject(Mapa::class.java).apply { id = it.id } }
                mapaAdapter.updateMapas(allMapas)
            }
        }
    }

    private fun showEditMapaDialog(mapa: Mapa?) {
        val dialogFragment = AddEditMapaDialogFragment.newInstance(mapa)
        dialogFragment.show(supportFragmentManager, "AddEditEventDialogFragment")
    }


    private fun deleteMapa(mapa: Mapa) {
        db.collection("Mapas").document(mapa.id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Mapa deletado com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao deletar mapa", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapaListener?.remove()
    }
}
