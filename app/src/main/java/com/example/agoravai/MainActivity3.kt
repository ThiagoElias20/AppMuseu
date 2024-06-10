package com.example.agoravai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MainActivity3 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mapaAdapter: MapaVisitanteAdapter
    private val db = FirebaseFirestore.getInstance()
    private var mapaListener: ListenerRegistration? = null
    private var allMapas: List<Mapa> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mapaAdapter = MapaVisitanteAdapter(emptyList())
        recyclerView.adapter = mapaAdapter

        val home = findViewById<Button>(R.id.home)
        val obras = findViewById<Button>(R.id.obras)
        val conta = findViewById<Button>(R.id.Conta)
        val evento = findViewById<ImageButton>(R.id.imageView)

        home.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
        obras.setOnClickListener {
            val intent = Intent(this, ObraVisitanteActivity::class.java)
            startActivity(intent)
        }
        conta.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        evento.setOnClickListener {
            val intent = Intent(this, CrudEventosVisitante::class.java)
            startActivity(intent)
        }

        fetchMapas()
    }

    private fun fetchMapas() {
        mapaListener = db.collection("Mapas").addSnapshotListener { snapshots, e ->
            if (e != null) {
                // Handle the error
                return@addSnapshotListener
            }

            if (snapshots != null) {
                allMapas = snapshots.map { it.toObject(Mapa::class.java).apply { id = it.id } }
                mapaAdapter.updateMapas(allMapas)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapaListener?.remove()
    }
}