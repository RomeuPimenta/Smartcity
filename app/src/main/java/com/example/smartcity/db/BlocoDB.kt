package com.example.smartcity.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smartcity.dao.BlocoDao
import com.example.smartcity.entities.Nota
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Database(entities = arrayOf(Nota::class), version= 4, exportSchema = false)

public abstract class BlocoDB: RoomDatabase() {

    abstract fun blocoDao(): BlocoDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback(){

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch{
                    var blocoDao = database.blocoDao()

                    blocoDao.deleteAll()

                    val current = LocalDateTime.now()

                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    val formatted = current.format(formatter)

                    var nota = Nota(1, "Projeto", "Escola", formatted.toString(), "Tem que ser feito o projeto")
                    blocoDao.insert(nota)
                }

            }
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: BlocoDB? = null
        fun getDatabase(context: Context, scope: CoroutineScope): BlocoDB{
            val tempInstance = INSTANCE
            if(tempInstance !=null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BlocoDB::class.java,
                    "notas_database"
                )

                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }

}
