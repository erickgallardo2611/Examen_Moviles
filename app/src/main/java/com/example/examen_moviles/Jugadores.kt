package com.example.examen_moviles

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.moviles_computacion_2021_b.BJugador
import com.example.moviles_computacion_2021_b.BTorneo
import com.example.moviles_computacion_2021_b.ESqliteHelperUsuario

class Jugadores : AppCompatActivity() {
    var posiconElementoSeleccionado = 0
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 400
    val datos = ESqliteHelperUsuario(this)
    var adaptador: ArrayAdapter<BJugador>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jugadores)
        var jugadores=datos.mostrarTodoJugador()
        val torneoSelect = intent.getParcelableExtra<BTorneo>("torneo")

        var btn_crear = findViewById<Button>(R.id.btn_crear_jugador)
        btn_crear.setOnClickListener{
            abrirActividad(CrearJugador::class.java)
        }
        if(torneoSelect!=null) {
            btn_crear.setVisibility(View.GONE)
            jugadores = datos.mostrarTodoJugadorTorneo(torneoSelect.id_torneo!!)
            Log.i("ID_del torneo","${torneoSelect} ${torneoSelect.id_torneo}")
            adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                jugadores
            )
        }else{
            adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                jugadores
            )
        }

        Log.i("Parametrs","onCreate ${jugadores}")
        val vista=findViewById<ListView>(R.id.list_jugador)
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
        Log.i("list-view","Usuario ${datos.mostrarTodoJugador()}")

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var jugadorSelected = adaptador!!.getItem(posiconElementoSeleccionado)
        return when (item?.itemId) {
            // Editar
            R.id.miEditar -> {
                if (jugadorSelected != null) {
                    Log.i("Parametrs","onCreate ${jugadorSelected.nombre} - ${jugadorSelected.elo}")
                    val Anterior = datos.consultarJugadorPorId(jugadorSelected.id_jugador!!)
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
                        val dato = adaptador!!.getItem(posiconElementoSeleccionado)
                        if (dato != null) {
                            datos.eliminarJugadorFormulario(dato.id_jugador!!)
                            adaptador?.remove(datos.consultarJugadorPorId(dato.id_torneo!!))
                            abrirActividad(MainActivity::class.java)
                        }
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
}