package com.example.moviles_computacion_2021_b

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ESqliteHelperUsuario(
    context: Context?,
) : SQLiteOpenHelper(
    context,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {

        var scriptCrear = """ 
            CREATE TABLE TORNEO (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                nombre VARCHAR(50),
                descripcion  VARCHAR(50)
            )            
        """.trimIndent()
        Log.i("bdd", "Creando la tabla de usuario")

        db?.execSQL(scriptCrear)

        scriptCrear = """ 
        CREATE TABLE JUGADOR (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre VARCHAR(50),
            elo  INTEGER, 
            id_Torneo INTEGER
         )       
        """.trimIndent()
        Log.i("bdd", "Creando la tabla de usuario")

        db?.execSQL(scriptCrear)

    }

    fun crearTorneoFormulario(
        nombre: String,
        descripcion: String
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("descripcion", descripcion)
        val resultadoEscritura: Long = conexionEscritura
            .insert(
                "TORNEO",
                null,
                valoresAGuardar
            )
        conexionEscritura.close()
        return if (resultadoEscritura.toInt() == -1) false else true
    }


    fun crearJugadorFormulario(
        nombre: String
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("elo", 1500)
        valoresAGuardar.put("id_torneo", -1)
        val resultadoEscritura: Long = conexionEscritura
            .insert(
                "JUGADOR",
                null,
                valoresAGuardar
            )
        conexionEscritura.close()
        return if (resultadoEscritura.toInt() == -1) false else true
    }


    fun consultarJugadorPorId(id: Int): BJugador {
        val scriptConsultarUsuario = "SELECT * FROM JUGADOR WHERE ID = ${id}"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        //val arregloUsuario = arrayListOf<EUsuarioBDD>()       //En caso de3 necesitar un arreglo de registros
        val jugadorEncontrado = BJugador(0, "", 0,-1)
        if (existeUsuario) {
            do {
                val id = resultadoConsultaLectura.getInt(0) //Columna indice 0 -> ID
                val nombre = resultadoConsultaLectura.getString(1) //Columna indice 1 -> NOMBRE
                val elo = resultadoConsultaLectura.getInt(2)
                val id_torneo = resultadoConsultaLectura.getInt(3)

                if (id != null) {
                    jugadorEncontrado.id_jugador = id
                    jugadorEncontrado.nombre = nombre
                    jugadorEncontrado.elo = elo
                    jugadorEncontrado.id_torneo = id_torneo
                    //arregloUsuario.add(usuarioEncontrado)
                }
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()

        return jugadorEncontrado
    }



    fun eliminarJugadorFormulario(id: Int): Boolean {

        //val conexionEscritura = this.writableDatabase
        val conexionEscritura = writableDatabase
        var resultadoEliminacion = conexionEscritura
            .delete(
                "JUGADOR",
                "id=?",
                arrayOf(id.toString())
            )
        conexionEscritura.close()
        return if (resultadoEliminacion.toInt() == -1) false else true
        //return resultadoEliminacion.toInt() != -1
    }

    fun actualizarJugadorFormulario(
        nombre: String,
        idActualizar: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        val resultadoActualización = conexionEscritura
            .update(
                "JUGADOR",
                valoresAActualizar,
                "id=?",
                arrayOf(
                    idActualizar.toString()
                )
            )
        conexionEscritura.close()
        return if (resultadoActualización.toInt() == -1) false else true
    }


    fun actualizarJugadorTorneoFormulario(
        idActualizar: Int,
        id: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("id_torneo", idActualizar)
        val resultadoActualización = conexionEscritura
            .update(
                "JUGADOR",
                valoresAActualizar,
                "id=?",
                arrayOf(
                    id.toString()
                )
            )
        conexionEscritura.close()
        return if (resultadoActualización.toInt() == -1) false else true
    }



    fun actualizarTorneoFormulario(
        nombre: String,
        descripcion: String,
        idActualizar: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("descripcion", descripcion)
        val resultadoActualización = conexionEscritura
            .update(
                "TORNEO",
                valoresAActualizar,
                "id=?",
                arrayOf(
                    idActualizar.toString()
                )
            )
        conexionEscritura.close()
        return if (resultadoActualización.toInt() == -1) false else true
    }

    fun mostrarTodoTorneo():ArrayList<BTorneo>{
        val torneo = ArrayList<BTorneo>()
        val scriptConsultarUsuario = "SELECT * FROM TORNEO"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )
        while(resultadoConsultaLectura.moveToNext()){
            val id = resultadoConsultaLectura.getInt(0)
            val nombre = resultadoConsultaLectura.getString(1)
            val descripcion = resultadoConsultaLectura.getString(2)
            torneo.add(BTorneo(id,nombre,descripcion))
        }
        baseDatosLectura.close()
        Log.i("bdd", resultadoConsultaLectura.toString())

     return torneo
    }

    fun mostrarTodoJugador():ArrayList<BJugador>{
        val jugador = ArrayList<BJugador>()
        val scriptConsultarUsuario = "SELECT * FROM JUGADOR"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )
        while(resultadoConsultaLectura.moveToNext()){
            val id = resultadoConsultaLectura.getInt(0)
            val nombre = resultadoConsultaLectura.getString(1)
            val elo = resultadoConsultaLectura.getInt(2)
            val id_torneo = resultadoConsultaLectura.getInt(3)
            jugador.add(BJugador(id,nombre,elo,id_torneo))
        }
        baseDatosLectura.close()
        Log.i("bdd", resultadoConsultaLectura.toString())

        return jugador
    }
    fun mostrarTodoJugadorTorneo(id_torneo: Int):ArrayList<BJugador>{
        val jugador = ArrayList<BJugador>()
        val scriptConsultarUsuario = "SELECT * FROM JUGADOR WHERE ID_TORNEO=${id_torneo}"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )
        while(resultadoConsultaLectura.moveToNext()){
            val id = resultadoConsultaLectura.getInt(0)
            val nombre = resultadoConsultaLectura.getString(1)
            val elo = resultadoConsultaLectura.getInt(2)
            val id_torneo = resultadoConsultaLectura.getInt(3)
            jugador.add(BJugador(id,nombre,elo,id_torneo))
        }
        baseDatosLectura.close()
        Log.i("bdd", resultadoConsultaLectura.toString())

        return jugador
    }


    fun mostrarMenosJugadorTorneo():ArrayList<BJugador>{
        val jugador = ArrayList<BJugador>()
        val scriptConsultarUsuario = "SELECT * FROM JUGADOR WHERE ID_TORNEO=-1"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )
        while(resultadoConsultaLectura.moveToNext()){
            val id = resultadoConsultaLectura.getInt(0)
            val nombre = resultadoConsultaLectura.getString(1)
            val elo = resultadoConsultaLectura.getInt(2)
            val id_torneo = resultadoConsultaLectura.getInt(3)
            jugador.add(BJugador(id,nombre,elo,id_torneo))
        }
        baseDatosLectura.close()
        Log.i("bdd", resultadoConsultaLectura.toString())

        return jugador
    }

    fun consultarTorneoPorId(id: Int): BTorneo {
        val scriptConsultarUsuario = "SELECT * FROM TORNEO WHERE ID = ${id}"
        val baseDatosLectura = readableDatabase
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        //val arregloUsuario = arrayListOf<EUsuarioBDD>()       //En caso de3 necesitar un arreglo de registros
        val torneo = BTorneo(0, "", "")
        if (existeUsuario) {
            do {
                val id = resultadoConsultaLectura.getInt(0) //Columna indice 0 -> ID
                val nombre = resultadoConsultaLectura.getString(1) //Columna indice 1 -> NOMBRE
                val elo = resultadoConsultaLectura.getString(2)

                if (id != null) {
                    torneo.id_torneo = id
                    torneo.nombre = nombre
                    torneo.descripcion = elo
                    //arregloUsuario.add(usuarioEncontrado)
                }
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()

        return torneo
    }


    fun eliminarTorneoFormulario(id: Int): Boolean {

        //val conexionEscritura = this.writableDatabase
        val conexionEscritura = writableDatabase
        var resultadoEliminacion = conexionEscritura
            .delete(
                "TORNEO",
                "id=?",
                arrayOf(id.toString())
            )
        conexionEscritura.close()
        return if (resultadoEliminacion.toInt() == -1) false else true
        //return resultadoEliminacion.toInt() != -1
    }





    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }


}