package com.example.moviles_computacion_2021_b

import android.os.Parcel
import android.os.Parcelable

data class BTorneo(
    var nombre: String?,
    var descripcion: String?,
    val torneo: BTorneo? = null )
    : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(BJugador::class.java.classLoader)
    ) {
    }
    override fun toString(): String {
        return "${nombre} - ${descripcion}"
    }
    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(nombre)
        parcel?.writeString(descripcion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BTorneo> {
        override fun createFromParcel(parcel: Parcel): BTorneo {
            return BTorneo(parcel)
        }

        override fun newArray(size: Int): Array<BTorneo?> {
            return arrayOfNulls(size)
        }
    }

}