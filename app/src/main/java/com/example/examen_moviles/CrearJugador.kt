package com.example.examen_moviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.moviles_computacion_2021_b.BJugador
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CrearJugador : AppCompatActivity() {
    var query: Query? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_jugador)
        val btn_crear_jugador = findViewById<Button>(R.id.creaJugador)
        val nombrejugador = findViewById<TextView>(R.id.editNombre)
        val elojugador = findViewById<TextView>(R.id.textElo)
        val jugadorAnterior = intent.getParcelableExtra<BJugador>("jugador")
        val spinner: Spinner = findViewById(R.id.spinnerNacionalidad)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.country_arrays,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        //val basedatos= ESqliteHelperUsuario(this)
        //val basedatos = Firebase.getInstance().getCurrentUser()
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
                    actualizarJugador(jugadorAnterior.nombre.toString())
                }else{
                    crearJugador()
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

    fun crearJugador() {
        val nombrejugador = findViewById<TextView>(R.id.editNombre)
        val elojugador = findViewById<TextView>(R.id.textElo)
        val spinner: Spinner = findViewById(R.id.spinnerNacionalidad)
        val nuevoJugador = hashMapOf<String, Any>(
            "nombre" to nombrejugador.text.toString(),
            "nacionalidad" to spinner.selectedItem.toString(),
            "elo" to elojugador.text.toString()
        )
        val db = Firebase.firestore
        val referencia = db.collection("proyectoAjedrez")
            .document(nombrejugador.text.toString())
            .set(nuevoJugador)
            .addOnSuccessListener {  }
            .addOnFailureListener { }

    }
    fun actualizarJugador(jugadorAnterior: String) {
        val nombrejugador = findViewById<TextView>(R.id.editNombre)
        val elojugador = findViewById<TextView>(R.id.textElo)
        val spinner: Spinner = findViewById(R.id.spinnerNacionalidad)
        val actualizarJugador = hashMapOf<String, Any>(
            "nombre" to nombrejugador.text.toString(),
            "nacionalidad" to spinner.selectedItem.toString(),
            "elo" to elojugador.text.toString()
        )
        val db = Firebase.firestore
        if (jugadorAnterior != null) {
            db.collection("proyectoAjedrez")
                .document(jugadorAnterior)
                .delete()
                .addOnSuccessListener {
                    Log.i("Actualizar Jugador", "se acutalizo jugador")
                }
                .addOnFailureListener { }
        }
        crearJugador()

    }
}