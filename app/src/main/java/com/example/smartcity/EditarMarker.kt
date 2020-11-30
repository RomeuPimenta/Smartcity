package com.example.smartcity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.smartcity.api.EndPoints
import com.example.smartcity.api.Locations
import com.example.smartcity.api.ServiceBuilder
import com.example.smartcity.entities.Nota
import com.example.smartcity.viewmodel.BlocoViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.editar_markers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarMarker : AppCompatActivity(){

    lateinit var id_marker :String
    var texto: String?=null
    var id: String?=null
    var morada: String?=null
    var id_utilizador: Int = 0
    var data: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editar_markers)
        val buttonEdit = findViewById<Button>(R.id.editar_marker)
        val intent = getIntent();


        id = intent.getStringExtra("id_marker").toString()

        val textoView: EditText = findViewById(R.id.texto)
        val moradaView: EditText = findViewById(R.id.morada)
        getMarkerById(id.toString());

        buttonEdit.setOnClickListener {
            if(textoView.text.isEmpty()){
                Toast.makeText(applicationContext, "É necessário ter um texto", Toast.LENGTH_SHORT).show()
            }else{
                texto = textoView.text.toString()
                morada = moradaView.text.toString()

                val request2 = ServiceBuilder.buildService(EndPoints::class.java)
                val call2 = request2.update(textoView.text.toString(), moradaView.text.toString(), id!!.toInt())
                call2.enqueue(object: Callback<Object> {
                    override fun onResponse(call: Call<Object>, response: Response<Object>){
                        Log.e("TesteIf", response.toString())
                        if(response.isSuccessful){
                            Toast.makeText(applicationContext, "Editado com sucesso", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Object>, t: Throwable) {
                        Toast.makeText(applicationContext, "Erro!", Toast.LENGTH_SHORT).show()
                        Log.e("TesteErro", t.toString())
                    }
                })

            }
        }

    }

     fun getMarkerById(id: String){
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getMarkerById(id!!.toInt())
         val textoView: EditText = findViewById(R.id.texto)
         val moradaView: EditText = findViewById(R.id.morada)
        call.enqueue(object: Callback<List<Locations>> {
            override fun onResponse(call: Call<List<Locations>>, response: Response<List<Locations>>){
                if(response.isSuccessful){

                    for(entry in response.body()!!){
                        //val loc = LatLng(entry.lat, entry.lng)
                        texto = entry.texto
                        morada = entry.morada
                        id_utilizador = entry.id_utilziador
                        data = entry.data
                    }


                    textoView.setText(texto)
                    moradaView.setText(morada)

                }
            }

            override fun onFailure(call: Call<List<Locations>>, t: Throwable) {
                Toast.makeText(applicationContext, "Erro!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_editar, menu)
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
                            Log.e("Teste40", id!!.toString())
                            val request3 = ServiceBuilder.buildService(EndPoints::class.java)
                            val call3 = request3.apagarmarker(id!!.toInt())

                            call3.enqueue(object: Callback<Object> {
                                override fun onResponse(call: Call<Object>, response: Response<Object>){
                                    Log.e("Teste400", id!!.toString())
                                    if(response.isSuccessful){
                                        Log.e("Teste4000", id!!.toString())
                                        Toast.makeText(applicationContext, R.string.ApagadoCSucesso, Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }

                                override fun onFailure(call: Call<Object>, t: Throwable) {
                                    Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_SHORT).show()
                                    Log.e("Teste40000", id!!.toString())
                                }
                            })

                            finish()
                        }) // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getString(R.string.nao), null)
                    .show()


                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

}
