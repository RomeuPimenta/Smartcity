package com.example.smartcity.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "notas_table")

class Nota(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "titulo") val titulo: String,
    @ColumnInfo(name= "subtitulo") val subtitulo: String,
    @ColumnInfo(name= "data") val data: String,
    @ColumnInfo(name = "nota") val nota: String

)

