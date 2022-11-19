package com.keepcoding.dragonball

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class LoginActivity : AppCompatActivity() {

    var tiempoInicio = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_constraint)
        var hola = "Hola"
        println(hola)
        Log.v(LoginActivity::class.java.simpleName, "onCreate finishing...")
    }

    override fun onStart() {
        super.onStart()
        tiempoInicio = System.currentTimeMillis()
    }

    override fun onStop() {
        val tiempoFin = System.currentTimeMillis()
        val tiempoEnLaApp = tiempoFin -tiempoInicio

        with(getPreferences(Context.MODE_PRIVATE)){
            val tiempoAnterior = getLong("Tiempo",0L)
            val preferenciasEditables = edit()
            preferenciasEditables.putLong("Tiempo",tiempoEnLaApp + tiempoAnterior)
            preferenciasEditables.apply()
        }

        super.onStop()
    }
}