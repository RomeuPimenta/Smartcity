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
import com.example.smartcity.api.User
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.parseInt


class MainActivity : AppCompatActivity() {
    var button: Button? = null;
    var buttonLogin: Button? = null;
    var buttonRegistar: Button? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences: SharedPreferences = getSharedPreferences("REMEMBER", Context.MODE_PRIVATE);
        val remember = sharedPreferences.getString("username", null);
        if(remember != null){
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
            finish()
        }
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


        call.enqueue(object : Callback<Object> {
            override fun onResponse(call: Call<Object>, response: Response<Object>) {

                if (response.isSuccessful) {
                    val obj = JSONObject(response.body().toString())

                    if(obj["message"] == "password_incorreta"){
                        username.error = "Username ou password incorreta";
                        password.error = "Username ou password incorreta";
                    }else{
                        Toast.makeText(applicationContext, "Login efetuado com sucesso", Toast.LENGTH_LONG).show()
                        Log.e("response", response.toString())
                        val intent = Intent(this@MainActivity, MapsActivity::class.java)
                        val sharedPreferences: SharedPreferences = getSharedPreferences("REMEMBER", Context.MODE_PRIVATE);
                        val remember = sharedPreferences.edit();
                        remember.putString("username", username.text.toString())
                        remember.putInt("id_utilizador", parseInt(obj["id"].toString()))
                        remember.apply();
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<Object>, t: Throwable) {
                username.error = "Username ou password incorreta";
                password.error = "Username ou password incorreta";
                Log.e("responseError", t.toString())
                //Toast.makeText(applicationContext, "Erro", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
