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

class AddEditObraDialogFragment : DialogFragment() {

    private lateinit var imgObra: ImageView
    private lateinit var etNome: EditText
    private lateinit var etAno: EditText
    private lateinit var etAutor: EditText
    private lateinit var btnSalvar: Button

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var selectedImageUri: Uri? = null

    companion object {
        private const val ARG_OBRA = "obra"
        private const val PICK_IMAGE_REQUEST = 1

        fun newInstance(obra: Obra?): AddEditObraDialogFragment {
            val fragment = AddEditObraDialogFragment()
            val args = Bundle().apply {
                putSerializable(ARG_OBRA, obra)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_edit_obra, container, false)

        imgObra = view.findViewById(R.id.imgObra)
        etNome = view.findViewById(R.id.etNome)
        etAno = view.findViewById(R.id.etAno)
        etAutor = view.findViewById(R.id.etAutor)
        btnSalvar = view.findViewById(R.id.btnSalvar)

        val obra = arguments?.getSerializable(ARG_OBRA) as? Obra

        obra?.let {
            etNome.setText(it.nome)
            etAno.setText(it.ano)
            etAutor.setText(it.autor)
            Glide.with(this).load(it.imagem).into(imgObra)
        }

        btnSalvar.setOnClickListener {
            saveObra(obra)
        }

        imgObra.setOnClickListener {
            openImagePicker()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            imgObra.setImageURI(selectedImageUri)
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun saveObra(obra: Obra?) {
        val nome = etNome.text.toString().trim()
        val ano = etAno.text.toString().trim()
        val autor = etAutor.text.toString().trim()

        if (nome.isEmpty() || ano.isEmpty() || autor.isEmpty()) {
            Toast.makeText(requireContext(), "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            return
        }

        val obraId = obra?.id ?: db.collection("Obras").document().id

        val obraData = mutableMapOf(
            "nome" to nome,
            "ano" to ano,
            "autor" to autor
        )

        if (selectedImageUri != null) {
            val imageRef = storage.reference.child("images/${UUID.randomUUID()}")
            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        obraData["imagem"] = uri.toString()
                        saveObraToFirestore(obraId, obraData)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Erro ao salvar imagem", Toast.LENGTH_SHORT).show()
                }
        } else {
            obraData["imagem"] = obra?.imagem ?: ""
            saveObraToFirestore(obraId, obraData)
        }
    }

    private fun saveObraToFirestore(obraId: String, obraData: Map<String, Any>) {
        db.collection("Obras").document(obraId).set(obraData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Obra salva com sucesso!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Erro ao salvar obra", Toast.LENGTH_SHORT).show()
            }
    }
}




