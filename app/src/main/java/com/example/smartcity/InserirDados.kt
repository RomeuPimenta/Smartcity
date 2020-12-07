package com.example.smartcity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartcity.api.EndPoints
import com.example.smartcity.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InserirDados : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var button_save: Button? = null;
    var tipo: String? =null
    var lista = arrayOf(
        "Obras",
        "Danos na via",
        "Outros"
    )
    var tipo_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.inserir_dados)
        spinner();
        val intent = getIntent();
        setViews();
        clickListener(intent)
    }

    fun setViews() {
        button_save = findViewById(R.id.button_save);
    }

    fun clickListener(intent: Intent) {
        button_save?.setOnClickListener {

            buscarIdTipo(tipo.toString() )

        }
    }

    fun spinner(){
        val spin: Spinner = findViewById(R.id.spinner12) as Spinner
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.setAdapter(adapter)
        spin.setOnItemSelectedListener(this)
    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {

        Toast.makeText(applicationContext, "Selected Tipo: " + lista[position], Toast.LENGTH_SHORT).show()

        tipo = lista[position]
        Log.e("tipotipo", tipo.toString())
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        // TODO - Custom Code
    }

    fun buscarIdTipo(temp: String){

        Log.e("spinnerInserir1234", temp.toString())
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.buscaridtipo(temp)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                Log.e("testehj", response.body().toString())

                if (response.isSuccessful) {
                    tipo_id = response.body().toString()
                    functionInserir()
                    Log.e("05dec", "entrou")
                    Log.e("05dec20", tipo.toString())
                    Log.e("05dec20a", tipo_id.toString())
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.e("idUser", t.toString())

            }
        })

    }

    fun functionInserir(){
        val editext = findViewById<EditText>(R.id.texto)
        val texto = editext.text
        val lat = intent.getStringExtra("lat")
        val lng = intent.getStringExtra("lng")
        Log.e("lat", lat.toString())
        val morada = intent.getStringExtra("morada")
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("REMEMBER", Context.MODE_PRIVATE);

        val utilizador_id = sharedPreferences.getString("utilizador_id", null)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call =

            request.inserir(texto.toString(), lat!!.toFloat(), lng!!.toFloat(), morada.toString(), utilizador_id.toString(), tipo_id.toString())

        call.enqueue(object : Callback<Object> {
            override fun onResponse(call: Call<Object>, response: Response<Object>) {
                Log.e("testehj", response.body().toString())
                Log.d("testehj1", response.body().toString())
                if (response.isSuccessful) {

                    Toast.makeText(
                        applicationContext,
                        "Inserido com sucesso",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Object>, t: Throwable) {
                Log.e("idUser", t.toString())
                Log.d("idUser1", t.toString())
                Toast.makeText(applicationContext, R.string.err0, Toast.LENGTH_SHORT).show()
            }
        })
    }

}
