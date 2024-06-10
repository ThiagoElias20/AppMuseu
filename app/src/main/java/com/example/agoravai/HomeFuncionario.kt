package com.example.agoravai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class HomeFuncionario : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mapaAdapter: MapaVisitanteAdapter
    private lateinit var viewPagerObras: ViewPager2
    private lateinit var viewPagerEventos: ViewPager2
    private val db = FirebaseFirestore.getInstance()
    private var mapaListener: ListenerRegistration? = null
    private var obrasListener: ListenerRegistration? = null
    private var eventsListener: ListenerRegistration? = null
    private var allMapas: List<Mapa> = emptyList()
    private var allObras: List<Obra> = emptyList()
    private var allEvents: List<Event> = emptyList()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_funcionario)

        val home = findViewById<Button>(R.id.home_func)
        val obras = findViewById<Button>(R.id.obras_func)
        val conta = findViewById<Button>(R.id.Conta)
        val crudMapa = findViewById<Button>(R.id.crudMapa)
        val crudObrasFuncionario = findViewById<Button>(R.id.crudObrasFuncionario)
        val crudEventosFuncionario = findViewById<Button>(R.id.crudEventosFuncionario)

        recyclerView = findViewById(R.id.recyclerViewFuncionario)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mapaAdapter = MapaVisitanteAdapter(emptyList())
        recyclerView.adapter = mapaAdapter

        viewPagerObras = findViewById(R.id.viewPagerObras)
        viewPagerEventos = findViewById(R.id.viewPagerEventos)

        home.setOnClickListener {
            val intent = Intent(this, HomeFuncionario::class.java)
            startActivity(intent)
        }
        obras.setOnClickListener {
            val intent = Intent(this, CrudObras::class.java)
            startActivity(intent)
        }
        conta.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        crudMapa.setOnClickListener {
            val intent = Intent(this, CrudMapa::class.java)
            startActivity(intent)
        }
        crudObrasFuncionario.setOnClickListener {
            val intent = Intent(this, CrudObras::class.java)
            startActivity(intent)
        }
        crudEventosFuncionario.setOnClickListener {
            val intent = Intent(this, CrudEventos::class.java)
            startActivity(intent)
        }

        fetchMapas()
        fetchObras()
        loadEventsInRealTime()
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

    private fun fetchObras() {
        obrasListener = db.collection("Obras").addSnapshotListener { snapshots, e ->
            if (e != null) {
                // Handle the error
                return@addSnapshotListener
            }

            if (snapshots != null) {
                allObras = snapshots.map { it.toObject(Obra::class.java).apply { id = it.id } }
                val obraSliderAdapter = ObraSliderAdapter(allObras)
                viewPagerObras.adapter = obraSliderAdapter
            }
        }
    }

    private fun loadEventsInRealTime() {
        eventsListener = db.collection("Eventos")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Erro ao carregar eventos", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                allEvents = snapshot?.map { document ->
                    Event(
                        document.id,
                        document.getString("nome") ?: "",
                        document.getString("descricao") ?: "",
                        document.getString("imagem") ?: ""
                    )
                } ?: emptyList()

                val eventSliderAdapter = EventSliderAdapter(allEvents)
                viewPagerEventos.adapter = eventSliderAdapter
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapaListener?.remove()
        obrasListener?.remove()
        eventsListener?.remove()
    }
}