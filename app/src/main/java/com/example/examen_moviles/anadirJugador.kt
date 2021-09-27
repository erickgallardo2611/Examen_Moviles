package com.example.examen_moviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.moviles_computacion_2021_b.BJugador
import com.example.moviles_computacion_2021_b.BTorneo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class anadirJugador : AppCompatActivity() {
    //val datos = ESqliteHelperUsuario(this)
    var adaptador: ArrayAdapter<BJugador>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val torneo_selct = intent.getParcelableExtra<BTorneo>("torneo")

        setContentView(R.layout.activity_anadir_jugador)
        val txtIdplayer = findViewById<TextView>(R.id.idjugador)
        val btnBuscar = findViewById<Button>(R.id.findJugador)
        //val jugadores = datos.mostrarMenosJugadorTorneo()
        //Log.i("Jugadores view","${jugadores}")
        val jugadores: ArrayList<BJugador> = ArrayList<BJugador>()
        if(jugadores!=null) {
            adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                jugadores
            )
            Log.i("Jugador Encontrado", "${adaptador}")
            val lisviewVista = findViewById<ListView>(R.id.encontradoJugador)
            lisviewVista.adapter = adaptador
        }
        btnBuscar.setOnClickListener{
            //val jugador=datos.consultarJugadorPorId(txtIdplayer.text.toString().toInt())
            val jugador = consultarUno()
            if(jugador.elo!=0){
                if (torneo_selct != null) {
                    //datos.actualizarJugadorTorneoFormulario(torneo_selct.id_torneo!!,txtIdplayer.text.toString().toInt())
                }
                abrirActividad(MainActivity::class.java)
                Log.i("Jugador Encontrado","${jugador}")
            }else{
                Log.i("Jugador no encontrado","${jugador}")
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Jugador no encontrado")
                builder.setPositiveButton("Aceptar", null)
                val dialogo = builder.create()
                dialogo.show()
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
    fun consultarUno():BJugador{
        val db = Firebase.firestore
        var agregarJugador = BJugador("",1,"")
        var jugadoresRef = db
            .collection("proyectoAjedrez")
        jugadoresRef
            .whereEqualTo("nombre","erick")
            .get()
            .addOnSuccessListener {
                for( jugadores in it){
                    Log.i("jugador","${jugadores.data}")
                    agregarJugador = BJugador(jugadores.get("nombre").toString(),Integer.parseInt(jugadores.get("elo").toString()),jugadores.get("nacionalidad").toString())
                }
            }
            .addOnFailureListener {  }
        return agregarJugador
    }
}