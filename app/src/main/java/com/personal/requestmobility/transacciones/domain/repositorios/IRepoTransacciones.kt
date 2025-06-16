package com.personal.requestmobility.transacciones.domain.repositorios

import com.personal.requestmobility.core.room.ResultadoEjecucionSQL
import com.personal.requestmobility.transacciones.domain.entidades.Transacciones

interface IRepoTransacciones {
    suspend fun ejecutarSQL(sql: String): ResultadoEjecucionSQL
    suspend fun obtenerTransacciones(): List<Transacciones>
    suspend fun getTrxOracle(organizacion: String): List<Transacciones>
    suspend fun guardar(trx: List<Transacciones>): Long
    suspend fun vaciarContenido()

}
