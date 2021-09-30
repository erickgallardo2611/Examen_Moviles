package com.example.examen_moviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.moviles_computacion_2021_b.BJugador
import com.example.moviles_computacion_2021_b.BTorneo
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class anadirJugador : AppCompatActivity() {
    //val datos = ESqliteHelperUsuario(this)
    var listaJugador: ArrayList<BJugador> = ArrayList()
    var adaptador: ArrayAdapter<BJugador>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val torneo_selct = intent.getParcelableExtra<BTorneo>("torneo")
        setContentView(R.layout.activity_anadir_jugador)
        val txtIdplayer = findViewById<TextView>(R.id.idjugador)
        val btnBuscar = findViewById<Button>(R.id.findJugador)
        //val jugadores = datos.mostrarMenosJugadorTorneo()
        //Log.i("Jugadores view","${jugadores}")
        listaJugador
        consulta()
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaJugador
        )
        Log.i("Jugador Encontrado", "${listaJugador}")
        val lisviewVista = findViewById<ListView>(R.id.encontradoJugador)
        lisviewVista.adapter = adaptador

        var jugadorSelected = BJugador("","","",1)
        lisviewVista.onItemClickListener = object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long) {

                // value of item that is clicked
                val itemValue = lisviewVista.getItemAtPosition(position) as BJugador


                // Toast the values
                Toast.makeText(applicationContext,
                    "Position :$position\nItem Value : $itemValue", Toast.LENGTH_LONG)
                    .show()
                jugadorSelected=itemValue
                txtIdplayer.setText(jugadorSelected.email.toString())
            }
        }

        btnBuscar.setOnClickListener{
            //val jugador=datos.consultarJugadorPorId(txtIdplayer.text.toString().toInt())
            if (torneo_selct != null) {
                jugadorSelected.email?.let { it1 ->
                    insertarJugador(torneo_selct.nombre.toString(),
                        it1
                    )
                }
            }
            abrirActividad(MainActivity::class.java)
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

    fun consulta(){
        Log.i("consulta","se realiza consulta")
        val db = Firebase.firestore
        val jugadoresConsultados = ArrayList<BJugador>()
        var jugadoresRef = db
            .collection("proyectoAjedrez")
        jugadoresRef.get()
            .addOnSuccessListener {
                for( jugadores in it){
                    Log.i("jugador","${jugadores.data}")
                    listaJugador.add(BJugador("",jugadores.get("email").toString(),jugadores.get("nombre").toString(),Integer.parseInt(jugadores.get("elo").toString())))
                }
                adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    listaJugador
                )
                Log.i("Parametrs","onCreate ${listaJugador}")
                val vista=findViewById<ListView>(R.id.encontradoJugador)
                vista.adapter = adaptador

            }
            .addOnFailureListener {  }

    }


    fun insertarJugador(nombreTorneo:String,correoJugador:String){
        val db = Firebase.firestore
        db.collection("torneoAjedrez")
            .document(nombreTorneo)
            .update("jugadores",FieldValue.arrayUnion(correoJugador))
            .addOnSuccessListener { Log.i("DocumentSnapshot successfully updated!","yes") }
            .addOnFailureListener { e -> Log.i("Error updating document", "F") }
    }


}