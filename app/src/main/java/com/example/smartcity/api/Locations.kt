package com.example.smartcity.api

import java.util.*


data class Locations(
    val id: Int,
    val texto: String,
    val data: String,
    val utilizador_id: Int,
    val morada: String,
    val lat: Double,
    val lng: Double,
    val username: String,
    val tipo_id: String,
    val tipo: String
)
