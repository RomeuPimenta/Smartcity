package com.example.smartcity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartcity.api.EndPoints
import com.example.smartcity.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InserirDados : AppCompatActivity() {

    var button_save: Button? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inserir_dados)
        val intent = getIntent();
        setViews();
        clickListener(intent)
    }

    fun setViews() {
        button_save = findViewById(R.id.button_save);
    }

    fun clickListener(intent: Intent) {
        button_save?.setOnClickListener {
            val editext = findViewById<EditText>(R.id.texto)
            val texto = editext.text
            val lat = intent.getStringExtra("lat")
            val lng = intent.getStringExtra("lng")
            Log.e("lat", lat.toString())
            val morada = intent.getStringExtra("morada")
            val sharedPreferences: SharedPreferences =
                getSharedPreferences("REMEMBER", Context.MODE_PRIVATE);
            val id_utilizador = sharedPreferences.getInt("id_utilizador", 0);

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call =
                request.inserir(texto.toString(), lat!!.toFloat(), lng!!.toFloat(), morada.toString(), id_utilizador)

            call.enqueue(object : Callback<Object> {
                override fun onResponse(call: Call<Object>, response: Response<Object>) {
                    Log.e("teste", response.toString())
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

                    Toast.makeText(applicationContext, "Erro ao inserir", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
