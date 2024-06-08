package com.example.agoravai

import java.io.Serializable

data class Event(
    val id: String = "",
    val nome: String = "",
    val descricao: String = "",
    val imagem: String = ""
) : Serializable