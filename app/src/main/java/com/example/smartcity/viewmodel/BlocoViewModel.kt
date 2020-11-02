package com.example.smartcity.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.smartcity.db.BlocoDB
import com.example.smartcity.db.BlocoRepository
import com.example.smartcity.entities.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlocoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BlocoRepository

    val allnotas: LiveData<List<Nota>>

    init{
        val notasDao = BlocoDB.getDatabase(application, viewModelScope).blocoDao()
        repository = BlocoRepository(notasDao)
        allnotas = repository.allnotas
    }

    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(nota)
    }

    fun edit(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.edit(nota)
    }
}

