package com.example.examen_moviles

import android.content.DialogInterface
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import com.example.moviles_computacion_2021_b.BTorneo
import com.example.moviles_computacion_2021_b.ESqliteHelperUsuario


class IntTorneo : AppCompatActivity() {
    var posiconElementoSeleccionado = 0
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 400
    val datos=ESqliteHelperUsuario(this)
    var adaptador: ArrayAdapter<BTorneo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_int_torneo)
        val torneos=datos.mostrarTodoTorneo()
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            torneos
        )
        val vista=findViewById<ListView>(R.id.list_jugador)
        vista.adapter = adaptador

        val btn_crear = findViewById<Button>(R.id.btn_crear_torneo)
        btn_crear.setOnClickListener{
            abrirActividad(CrearTorneo::class.java)
        }
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
        Log.i("list-view","Usuario ${datos.mostrarTodoTorneo()}")

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var tounamentSelected = adaptador!!.getItem(posiconElementoSeleccionado)
        return when (item?.itemId) {
            R.id.miIngresar -> {
                if (tounamentSelected != null) {
                    Log.i("Parametrs","onCreate ${tounamentSelected.nombre} - ${tounamentSelected.descripcion}")
                    val Anterior = datos.consultarTorneoPorId(tounamentSelected.id_torneo!!)
                    abrirActividadEdit(anadirJugador::class.java,Anterior)
                }else{
                    Log.i("Parametrs","nulos ")
                }
                return true
            }
            R.id.miJugadores -> {
                if (tounamentSelected != null) {
                    Log.i("Parametrs","onCreate ${tounamentSelected.nombre} - ${tounamentSelected.descripcion}")
                    val Anterior = datos.consultarTorneoPorId(tounamentSelected.id_torneo!!)
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
                    val Anterior = datos.consultarTorneoPorId(tounamentSelected.id_torneo!!)
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
                        val dato = adaptador!!.getItem(posiconElementoSeleccionado)
                        if (dato != null) {
                            datos.eliminarTorneoFormulario(dato.id_torneo!!)
                            adaptador?.remove(datos.consultarTorneoPorId(dato.id_torneo!!))
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