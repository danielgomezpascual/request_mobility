package com.personal.requestmobility.transacciones.data.repositorios

import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.core.room.ResultadoEjecucionSQL
import com.personal.requestmobility.transacciones.data.ds.local.dao.TansaccionesDao
import com.personal.requestmobility.transacciones.data.ds.local.entities.toTransacciones
import com.personal.requestmobility.transacciones.domain.entidades.ResultadoSQL
import com.personal.requestmobility.transacciones.domain.entidades.Transacciones
import com.personal.requestmobility.transacciones.domain.repositorios.IRepoTransacciones

import org.koin.mp.KoinPlatform.getKoin

class TransaccionesRepositorio(private val dao: TansaccionesDao) : IRepoTransacciones {

    override suspend fun ejecutarSQL(sql: String): ResultadoEjecucionSQL = dao.sqlToListString(sql)

    override suspend fun obtenerTransacciones(): List<Transacciones> {
        return dao.todasTransacciones().map { it.toTransacciones() }
    }
}


fun SqlToListString(sql : String): ResultadoSQL{
    val trxDao = getKoin().get<AppDatabase>().transaccionesDao()

    val lista =  trxDao.sqlToListString(sql)
    return ResultadoSQL.Companion.from(trxDao.sqlToListString(sql))
}