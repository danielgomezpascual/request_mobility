package com.personal.requestmobility.transacciones.domain.interactors

import com.personal.requestmobility.transacciones.domain.entidades.Transacciones
import com.personal.requestmobility.transacciones.domain.repositorios.IRepoTransacciones

class GuardarTransacciones (private val repoTransacciones : IRepoTransacciones) {

    suspend fun guardar(trxs: List<Transacciones>) = repoTransacciones.guardar(trxs)

}