package com.example.moviles_computacion_2021_b

import android.os.Parcel
import android.os.Parcelable

class BJugador (
    var id_jugador:Int?,
    var nombre: String?,
    var elo: Int,
    var id_torneo:Int?,
    val jugador: BJugador? = null )
    :Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(BJugador::class.java.classLoader)
    ) {
    }

    override fun toString(): String {
        if (id_torneo!=-1){
            return "${id_jugador} - ${nombre} - ${elo} - ${id_torneo}"
        }else {
            return "${id_jugador} - ${nombre} - ${elo}"
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeInt(id_jugador!!)
        parcel?.writeString(nombre)
        parcel?.writeInt(elo)
        parcel?.writeInt(id_torneo!!)
        parcel?.writeParcelable(jugador, flags)
    }

    companion object CREATOR : Parcelable.Creator<BJugador> {
        override fun createFromParcel(parcel: Parcel): BJugador {
            return BJugador(parcel)
        }


        override fun newArray(size: Int): Array<BJugador?> {
            return arrayOfNulls(size)
        }
    }

}