package com.personal.requestmobility.transacciones.ui.entidades

import androidx.compose.ui.graphics.Color



enum class EstadoProceso(val color: Color) {
    OK(Color.Green), // Verde
    ERROR(Color.Red), // Rojo
    REPROCESADO(Color.Blue), // Azul
    SIN_PROCESAR(Color.Gray), // Gris
    DESCONOCIDO(Color.Black); // Negro;


    companion object{
        fun fromReqStatus(valor: Int) =when (valor){
            0 ->  EstadoProceso.OK
            1 ->  EstadoProceso.ERROR
           2 ->  EstadoProceso.ERROR
            4 ->  EstadoProceso.REPROCESADO
            -1 ->  EstadoProceso.SIN_PROCESAR
            else ->    EstadoProceso.DESCONOCIDO
        }
    }
}


