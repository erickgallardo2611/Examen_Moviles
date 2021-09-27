package com.example.moviles_computacion_2021_b

import android.os.Parcel
import android.os.Parcelable

data class BJugador (
    var nombre: String?,
    var elo: Int,
    var nacionalidad:String?,
    val jugador: BJugador? = null )
    :Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readParcelable(BJugador::class.java.classLoader)
    ) {
    }

    override fun toString(): String {
        return "${nombre} - ${elo} - ${nacionalidad}"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(nombre)
        parcel?.writeInt(elo)
        parcel?.writeString(nacionalidad)
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