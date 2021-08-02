package com.example.examen_moviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.moviles_computacion_2021_b.BJugador
import com.example.moviles_computacion_2021_b.BTorneo
import com.example.moviles_computacion_2021_b.ESqliteHelperUsuario

class CrearJugador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_jugador)
        val jugadorAnterior = intent.getParcelableExtra<BJugador>("jugador")
        val btn_crear_jugador = findViewById<Button>(R.id.creaJugador)
        val nombrejugador = findViewById<TextView>(R.id.editNombre)
        val elojugador = findViewById<TextView>(R.id.textElo)
        val basedatos= ESqliteHelperUsuario(this)
        nombrejugador.editableText
        if(jugadorAnterior!=null){
            Log.i("Pase","${jugadorAnterior.nombre}")
            nombrejugador.setText(jugadorAnterior.nombre)
            elojugador.setText(jugadorAnterior.elo.toString())
            btn_crear_jugador.setText("Actualizar")
        }else{
            Log.i("Pase","no hay no existe :v")
        }
        btn_crear_jugador.setOnClickListener{
            if(nombrejugador.text.toString()=="Ingrese el nombre del jugador"||nombrejugador.text.toString()==""){
                Log.i("bdd", "No esta lleno")
            }else{
                Log.i("bdd", "Esta lleno")
                if(jugadorAnterior!=null){
                    val actualizado = basedatos.actualizarJugadorFormulario(nombrejugador.text.toString(),jugadorAnterior.id_jugador!!)
                    Log.i("bdd", "Esta lleno ${actualizado} ${jugadorAnterior.nombre}")
                }else{
                    if (basedatos != null) {
                        basedatos.crearJugadorFormulario(
                            nombrejugador.text.toString()
                        )
                        Log.i("bdd", "Torneo creado")
                    }
                }
                abrirActividad(MainActivity::class.java)
            }
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