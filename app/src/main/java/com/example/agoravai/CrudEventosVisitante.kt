package com.example.agoravai

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class CrudEventosVisitante : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapterVisitante
    private lateinit var etSearch: EditText
    private val db = FirebaseFirestore.getInstance()
    private var eventsListener: ListenerRegistration? = null
    private var allEvents: List<Event> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crud_eventos_visitante)

        recyclerView = findViewById(R.id.recyclerView)
        etSearch = findViewById(R.id.etSearch)
        recyclerView.layoutManager = LinearLayoutManager(this)
        eventAdapter = EventAdapterVisitante { event ->
            val intent = Intent(this, EventDetailsActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
        recyclerView.adapter = eventAdapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterEvents(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        loadEventsInRealTime()
    }

    private fun loadEventsInRealTime() {
        eventsListener = db.collection("Eventos")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
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

                eventAdapter.submitList(allEvents)
            }
    }

    private fun filterEvents(query: String) {
        val filteredList = if (query.isEmpty()) {
            allEvents
        } else {
            allEvents.filter { it.nome.contains(query, ignoreCase = true) }
        }
        eventAdapter.submitList(filteredList)
    }

    override fun onDestroy() {
        super.onDestroy()
        eventsListener?.remove()
    }
}



