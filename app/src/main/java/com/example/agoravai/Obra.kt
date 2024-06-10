package com.example.agoravai

import java.io.Serializable

data class Obra(
    var id: String = "",
    val nome: String = "",
    val ano: String = "",
    val autor: String = "",
    val imagem: String = ""
) : Serializable
