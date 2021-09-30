package com.example.examen_moviles

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.moviles_computacion_2021_b.BJugador
import com.example.moviles_computacion_2021_b.BTorneo
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Parser


class MainActivity : AppCompatActivity() {
    val CODIGO_INICIO_SESION = 102
    var email:String=""
    var nombreJugador:String=""
    var eloJugador:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val usuaioPosible = intent.getStringExtra("email")
        val button_torneo = findViewById<Button>(R.id.button_torneo)
        button_torneo.setOnClickListener{
            abrirActividadParametro(IntTorneo::class.java,email)
        }
        val button_Jugador = findViewById<Button>(R.id.button_participante)
        button_Jugador.setOnClickListener{
            abrirActividad(Jugadores::class.java)
        }
        val botonLogin = findViewById<Button>(R.id.btn_login)
        botonLogin.setOnClickListener {
            llamarLoginUsuario()
        }
        val botonLogout = findViewById<Button>(R.id.btn_logout)
        botonLogout.setOnClickListener {
            solicitarSalirDelAplicativo()
        }
        val botonPartida = findViewById<Button>(R.id.btn_Partida)
        botonPartida.setOnClickListener {
            abrirActividadJugar(JugarPartida::class.java,nombreJugador,eloJugador)
        }
    }

    fun llamarLoginUsuario() {
        val providers = arrayListOf(
            // lista de los proveedores
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html"
                )
                .build(),
            CODIGO_INICIO_SESION
        )
    }
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CODIGO_INICIO_SESION -> {
                if (resultCode == Activity.RESULT_OK) {
                    val usuario: IdpResponse? = IdpResponse.fromResultIntent(data)
                    if (usuario != null) {
                        if (usuario.isNewUser == true) {
                            Log.i("firebase-login", "Nuevo Usuario")
                            registrarUsuarioPorPrimeraVez(usuario)
                        } else {
                            Log.i("usuario xdd","${usuario}")
                            setearUsuarioFirebase()
                            Log.i("firebase-login", "Usuario Antiguo")
                        }
                    }
                } else {
                    Log.i("firebase-login", "El usuario cancelo")
                }
            }
        }
    }
    fun setearUsuarioFirebase() {
        val instanciaAuth = FirebaseAuth.getInstance()
        val usuarioLocal = instanciaAuth.currentUser
        if (usuarioLocal != null) {
            if (usuarioLocal.email != null) {

                val db = Firebase.firestore

                val referencia = db
                    .collection("proyectoAjedrez")
                    .document(usuarioLocal.email.toString()) // /usuario/a@...com

                referencia
                    .get()
                    .addOnSuccessListener {
                        val usuarioCargado = BJugador(it.get("uid").toString(),it.get("email").toString(),it.get("nombre").toString(),Integer.parseInt(it.get("elo").toString()))
                        BAuthUsuario.usuario = BJugador(
                            usuarioCargado.uid,
                            usuarioCargado.email,
                            usuarioCargado.nombre,
                            usuarioCargado.elo)
                        setearBienvenida()
                        email = usuarioCargado.email.toString()
                        nombreJugador = usuarioCargado.nombre.toString()
                        eloJugador = usuarioCargado.elo.toString()
                        BAuthUsuario.usuario = usuarioCargado
                        Log.i("firebase-firestore", "Usuario cargado")
                    }
                    .addOnFailureListener {
                        Log.i("firebase-firestore", "Fallo cargar usuario")
                    }
            }
        }
    }
    @SuppressLint("RestrictedApi")
    fun registrarUsuarioPorPrimeraVez(usuario: IdpResponse) {
        val usuarioLogeado = FirebaseAuth
            .getInstance()
            .getCurrentUser()
        if (usuario.email != null && usuarioLogeado != null) {

            // roles : ["usuario", "admin"]
            val db = Firebase.firestore // obtenemos referencia

            val nombre = usuario.user.name // creamos el arreglo de permisos

            val identificadorUsuario = usuario.email // nada

            val nuevoUsuario = hashMapOf<String, Any>( // { roles:... uid:...}
                "uid" to usuarioLogeado.uid,
                "email" to identificadorUsuario.toString(),
                "nombre" to nombre.toString(),
                "elo" to 1500
            )

            db.collection("proyectoAjedrez") // /usuario/XZY/{roles:... uid:...}
                // Forma a) Firestore crea identificador
                // .add(nuevoUsuario)
                // Forma b) Yo seteo el identificador
                .document(identificadorUsuario.toString())
                .set(nuevoUsuario)
                .addOnSuccessListener {
                    Log.i("firebase-firestore", "Se creo")
                    setearUsuarioFirebase()
                }
                .addOnFailureListener {
                    Log.i("firebase-firestore", "Fallo")
                }

        } else {
            Log.i("firebase-login", "ERROR")
        }
    }



    fun setearBienvenida(){
        val botonLogin = findViewById<Button>(R.id.btn_login)
        val botonLogout = findViewById<Button>(R.id.btn_logout)
        val botonProducto = findViewById<Button>(R.id.button_torneo)
        val botonRestaurante = findViewById<Button>(R.id.button_participante)
        val nombreJugador = findViewById<TextView>(R.id.txtNombreUsuaio)
        val botonJugarPartida = findViewById<Button>(R.id.btn_Partida)
        Log.i("usuario xdd","${BAuthUsuario.usuario}")
        if(BAuthUsuario.usuario != null){
            botonLogin.visibility = View.INVISIBLE
            botonLogout.visibility = View.VISIBLE
            botonProducto.visibility = View.VISIBLE
            botonRestaurante.visibility = View.VISIBLE
            nombreJugador.visibility = View.VISIBLE
            botonJugarPartida.visibility = View.VISIBLE
            nombreJugador.setText(BAuthUsuario.usuario!!.nombre)
        }else{
            botonLogin.visibility = View.VISIBLE
            botonLogout.visibility = View.INVISIBLE
            botonProducto.visibility = View.INVISIBLE
            botonRestaurante.visibility = View.VISIBLE
            nombreJugador.visibility = View.INVISIBLE
            botonJugarPartida.visibility = View.INVISIBLE
        }
    }

    fun solicitarSalirDelAplicativo(){
        AuthUI
            .getInstance()
            .signOut(this)
            .addOnCompleteListener {
                BAuthUsuario.usuario = null
                setearBienvenida()
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
    fun abrirActividadParametro(
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

    fun abrirActividadJugar(
        clase: Class<*>,
        nombre: String,
        elo: String
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        intentExplicito.putExtra("nombre",nombre)
        intentExplicito.putExtra("elo",elo)
        Log.i("datos","${elo},${nombre}")
        this.startActivity(intentExplicito);
    }
    fun abrirTorneo(
        clase: Class<*>,
        correo: String
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        intentExplicito.putExtra("jugador",correo)
        this.startActivity(intentExplicito);
    }


}