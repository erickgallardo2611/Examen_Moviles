package com.example.examen_moviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.moviles_computacion_2021_b.BTorneo
import com.example.moviles_computacion_2021_b.EBaseDeDatos
import com.example.moviles_computacion_2021_b.ESqliteHelperUsuario
import org.w3c.dom.Text

class CrearTorneo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_torneo)
        val torneoAnterior = intent.getParcelableExtra<BTorneo>("torneo")

        var btn_crear = findViewById<Button>(R.id.crear_torneo)
        val torneo = findViewById<TextView>(R.id.edit_torneo)
        val descripcion = findViewById<TextView>(R.id.edit_torneo2)
        val basedatos=ESqliteHelperUsuario(this)
        torneo.editableText
        descripcion.editableText
        if(torneoAnterior!=null){
            Log.i("Pase","${torneoAnterior.nombre}")
            torneo.setText(torneoAnterior.nombre)
            descripcion.setText(torneoAnterior.descripcion)
            btn_crear.setText("Actualizar")
        }else{
            Log.i("Pase","no hay no existe :v")
        }
        btn_crear.setOnClickListener{
            if(torneo.text.toString()=="Ingrese el nombre del torneo" || descripcion.text.toString()=="Ingrese la descripcion del torneo" || torneo.text.toString()==""||descripcion.text.toString()==""){
                Log.i("bdd", "No esta lleno")
            }else{
                Log.i("bdd", "Esta lleno")
                if(torneoAnterior!=null){
                    val actualizado = basedatos.actualizarTorneoFormulario(torneo.text.toString(),descripcion.text.toString(),torneoAnterior.id_torneo!!)
                    Log.i("bdd", "Esta lleno ${actualizado} ${torneoAnterior.nombre}")
                }else{
                    if (basedatos != null) {
                        basedatos.crearTorneoFormulario(
                                torneo.text.toString(),
                                descripcion.text.toString()
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