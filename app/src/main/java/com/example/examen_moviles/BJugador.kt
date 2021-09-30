package com.example.moviles_computacion_2021_b

import android.os.Parcel
import android.os.Parcelable

data class BJugador(
    var uid: String?,
    var email: String?,
    var nombre: String?,
    var elo: Int)
    :Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt())
    {
    }

    override fun toString(): String {
        return "${nombre} - ${elo}"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(uid)
        parcel?.writeString(email)
        parcel?.writeString(nombre)
        parcel?.writeInt(elo)
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