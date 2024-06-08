package com.example.agoravai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

class loginfuncionario : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var senhaEditText: EditText
    private lateinit var entrarFunc: Button
    private lateinit var retornarLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loginfuncionario)

        // Inicialize o Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Referencie os elementos de UI do layout
        emailEditText = findViewById(R.id.nome_Func)
        senhaEditText = findViewById(R.id.editTextTextPassword)
        entrarFunc = findViewById(R.id.entrar_Func)
        retornarLogin = findViewById(R.id.retornar_login)

        // Configurar o clique do botão de login
        entrarFunc.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()
            signInWithEmailAndPassword(email, senha)
        }

        // Configurar o clique do botão de retornar ao login
        retornarLogin.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun signInWithEmailAndPassword(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login bem-sucedido, redirecione para a tela HomeFuncionario
                    val intent = Intent(this, HomeFuncionario::class.java)
                    startActivity(intent)
                } else {
                    val mensagemErro = "Falha no login. Verifique seu e-mail e senha."
                    Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show()
                }
            }
    }
}