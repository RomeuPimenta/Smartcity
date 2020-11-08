package com.example.smartcity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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

    var id : Int = 0
    lateinit var titulo :String
    lateinit var subtitulo :String
    lateinit var data :String
    lateinit var nota :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_notas)

        val intent = getIntent();

        id = intent.getIntExtra("id",-1)
        titulo = intent.getStringExtra("titulo").toString()
        subtitulo = intent.getStringExtra("subtitulo").toString()
        data = intent.getStringExtra("data").toString()
        nota = intent.getStringExtra("nota").toString()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menuvernota, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.botao_eliminar -> {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.tituloCerteza2))
                    .setMessage(getString(R.string.mensagemCerteza2)) // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(
                        getString(R.string.sim),
                        DialogInterface.OnClickListener { dialog, which ->
                            Log.e("id", id.toString())
                            val notaDelete: Nota = Nota (
                                id = id,
                                titulo = titulo,
                                subtitulo = subtitulo,
                                data = data,
                                nota = nota
                            )

                            blocoViewModel.delete(notaDelete)
                            finish()
                        }) // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getString(R.string.nao), null)
                    .show()


                true
            } else -> super.onOptionsItemSelected(item)
        }
    }
}
