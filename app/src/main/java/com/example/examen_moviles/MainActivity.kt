package com.example.examen_moviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_torneo = findViewById<Button>(R.id.button_torneo)
        button_torneo.setOnClickListener{
            abrirActividad(IntTorneo::class.java)
        }
        val button_Jugador = findViewById<Button>(R.id.button_participante)
        button_Jugador.setOnClickListener{
            abrirActividad(Jugadores::class.java)
        }
    }

    fun abrirActividad(
        clase: Class<*>
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        this.startActivity(intentExplicito);
    }
}