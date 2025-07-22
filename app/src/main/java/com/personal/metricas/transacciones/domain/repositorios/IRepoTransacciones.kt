package com.personal.metricas.transacciones.domain.repositorios

import com.personal.metricas.core.room.ResultadoEjecucionSQL
import com.personal.metricas.transacciones.domain.entidades.Transacciones

interface IRepoTransacciones {
    suspend fun ejecutarSQL(sql: String): ResultadoEjecucionSQL
    suspend fun obtenerTransacciones(): List<Transacciones>
    suspend fun getTrxOracle(organizacion: String): List<Transacciones>
    suspend fun guardar(trx: List<Transacciones>): Long
    suspend fun vaciarContenido()

}
