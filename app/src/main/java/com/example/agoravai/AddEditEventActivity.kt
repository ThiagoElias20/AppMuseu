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
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddEditEventActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etDescricao: EditText
    private lateinit var imgEvent: ImageView
    private lateinit var btnSave: Button
    private lateinit var btnUpload: Button

    private val db = FirebaseFirestore.getInstance()
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_event)

        etNome = findViewById(R.id.etNome)
        etDescricao = findViewById(R.id.etDescricao)
        imgEvent = findViewById(R.id.imgEvent)
        btnSave = findViewById(R.id.btnSave)
        btnUpload = findViewById(R.id.btnUpload)

        btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSave.setOnClickListener {
            if (imageUrl != null) {
                addEvent()
            } else {
                Toast.makeText(this, "Aguarde o upload da imagem", Toast.LENGTH_SHORT).show()
            }
        }

        val event = intent.getSerializableExtra("event") as? Event
        event?.let {
            etNome.setText(it.nome)
            etDescricao.setText(it.descricao)
            Glide.with(this).load(it.imagem).into(imgEvent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            val inputStream = contentResolver.openInputStream(imageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imgEvent.setImageBitmap(bitmap)

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

    private fun addEvent() {
        val nome = etNome.text.toString()
        val descricao = etDescricao.text.toString()
        val imagem = imageUrl

        if (nome.isEmpty() || descricao.isEmpty() || imagem == null) {
            Toast.makeText(this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            return
        }

        val event = hashMapOf(
            "nome" to nome,
            "descricao" to descricao,
            "imagem" to imagem
        )

        db.collection("Eventos").add(event)
            .addOnSuccessListener {
                Toast.makeText(this, "Evento adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao adicionar evento", Toast.LENGTH_SHORT).show()
            }
    }
}