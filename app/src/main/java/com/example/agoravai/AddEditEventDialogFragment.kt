package com.example.agoravai

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddEditEventDialogFragment : DialogFragment() {

    private lateinit var imgEvent: ImageView
    private lateinit var etNome: EditText
    private lateinit var etDescricao: EditText
    private lateinit var btnSalvar: Button

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var selectedImageUri: Uri? = null

    companion object {
        private const val ARG_EVENT = "event"
        private const val PICK_IMAGE_REQUEST = 1

        fun newInstance(event: Event?): AddEditEventDialogFragment {
            val fragment = AddEditEventDialogFragment()
            val args = Bundle().apply {
                putSerializable(ARG_EVENT, event)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_edit_event, container, false)

        imgEvent = view.findViewById(R.id.imgEvent)
        etNome = view.findViewById(R.id.etNome)
        etDescricao = view.findViewById(R.id.etDescricao)
        btnSalvar = view.findViewById(R.id.btnSalvar)

        val event = arguments?.getSerializable(ARG_EVENT) as? Event

        event?.let {
            etNome.setText(it.nome)
            etDescricao.setText(it.descricao)
            Glide.with(this).load(it.imagem).into(imgEvent)
        }

        btnSalvar.setOnClickListener {
            saveEvent(event)
        }

        imgEvent.setOnClickListener {
            openImagePicker()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            imgEvent.setImageURI(selectedImageUri)
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun saveEvent(event: Event?) {
        val nome = etNome.text.toString().trim()
        val descricao = etDescricao.text.toString().trim()

        if (nome.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(requireContext(), "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            return
        }

        val eventId = event?.id ?: db.collection("Eventos").document().id

        val eventData = mutableMapOf(
            "nome" to nome,
            "descricao" to descricao
        )

        if (selectedImageUri != null) {
            val imageRef = storage.reference.child("images/${UUID.randomUUID()}")
            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        eventData["imagem"] = uri.toString()
                        saveEventToFirestore(eventId, eventData)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Erro ao salvar imagem", Toast.LENGTH_SHORT).show()
                }
        } else {
            eventData["imagem"] = event?.imagem ?: ""
            saveEventToFirestore(eventId, eventData)
        }
    }

    private fun saveEventToFirestore(eventId: String, eventData: Map<String, Any>) {
        db.collection("Eventos").document(eventId).set(eventData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Evento salvo com sucesso!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Erro ao salvar evento", Toast.LENGTH_SHORT).show()
            }
    }
}

