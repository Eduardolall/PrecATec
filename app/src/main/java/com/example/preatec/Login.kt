package com.example.preatec

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest


class Login : AppCompatActivity() {

    lateinit var iniciar: Button
    lateinit var registrar: Button
    lateinit var correo : TextInputLayout
    lateinit var pass : TextInputLayout
    lateinit var sesion: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        iniciar = findViewById(R.id.btnIngresar)
        correo = findViewById(R.id.email)
        pass = findViewById(R.id.password)
        registrar = findViewById(R.id.btnRegistrar)

        //inicializacion de la variable sesion con el archivo de preferencias llamado "sesion"
        sesion = getSharedPreferences("sesion", 0)

        iniciar.setOnClickListener { login() }

        registrar.setOnClickListener {
            startActivity(Intent(this, RegistrarUsu::class.java))
        }
    }
        private fun login() {
            //url de la peticion del login
            val url = Uri.parse(Config.URL + "/auth/login")
                .buildUpon()
                .build().toString()
            //debe crearse una peticion de tipo StringRequest con el metodo POST para la url de login debido al que el servidor siempre devulve un string con el token
            val peticion = object:StringRequest(Request.Method.POST, url, {
                //si la peticion es exitosa el token esta en response
                    response ->
                //se guarda el token "jwt" y el nombre de usuario en el archivo de preferencias
                with(sesion.edit()) {
                    putString("jwt", response)
                    putString("email", correo.editText?.text.toString())
                    apply()
                }
                //se inicia la actividad MainActivity2
                startActivity(Intent(this, Wmenu::class.java))
            }, { error ->
                //si la peticion falla se muestra un mensaje de error
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                //se muestra la traza completa del error en el Logcat
                Log.e("LOGIN", error.stackTraceToString())
            }){
                //se envian los datos de la peticion usuario y contraseña para el post
                override fun getParams(): Map<String, String>{
                    val body: MutableMap<String, String> = HashMap()
                    body["email"] = correo.editText?.text.toString()
                    body.put("password", pass.editText?.text.toString())
                    return body
                }
            }
            //se agrega la peticion a la cola de peticiones para que sea procesada
            MySingleton.getInstance(applicationContext).addToRequestQueue(peticion)

    }
}