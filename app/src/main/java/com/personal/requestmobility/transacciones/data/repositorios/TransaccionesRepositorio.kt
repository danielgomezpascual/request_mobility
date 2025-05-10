package com.personal.requestmobility.transacciones.data.repositorios

import com.personal.requestmobility.core.room.ResultadoEjecucionSQL
import com.personal.requestmobility.transacciones.data.local.dao.TansaccionesDao
import com.personal.requestmobility.transacciones.data.local.entities.toTransacciones
import com.personal.requestmobility.transacciones.domain.entidades.Transacciones
import com.personal.requestmobility.transacciones.domain.repositorios.IRepoTransacciones

class TransaccionesRepositorio(private val dao: TansaccionesDao) : IRepoTransacciones {

    override suspend fun ejecutarSQL(sql: String): ResultadoEjecucionSQL = dao.sqlToListString(sql)

    override suspend fun obtenerTransacciones(): List<Transacciones> {
        return dao.todasTransacciones().map { it.toTransacciones() }
    }
}