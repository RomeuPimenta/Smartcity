package com.example.smartcity.db


import androidx.lifecycle.LiveData
import com.example.smartcity.dao.BlocoDao
import com.example.smartcity.entities.Nota

class BlocoRepository(private val blocoDao: BlocoDao) {

    val allnotas: LiveData<List<Nota>> = blocoDao.getAlphabetizedCities()

    suspend fun insert(nota: Nota){
        blocoDao.insert(nota)
    }

    suspend fun edit(nota: Nota){
        blocoDao.update(nota)
    }
}
