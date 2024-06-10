package com.example.agoravai

import java.io.Serializable

data class Mapa(
    var id: String = "",
    var descricaoDoMapa: String = "",
    var imagemDoMapa: String = ""
) : Serializable

