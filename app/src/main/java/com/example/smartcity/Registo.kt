package com.example.smartcity

import android.content.Intent
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
import org.mindrot.jbcrypt.BCrypt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registo: AppCompatActivity() {

    var button_registar: Button? =null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registo)

        setViews()
        setClickListener()
    }

    fun setViews(){
        button_registar=findViewById<Button>(R.id.button_registo)
    }

    fun setClickListener(){
        button_registar?.setOnClickListener {

            setClickListenerRegistar()
        }
    }


    fun setClickListenerRegistar(){

        val username = findViewById<EditText>(R.id.login)
        val password = findViewById<EditText>(R.id.password)
        val password2 = findViewById<EditText>(R.id.password2)

        if(password.text.toString() == password2.text.toString()){

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.registo(username.text.toString(), BCrypt.hashpw(password.text.toString(), BCrypt.gensalt()))

        call.enqueue(object : Callback<Object> {

            override fun onResponse(call: Call<Object>, response: Response<Object>) {
                Log.e("teste", response.toString())
                if (response.isSuccessful) {
                    val obj = JSONObject(response.body().toString())

                    if(obj["message"] == "utilizador_ja_existe"){
                        username.error = getString(R.string.usernameExistente)

                    }else{
                        Toast.makeText(applicationContext, R.string.registoSucesso, Toast.LENGTH_LONG).show()
                        onBackPressed()
                    }
                }
            }

            override fun onFailure(call: Call<Object>, t: Throwable) {

                username.error = getString(R.string.usernameExistente)
            }
        })
        }else{
             password.error = R.string.passwordNCoin.toString();
            password2.error = R.string.passwordNCoin.toString();
        }
    }
}
