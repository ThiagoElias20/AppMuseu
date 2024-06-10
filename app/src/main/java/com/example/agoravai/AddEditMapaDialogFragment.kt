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

class AddEditMapaDialogFragment : DialogFragment() {

    private lateinit var imgMapa: ImageView
    private lateinit var etDescricaoDoMapa: EditText
    private lateinit var btnSalvarMapa: Button

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var selectedImageUri: Uri? = null

    companion object {
        private const val ARG_MAPA = "mapa"
        private const val PICK_IMAGE_REQUEST = 1

        fun newInstance(mapa: Mapa?): AddEditMapaDialogFragment {
            val fragment = AddEditMapaDialogFragment()
            val args = Bundle().apply {
                putSerializable(ARG_MAPA, mapa)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_edit_mapa, container, false)

        imgMapa = view.findViewById(R.id.imgMapa)
        etDescricaoDoMapa = view.findViewById(R.id.etDescricaoDoMapa)
        btnSalvarMapa = view.findViewById(R.id.btnSalvarMapa)

        val mapa = arguments?.getSerializable(ARG_MAPA) as? Mapa

        mapa?.let {
            etDescricaoDoMapa.setText(it.descricaoDoMapa)
            Glide.with(this).load(it.imagemDoMapa).into(imgMapa)
        }

        btnSalvarMapa.setOnClickListener {
            saveMapa(mapa)
        }

        imgMapa.setOnClickListener {
            openImagePicker()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            imgMapa.setImageURI(selectedImageUri)
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun saveMapa(mapa: Mapa?) {
        val descricaoDoMapa = etDescricaoDoMapa.text.toString().trim()

        if (descricaoDoMapa.isEmpty()) {
            Toast.makeText(requireContext(), "Preencha a descrição do mapa", Toast.LENGTH_SHORT).show()
            return
        }

        val mapaId = mapa?.id ?: db.collection("Mapas").document().id

        val mapaData = hashMapOf(
            "descricaoDoMapa" to descricaoDoMapa
        )

        if (selectedImageUri != null) {
            val imageRef = storage.reference.child("mapas/${UUID.randomUUID()}")
            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        mapaData["imagemDoMapa"] = uri.toString()
                        saveMapaToFirestore(mapaId, mapaData)
                    }
                }
                .addOnFailureListener {
                    if (isAdded) {
                        Toast.makeText(requireContext(), "Erro ao salvar imagem", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            mapaData["imagemDoMapa"] = mapa?.imagemDoMapa ?: ""
            saveMapaToFirestore(mapaId, mapaData)
        }
    }

    private fun saveMapaToFirestore(mapaId: String, mapaData: Map<String, Any>) {
        db.collection("Mapas").document(mapaId).set(mapaData)
            .addOnSuccessListener {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Mapa salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
            .addOnFailureListener {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Erro ao salvar mapa", Toast.LENGTH_SHORT).show()
                }
            }
    }
}



