package com.example.moviles_computacion_2021_b

class BaseDatos {
    companion object {
        val torneos = arrayListOf<BTorneo>()
        init {
            torneos
                .add(BTorneo(1,"Mundial", "2:00h,30seg"))
            torneos
                .add(BTorneo(2,"GranPrix", "1:30h,15seg"))
            torneos
                .add(BTorneo(3,"Libre", "0:03h,2seg"))

        }
    }
}