package com.example.agoravai

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddEditObraActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etAno: EditText
    private lateinit var etAutor: EditText
    private lateinit var imgObra: ImageView
    private lateinit var btnSave: Button
    private lateinit var btnUpload: Button

    private val db = FirebaseFirestore.getInstance()
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_obra)

        etNome = findViewById(R.id.etNome)
        etAno = findViewById(R.id.etAno)
        etAutor = findViewById(R.id.etAutor)
        imgObra = findViewById(R.id.imgObra)
        btnSave = findViewById(R.id.btnSave)
        btnUpload = findViewById(R.id.btnUpload)

        btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSave.setOnClickListener {
            if (imageUrl != null) {
                addObra()
            } else {
                Toast.makeText(this, "Aguarde o upload da imagem", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            val inputStream = contentResolver.openInputStream(imageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imgObra.setImageBitmap(bitmap)

            uploadImageToStorage(imageUri!!)
        }
    }

    private fun uploadImageToStorage(imageUri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}")
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    Toast.makeText(this, "Imagem carregada com sucesso!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addObra() {
        val nome = etNome.text.toString()
        val ano = etAno.text.toString()
        val autor = etAutor.text.toString()
        val imagem = imageUrl

        if (nome.isEmpty() || ano.isEmpty() || autor.isEmpty() || imagem == null) {
            Toast.makeText(this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            return
        }

        val obra = hashMapOf(
            "nome" to nome,
            "ano" to ano,
            "autor" to autor,
            "imagem" to imagem
        )

        db.collection("Obras").add(obra)
            .addOnSuccessListener {
                Toast.makeText(this, "Obra adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao adicionar obra", Toast.LENGTH_SHORT).show()
            }
    }
}