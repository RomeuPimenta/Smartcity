package com.example.smartcity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.smartcity.entities.Nota
import com.example.smartcity.viewmodel.AddNota
import com.example.smartcity.viewmodel.BlocoViewModel
import kotlinx.android.synthetic.main.activity_add_nota.view.*


class VerNotas : AppCompatActivity() {
    private lateinit var blocoViewModel: BlocoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_notas)

        val intent = getIntent();

        val id = intent.getIntExtra("id",-1)
        val titulo = intent.getStringExtra("titulo")
        val subtitulo = intent.getStringExtra("subtitulo")
        val data = intent.getStringExtra("data")
        val nota = intent.getStringExtra("nota")
        Log.e("String", titulo.toString())
        val titulotext: EditText = findViewById(R.id.titulo)
        val subtitulotext: EditText = findViewById(R.id.subtitulo)
        val notatext: EditText = findViewById(R.id.nota)

        titulotext.setText(titulo)
        subtitulotext.setText(subtitulo)
        notatext.setText(nota)

        val button = findViewById<Button>(R.id.button_edit)
        blocoViewModel = ViewModelProvider(this).get(BlocoViewModel::class.java)
        button.setOnClickListener {
            if(titulotext.text.isEmpty()){
                Toast.makeText(applicationContext, "É necessário ter um titulo", Toast.LENGTH_SHORT).show()
            }else{
                val titulotext = titulotext.text.toString()
                val subtitulotext = subtitulotext.text.toString()
                val notatext = notatext.text.toString()
                Log.e("id", id.toString())
                val nota = data?.let { it1 ->
                    Nota(
                        id = id,
                        titulo = titulotext,
                        subtitulo = subtitulotext,
                        data = it1,
                        nota = notatext
                    )
                }
                Log.e("nota.id", nota?.id.toString())
                blocoViewModel.edit(nota!!)
            }
            finish()
        }

    }
}
