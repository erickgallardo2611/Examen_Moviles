package com.example.examen_moviles

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.moviles_computacion_2021_b.BTorneo
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class IntTorneo : AppCompatActivity() {
    var posiconElementoSeleccionado = 0
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 400
    var email=""
    //val datos=ESqliteHelperUsuario(this)
    var torneos: ArrayList<BTorneo> = ArrayList()
    var adaptador: ArrayAdapter<BTorneo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_int_torneo)
        consulta()
        val btn_crear = findViewById<Button>(R.id.btn_crear_torneo)
        btn_crear.setOnClickListener{
            abrirActividad(CrearTorneo::class.java)
        }
        email = intent.getStringExtra("email").toString()

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
        torneo: BTorneo
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        intentExplicito.putExtra("torneo",torneo)
        Log.i("Parametrs","onCreate ${torneo.nombre} - ${torneo.descripcion}")
        this.startActivity(intentExplicito);
    }
    fun abrirActividadIngresar(
        clase: Class<*>,
        email: String
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        intentExplicito.putExtra("email",email)
        this.startActivity(intentExplicito);
    }


    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position

        posiconElementoSeleccionado = id

        Log.i("list-view","onCreate ${id}")
        //Log.i("list-view","Usuario ${datos.mostrarTodoTorneo()}")

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var tounamentSelected = adaptador!!.getItem(posiconElementoSeleccionado)
        return when (item?.itemId) {
            R.id.miMapa -> {
                val intent = Intent(
                    this,
                    FMapsActivity::class.java
                )
                startActivity(intent)
                return true
            }
            R.id.miIngresar -> {
                if (tounamentSelected != null) {
                    Log.i("Parametrs","onCreate ${tounamentSelected.nombre} - ${tounamentSelected.descripcion}")
                    //val Anterior = datos.consultarTorneoPorId(tounamentSelected.id_torneo!!)
                    val Anterior = BTorneo(tounamentSelected.nombre,tounamentSelected.descripcion)
                    insertarJugador(tounamentSelected.nombre.toString(),email)
                    Toast.makeText(applicationContext,
                        "Ingresado con exito", Toast.LENGTH_LONG)
                        .show()
                }else{
                    Log.i("Parametrs","nulos ")
                }
                return true
            }
            R.id.miJugadores -> {
                if (tounamentSelected != null) {
                    Log.i("Parametrs","onCreate ${tounamentSelected.nombre} - ${tounamentSelected.descripcion}")
                    //val Anterior = datos.consultarTorneoPorId(tounamentSelected.id_torneo!!)
                    val Anterior = BTorneo(tounamentSelected.nombre,tounamentSelected.descripcion)
                    abrirActividadEdit(Jugadores::class.java,Anterior)
                }else{
                    Log.i("Parametrs","nulos ")
                }

                return true
            }
            // Editar
            R.id.miEditar -> {
                if (tounamentSelected != null) {
                    Log.i("Parametrs","onCreate ${tounamentSelected.nombre} - ${tounamentSelected.descripcion}")
                    //val Anterior = datos.consultarTorneoPorId(tounamentSelected.id_torneo!!)
                    val Anterior = BTorneo(tounamentSelected.nombre,tounamentSelected.descripcion)
                    abrirActividadEdit(CrearTorneo::class.java,Anterior)
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
                builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(){Dialog, which ->
                    try {
                        val db = Firebase.firestore
                        if (tounamentSelected != null) {
                            db.collection("torneoAjedrez")
                                .document(tounamentSelected.nombre.toString())
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
    fun consulta(){
        Log.i("consulta","se realiza consulta")
        val db = Firebase.firestore
        var torneosRef = db
            .collection("torneoAjedrez")
        torneosRef.get()
            .addOnSuccessListener {
                for( torenos in it){
                    Log.i("torneo","${torenos.data}")
                    torneos.add(BTorneo(torenos.get("torneo").toString(),torenos.get("descripcion").toString()))
                    Log.i("torneo","${torneos}")
                }
                adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    torneos
                )

                val vista=findViewById<ListView>(R.id.listTorneo)
                vista.adapter = adaptador
                registerForContextMenu(vista)
            }
            .addOnFailureListener {  }
    }
    fun insertarJugador(nombreTorneo:String,correoJugador:String){
        val db = Firebase.firestore
        db.collection("torneoAjedrez")
            .document(nombreTorneo)
            .update("jugadores", FieldValue.arrayUnion(correoJugador))
            .addOnSuccessListener { Log.i("DocumentSnapshot successfully updated!","yes") }
            .addOnFailureListener { e -> Log.i("Error updating document", "F") }
    }

}