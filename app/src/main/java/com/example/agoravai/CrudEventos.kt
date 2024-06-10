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

class CrudEventos : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var etSearch: EditText
    private val db = FirebaseFirestore.getInstance()
    private var eventsListener: ListenerRegistration? = null
    private var allEvents: List<Event> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crud_eventos)

        recyclerView = findViewById(R.id.recyclerView)
        etSearch = findViewById(R.id.etSearch)
        recyclerView.layoutManager = LinearLayoutManager(this)
        eventAdapter = EventAdapter(
            onDeleteClick = { eventId -> deleteEvent(eventId) },
            onEditClick = { event -> showEditEventDialog(event) },
            onItemClick = { event -> showEventDetails(event) }
        )
        recyclerView.adapter = eventAdapter

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, AddEditEventActivity::class.java))
        }

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

    private fun deleteEvent(eventId: String) {
        db.collection("Eventos").document(eventId).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Evento excluído com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao excluir evento", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showEditEventDialog(event: Event?) {
        val dialogFragment = AddEditEventDialogFragment.newInstance(event)
        dialogFragment.show(supportFragmentManager, "AddEditEventDialogFragment")
    }

    private fun showEventDetails(event: Event) {
        val intent = Intent(this, EventDetailsActivity::class.java).apply {
            putExtra("event", event)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        eventsListener?.remove()
    }
}