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

class AddEditMapaActivity : AppCompatActivity() {

    private lateinit var etDescricaoMapa: EditText
    private lateinit var imgMapa: ImageView
    private lateinit var btnSaveMapa: Button
    private lateinit var btnChooseImage: Button

    private val db = FirebaseFirestore.getInstance()
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_mapa)

        etDescricaoMapa = findViewById(R.id.etDescricaoMapa)
        imgMapa = findViewById(R.id.imgMapa)
        btnSaveMapa = findViewById(R.id.btnSaveMapa)
        btnChooseImage = findViewById(R.id.btnChooseImage)

        val mapaId = intent.getStringExtra("MAPA_ID")

        if (mapaId != null) {
            db.collection("Mapas").document(mapaId).get().addOnSuccessListener { document ->
                val mapa = document.toObject(Mapa::class.java)
                etDescricaoMapa.setText(mapa?.descricaoDoMapa)
                Glide.with(this).load(mapa?.imagemDoMapa).into(imgMapa)
            }
        }

        btnChooseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSaveMapa.setOnClickListener {
            if (imageUrl != null) {
                saveMapa(mapaId)
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
            imgMapa.setImageBitmap(bitmap)

            uploadImageToStorage(imageUri!!)
        }
    }

    private fun uploadImageToStorage(imageUri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference.child("mapas/${UUID.randomUUID()}")
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

    private fun saveMapa(mapaId: String?) {
        val descricaoDoMapa = etDescricaoMapa.text.toString().trim()

        if (descricaoDoMapa.isEmpty() || imageUrl == null) {
            Toast.makeText(this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            return
        }

        val mapaData = hashMapOf(
            "descricaoDoMapa" to descricaoDoMapa,
            "imagemDoMapa" to imageUrl
        )

        db.collection("Mapas").add(mapaData)
            .addOnSuccessListener {
                Toast.makeText(this, "Mapa adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao adicionar mapa", Toast.LENGTH_SHORT).show()
            }
    }
}
