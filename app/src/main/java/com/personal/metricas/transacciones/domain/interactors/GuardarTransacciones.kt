package com.personal.metricas.transacciones.domain.interactors

import com.personal.metricas.transacciones.domain.entidades.Transacciones
import com.personal.metricas.transacciones.domain.repositorios.IRepoTransacciones

class GuardarTransacciones (private val repoTransacciones : IRepoTransacciones) {

    suspend fun guardar(trxs: List<Transacciones>) = repoTransacciones.guardar(trxs)

}