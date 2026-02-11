package com.personal.metricas.core.domain.usecases

import com.personal.metricas.transacciones.domain.entidades.Transacciones
import com.personal.metricas.transacciones.domain.repositorios.IRepoTransacciones

class ObtenerDatosTransaccionCU(private val repoTransacciones: IRepoTransacciones) {

    suspend fun ejecutar(mobRequestId: String): Transacciones {
        val transacciones = repoTransacciones.obtenerTransacciones()
        return transacciones.firstOrNull { it.mobRequestId.toString() == mobRequestId }
                ?: throw Exception("Transacción no encontrada: $mobRequestId")
    }
}
