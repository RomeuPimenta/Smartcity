package com.example.smartcity.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.smartcity.entities.Nota

@Dao

interface BlocoDao {

    @Query("SELECT * FROM notas_table ORDER BY nota ASC")
    fun getAlphabetizedCities(): LiveData<List<Nota>>

    /*@Query("SELECT * FROM notas_table WHERE capital == :name")
    fun getCitiesFromCountry(name: String): LiveData<List<City>>*/

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Query("DELETE FROM notas_table")
    suspend fun deleteAll()

    @Update
    fun update(nota: Nota)
}
