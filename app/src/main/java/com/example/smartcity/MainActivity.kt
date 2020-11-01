package com.example.smartcity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcity.adapter.NotaAdapter
import com.example.smartcity.entities.Nota
import com.example.smartcity.viewmodel.AddNota
import com.example.smartcity.viewmodel.BlocoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var blocoViewModel: BlocoViewModel
    private val newNotaActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotaAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        blocoViewModel = ViewModelProvider(this).get(BlocoViewModel::class.java)
        blocoViewModel.allnotas.observe(this, Observer { notas ->
            notas?.let {adapter.setNotas(it)}
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNota::class.java)
            startActivityForResult(intent, newNotaActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if ( requestCode == newNotaActivityRequestCode && resultCode == Activity.RESULT_OK){

            /*val city = data?.getStringExtra(AddCity.EXTRA_REPLY)
            val capital = data?.getStringExtra(AddCity.EXTRA1_REPLY)
            val city1 = city?.let { capital?.let { it1 -> City(city = it, capital = it1) } }
            cityViewModel.insert(city1!!)*/

            val titulo = data?.getStringExtra(AddNota.EXTRA_TITULO)
            val subtitulo = data?.getStringExtra(AddNota.EXTRA_SUBTITULO)
            val notatexto = data?.getStringExtra(AddNota.EXTRA_NOTA)
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            val formatted = current.format(formatter)
            val nota =
                notatexto?.let { subtitulo?.let { it1 -> titulo?.let { it2 -> Nota(titulo= it2, subtitulo= it1, data=formatted, nota= it) } } }
            blocoViewModel.insert(nota!!)

        }else{
            Toast.makeText(
                applicationContext,
                getString(R.string.mensagemErro),
                Toast.LENGTH_LONG).show()
        }
    }
}