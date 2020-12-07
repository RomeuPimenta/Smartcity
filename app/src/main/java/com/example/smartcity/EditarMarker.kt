package com.example.smartcity

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartcity.api.EndPoints
import com.example.smartcity.api.Locations
import com.example.smartcity.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditarMarker : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var tipos = arrayOf(
        "Obras",
        "Danos na via",
        "Outros"
    )
    lateinit var id_marker :String
    var texto: String?=null
    var id: String?=null
    var tipo_id: String?=null
    var morada: String?=null
    var utilizador_id: Int = 0
    var data: String?=null
    var tipo: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editar_markers)



        val buttonEdit = findViewById<Button>(R.id.editar_marker)
        val intent = getIntent();


        id = intent.getStringExtra("id_marker").toString()
        Log.e("tag3300", id.toString())
        val textoView: EditText = findViewById(R.id.texto)
        val moradaView: EditText = findViewById(R.id.morada)
        val spinner1: String = tipo.toString()

        Log.e("SPINNER", spinner1.toString())
        getMarkerById(id.toString());

        buttonEdit.setOnClickListener {
            if(textoView.text.isEmpty()){
                Toast.makeText(applicationContext, R.string.necessaryText, Toast.LENGTH_SHORT).show()
            }else{
                texto = textoView.text.toString()
                morada = moradaView.text.toString()

                val request2 = ServiceBuilder.buildService(EndPoints::class.java)
                val call2 = request2.update(textoView.text.toString(), moradaView.text.toString(), id!!.toInt(), tipo_id!!.toString())
                call2.enqueue(object: Callback<Object> {
                    override fun onResponse(call: Call<Object>, response: Response<Object>){
                        Log.e("TesteIf", response.toString())
                        if(response.isSuccessful){
                            Toast.makeText(applicationContext, R.string.editadoComSucess, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Object>, t: Throwable) {
                        Toast.makeText(applicationContext, R.string.err0, Toast.LENGTH_SHORT).show()
                        Log.e("TesteErro", t.toString())
                    }
                })

            }
        }

    }

     fun getMarkerById(id: String){
         Log.e("tag3301", id)
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getMarkerById(id!!.toInt())
         val textoView: EditText = findViewById(R.id.texto)
         val moradaView: EditText = findViewById(R.id.morada)
        call.enqueue(object: Callback<Locations> {
            override fun onResponse(call: Call<Locations>, response: Response<Locations>){
                Log.e("tag3301", response.body().toString())
                Log.e("tag33011", response.body()?.texto.toString())
                Log.e("tag33011a", response.body()?.morada.toString())

                texto = response.body()?.texto.toString()
                morada = response.body()?.morada.toString()
                tipo_id = response.body()?.tipo_id.toString()
                if(response.isSuccessful){
                    spinner();
                    textoView.setText(texto)
                    moradaView.setText(morada)

                }
            }

            override fun onFailure(call: Call<Locations>, t: Throwable) {
                Toast.makeText(applicationContext, R.string.err0, Toast.LENGTH_SHORT).show()
                Log.e("tag3303", t.toString())
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

     fun spinner(){
        val spin: Spinner = findViewById(R.id.spinner1) as Spinner
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.setAdapter(adapter)
        spin.setOnItemSelectedListener(this)
    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {

        Toast.makeText(applicationContext, "Selected Tipo: " + tipos[position], Toast.LENGTH_SHORT).show()

        tipo = tipos[position]
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        // TODO - Custom Code
    }

}
