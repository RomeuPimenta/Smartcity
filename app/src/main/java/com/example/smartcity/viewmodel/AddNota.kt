package com.example.smartcity.viewmodel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.smartcity.R

class AddNota : AppCompatActivity() {

    private lateinit var titulo: EditText
    private lateinit var subtitulo: EditText
    private lateinit var nota: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_nota)

        titulo = findViewById(R.id.titulo)
        subtitulo = findViewById(R.id.subtitulo)
        nota = findViewById(R.id.nota)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if(titulo.text.isEmpty()){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }else{
                val titulotext = titulo.text.toString()
                val subtitulotext = subtitulo.text.toString()
                val notatext = nota.text.toString()
                replyIntent.putExtra(EXTRA_TITULO, titulotext)
                replyIntent.putExtra(EXTRA_SUBTITULO, subtitulotext)
                replyIntent.putExtra(EXTRA_NOTA, notatext)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_TITULO = "titulo"
        const val EXTRA_SUBTITULO = "subtitulo"
        const val EXTRA_NOTA = "nota"
    }
}
