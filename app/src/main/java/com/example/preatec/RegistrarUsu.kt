package com.example.preatec

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject

class RegistrarUsu : AppCompatActivity() {

    lateinit var nombre: TextInputLayout
    lateinit var apaterno: TextInputLayout
    lateinit var amaterno: TextInputLayout
    lateinit var correo: TextInputLayout
    lateinit var pass: TextInputLayout

    private lateinit var btnRegistrar: Button
    private lateinit var btnEstoyRegistrado: MaterialButton

    private lateinit var sesion: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_usu)

        nombre = findViewById(R.id.nombre)
        apaterno = findViewById(R.id.apaterno)
        amaterno = findViewById(R.id.amaterno)
        correo = findViewById(R.id.email_registro)
        pass = findViewById(R.id.password_registro)

        btnRegistrar = findViewById(R.id.btnRegistrarDatos)
        btnEstoyRegistrado = findViewById(R.id.btnYaEstoyRegistrado)


        btnRegistrar.setOnClickListener { saveNew() }


        btnEstoyRegistrado.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }

    private fun saveNew() {
// URL de la petición de registro
        val url = "https://preactec-server.onrender.com/users/add"

        // Crear el objeto JSON con los datos de registro
        val jsonBody = JSONObject().apply {
            put("name", findViewById<TextInputLayout>(R.id.nombre).editText?.text.toString())
            put("password", findViewById<TextInputLayout>(R.id.password).editText?.text.toString())
            put("lastname", findViewById<TextInputLayout>(R.id.apaterno).editText?.text.toString())
            put("email", findViewById<TextInputLayout>(R.id.email).editText?.text.toString())

        }

        // Crear una petición de tipo JsonObjectRequest con el método POST para la URL de registro

        // Crear una petición de tipo JsonObjectRequest con el método POST para la URL de registro
        val peticion = JsonObjectRequest(Request.Method.POST, url, jsonBody, { response ->
            // Si la petición es exitosa, maneja la respuesta (puede ser un mensaje de éxito o un token)
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()
            // Inicia la actividad Wmenu o cualquier otra actividad
            startActivity(Intent(this, Wmenu::class.java))
        }, { error ->
            // Si la petición falla, se muestra un mensaje de error
            val networkResponse = error.networkResponse
            if (networkResponse != null) {
                val statusCode = networkResponse.statusCode
                val data = String(networkResponse.data, Charsets.UTF_8)
                Log.e("REGISTER", "Error: $statusCode\n$data")
                Toast.makeText(this, "Error: $statusCode\n$data", Toast.LENGTH_LONG).show()
            } else {
                Log.e("REGISTER", error.toString())
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }
            // Se muestra la traza completa del error en el Logcat
            Log.e("REGISTER", error.stackTraceToString())
        })

        // Agrega la petición a la cola de peticiones para que sea procesada
        Volley.newRequestQueue(this).add(peticion)

    }
    }

