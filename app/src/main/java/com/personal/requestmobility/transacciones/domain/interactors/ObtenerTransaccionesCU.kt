package com.personal.requestmobility.transacciones.domain.interactors

import com.personal.requestmobility.App
import com.personal.requestmobility.transacciones.domain.entidades.Transacciones
import com.personal.requestmobility.transacciones.domain.repositorios.IRepoTransacciones

class ObtenerTransaccionesCU(
    private val repositorioTransacciones: IRepoTransacciones,
) {

    suspend fun getTransacciones(): List<Transacciones> {
        val transacciones =  repositorioTransacciones.obtenerTransacciones()
        return transacciones
    }
}


