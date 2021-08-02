package com.example.moviles_computacion_2021_b

import android.os.Parcel
import android.os.Parcelable

class BTorneo(
    var id_torneo: Int?,
    var nombre: String?,
    var descripcion: String?,
    var torneo: BTorneo? = null
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(BTorneo::class.java.classLoader)
    ) {
    }
    override fun toString(): String {
        return "${nombre} - ${descripcion}"
    }
    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeInt(id_torneo!!)
        parcel?.writeString(nombre)
        parcel?.writeString(descripcion)
        parcel?.writeParcelable(torneo, flags)
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