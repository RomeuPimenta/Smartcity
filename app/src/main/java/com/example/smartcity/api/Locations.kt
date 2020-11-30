package com.example.smartcity.api

import java.util.*


data class Locations(
    val id: Int,
    val texto: String,
    val data: String,
    val id_utilziador: Int,
    val morada: String,
    val lat: Double,
    val lng: Double
)
