package com.example.examen_moviles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.moviles_computacion_2021_b.BJugador
import com.example.moviles_computacion_2021_b.BTorneo
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Jugadores : AppCompatActivity() {
    var query: Query? = null
    var posiconElementoSeleccionado = 0
    var nuevoJugador = BJugador("","","",0)
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 400
    var listaJugador: ArrayList<BJugador> = ArrayList()
    //val datos = ESqliteHelperUsuario(this)
    var adaptador: ArrayAdapter<BJugador>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jugadores)
        //var jugadores=datos.mostrarTodoJugador()
        //var jugadores =
        val torneoSelect = intent.getParcelableExtra<BTorneo>("torneo")
        var btn_crear = findViewById<Button>(R.id.btn_crear_jugador)
        btn_crear.setOnClickListener{
            abrirActividad(CrearJugador::class.java)
        }
        Log.i("OncreateJugador","en actividad jugadores")
        Log.i("los jugadores","${listaJugador}")
        listaJugador
        if(torneoSelect!=null) {
            consultaJugadoresInscritos(torneoSelect.nombre.toString())
            btn_crear.setVisibility(View.GONE)
            //jugadores = datos.mostrarTodoJugadorTorneo(torneoSelect.id_torneo!!)

            Log.i("ID_del torneo","${torneoSelect}")
            adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                listaJugador
            )
        }else{
            consulta()
            adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                listaJugador
            )
        }

        Log.i("Parametrs","onCreate ${listaJugador}")
        val vista=findViewById<ListView>(R.id.listTorneo)
        vista.adapter = adaptador

        registerForContextMenu(vista)
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
    fun abrirActividadEdit(
        clase: Class<*>,
        jugador: BJugador
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        intentExplicito.putExtra("jugador",jugador)
        Log.i("Parametrs","onCreate ${jugador.nombre} - ${jugador.elo}")
        this.startActivity(intentExplicito);
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu2,menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position

        posiconElementoSeleccionado = id

        Log.i("list-view","onCreate ${id}")
        //Log.i("list-view","Usuario ${datos.mostrarTodoJugador()}")

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var jugadorSelected = adaptador!!.getItem(posiconElementoSeleccionado)
        return when (item?.itemId) {
            // Editar
            R.id.miEditar -> {
                if (jugadorSelected != null) {
                    Log.i("Parametrs","onCreate ${jugadorSelected.nombre} - ${jugadorSelected.elo}")
                    //val Anterior = datos.consultarJugadorPorId(jugadorSelected.id_jugador!!)
                    val Anterior : BJugador = BJugador("","",jugadorSelected.nombre,jugadorSelected.elo)
                    abrirActividadEdit(CrearJugador::class.java,Anterior)
                }else{
                    Log.i("Parametrs","nulos ")
                }

                return true
            }
            // Eliminar
            R.id.miEliminar -> {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Desea eliminar?")

                builder.setNegativeButton("Cancelar", null)
                builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(){ Dialog, which ->
                    try {
                        val db = Firebase.firestore
                        if (jugadorSelected != null) {
                            db.collection("proyectoAjedrez")
                                .document(jugadorSelected.nombre.toString())
                                .delete()
                                .addOnSuccessListener {

                                }
                                .addOnFailureListener { }
                        }
                        abrirActividad(MainActivity::class.java)
                    }finally{
                        Log.i("Erorr","no se puede eliminar")
                    }
                })
                val dialogo = builder.create()
                dialogo.show()

                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }
    fun consultaUnJugador(nombreJugador:String){
        val db = Firebase.firestore

        db.collection("proyectoAjedrez")
            .document(nombreJugador)
            .get()
            .addOnSuccessListener {
                //Log.i("un jugador ", "${it}")
                nuevoJugador = BJugador("","",it.get("nombre").toString(),Integer.parseInt(it.get("elo").toString()))
                Log.i("un jugador ", "${nuevoJugador}")
                listaJugador.add(nuevoJugador)
                adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    listaJugador
                )
                Log.i("Parametrs","onCreate ${listaJugador}")
                val vista=findViewById<ListView>(R.id.listTorneo)
                vista.adapter = adaptador
            }
    }
    fun consulta():ArrayList<BJugador>{
        Log.i("consulta","se realiza consulta")
        val db = Firebase.firestore
        val jugadoresConsultados = ArrayList<BJugador>()
        var jugadoresRef = db
            .collection("proyectoAjedrez")
        jugadoresRef.get()
            .addOnSuccessListener {
                for( jugadores in it){
                    Log.i("jugador","${jugadores.data}")
                    listaJugador.add(BJugador("","",jugadores.get("nombre").toString(),Integer.parseInt(jugadores.get("elo").toString())))
                }
                adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    listaJugador
                )
                Log.i("Parametrs","onCreate ${listaJugador}")
                val vista=findViewById<ListView>(R.id.listTorneo)
                vista.adapter = adaptador

            }
            .addOnFailureListener {  }

        return jugadoresConsultados
    }
    fun consultaJugadoresInscritos(nombreTorneo:String){
        Log.i("consulta","se realiza consulta")
        val db = Firebase.firestore
        val jugadoresConsultados = ArrayList<BJugador>()
        db
            .collection("torneoAjedrez")
            .document(nombreTorneo)
            .get()
            .addOnSuccessListener {

                var nombreJugador = it.get("jugadores") as ArrayList<String>
                //Log.i("Arreglo","${it.get("jugadores")}   ${nombreJugador[0]}")
                for(i in 0..nombreJugador.size-1){
                    Log.i("arreglo ${i}","${nombreJugador[i]}")
                    consultaUnJugador(nombreJugador[i])
                }
            }
            .addOnFailureListener {  }
    }
}