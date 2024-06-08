package com.example.agoravai

import java.io.Serializable

data class Event(
    var id: String = "",
    val nome: String = "",
    val descricao: String = "",
    val imagem: String = ""
) : Serializable