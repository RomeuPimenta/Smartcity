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
import com.example.smartcity.api.UserLogin
import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt
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


        call.enqueue(object : Callback<UserLogin> {
            override fun onResponse(call: Call<UserLogin>, response: Response<UserLogin>) {

                if (response.isSuccessful) {

                    Log.e("bcrypt", response.body()?.password.toString())
                    //val obj = JSONObject(response.body().toString())
                    if(BCrypt.checkpw(password.text.toString(), response.body()?.password.toString())){
                        Toast.makeText(applicationContext, R.string.loginSucesso, Toast.LENGTH_LONG).show()
                        Log.e("response", response.toString())
                        val intent = Intent(this@MainActivity, MapsActivity::class.java)
                        val sharedPreferences: SharedPreferences = getSharedPreferences("REMEMBER", Context.MODE_PRIVATE);
                        val remember = sharedPreferences.edit();
                        remember.putString("username", response.body()?.username.toString())
                        remember.putString("utilizador_id", response.body()?.id.toString())
                        remember.apply()
                        
                        startActivity(intent)
                        finish()
                    }else

                    if(response.body()?.message.toString() == "password_incorreta"){
                        username.error = R.string.loginErrado.toString();
                        password.error = R.string.loginErrado.toString();
                    }
                }
            }

            override fun onFailure(call: Call<UserLogin>, t: Throwable) {
                username.error = R.string.loginErrado.toString();
                password.error = R.string.loginErrado.toString();
                Log.e("responseError", t.toString())
                //Toast.makeText(applicationContext, "Erro", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
