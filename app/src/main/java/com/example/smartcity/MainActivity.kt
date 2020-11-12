package com.example.smartcity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.smartcity.api.EndPoints
import com.example.smartcity.api.ServiceBuilder
import com.example.smartcity.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    var button: Button? = null;
    var buttonLogin: Button? = null;
    var buttonRegistar: Button? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViews()
        setClickListener()


    }

    fun setViews() {
        button = findViewById<Button>(R.id.button_irnotas)
        buttonLogin = findViewById<Button>(R.id.button_login)
        buttonRegistar = findViewById<Button>(R.id.button_registo)
    }

    fun setClickListener() {
        button?.setOnClickListener {
            val intent = Intent(this@MainActivity, ListaNotas::class.java)
            startActivity(intent)
        }

        buttonLogin?.setOnClickListener {

            buttonLoginClickListener()
        }

        buttonRegistar?.setOnClickListener {
            val intent = Intent(this@MainActivity, Registo::class.java)
            startActivity(intent)
        }
    }

    fun buttonLoginClickListener() {
        val username = findViewById<EditText>(R.id.login)
        val password = findViewById<EditText>(R.id.password)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.login(username.text.toString(), password.text.toString())


        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.e("teste", response.toString())
                if (response.isSuccessful) {
                    Log.e("response", response.toString())
                    val intent = Intent(this@MainActivity, camadaTeste::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                username.error = "Username ou password incorreta";
                password.error = "Username ou password incorreta";
                Log.e("responseError", t.toString())
                //Toast.makeText(applicationContext, "Erro", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
