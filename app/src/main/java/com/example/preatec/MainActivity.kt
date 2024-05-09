package com.example.preatec

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private lateinit var logo: ImageView
private lateinit var animacion: Animation


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        logo = findViewById(R.id.logotipo)
        animacion = AnimationUtils.loadAnimation(this,R.anim.spa)

        logo.startAnimation(animacion)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this,Login::class.java)
                val transicion = ActivityOptions.makeSceneTransitionAnimation(
                    this, Pair(logo,"logo_trans")
                )
                startActivity(intent, transicion.toBundle())
            }, 4000
        )



         }
}