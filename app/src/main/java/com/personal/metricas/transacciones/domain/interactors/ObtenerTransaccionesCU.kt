package com.personal.metricas.transacciones.domain.interactors

import com.personal.metricas.transacciones.domain.entidades.Transacciones
import com.personal.metricas.transacciones.domain.repositorios.IRepoTransacciones

class ObtenerTransaccionesCU(
    private val repositorioTransacciones: IRepoTransacciones,
) {

    suspend fun getTransacciones(): List<Transacciones> {
        val transacciones =  repositorioTransacciones.obtenerTransacciones()
        return transacciones
    }
}


