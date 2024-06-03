package com.example.preatec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.preatec.databinding.ActivityWmenuBinding


class Login : AppCompatActivity() {

    private lateinit var iniciar: Button
    private lateinit var registrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        iniciar= findViewById(R.id.btnIngresar)
        registrar = findViewById(R.id.btnRegistrar)

        iniciar.setOnClickListener {
            startActivity(Intent(this, Wmenu::class.java))
        }

        registrar.setOnClickListener {
            startActivity(Intent(this, RegistrarUsu::class.java))
        }



    }
}