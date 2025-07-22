package com.personal.metricas.transacciones.data.ds.local

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.core.room.ResultadoEjecucionSQL
import com.personal.metricas.transacciones.data.ds.IDataSourceTransacciones
import com.personal.metricas.transacciones.data.ds.local.dao.TansaccionesDao
import com.personal.metricas.transacciones.data.ds.local.entities.TransaccionesRoom
import com.personal.metricas.transacciones.data.ds.local.entities.fromTransaccion
import com.personal.metricas.transacciones.data.ds.local.entities.toTransacciones
import com.personal.metricas.transacciones.domain.entidades.Transacciones
import com.personal.metricas.transacciones.domain.repositorios.IRepoTransacciones

class TransaccionesLocalDS(private val dao: TansaccionesDao) : IRepoTransacciones , IDataSourceTransacciones{

    override val tipo: TIPO_DS
        get() = TIPO_DS.ROOM


    override suspend fun ejecutarSQL(sql: String): ResultadoEjecucionSQL
            = dao.sqlToListString(sql)

    override suspend fun obtenerTransacciones(): List<Transacciones> {
        return dao.todasTransacciones().map { it.toTransacciones() }
    }


    override suspend fun getAll(organizacion: String): List<Transacciones> {
        TODO("Not yet implemented")
    }

    override suspend fun eliminar(transaccion: Transacciones) {
        TODO("Not yet implemented")
    }

    override suspend fun eliminarTodas()    = dao.vaciarTabla()
  

    override suspend fun guardar(transaccion: Transacciones): Long {
        TODO("Not yet implemented")
    }


    override suspend fun obtener(id: Int): Transacciones {
        TODO("Not yet implemented")
    }

    override suspend fun getTrxOracle(organizacion: String): List<Transacciones> {
        TODO("Not yet implemented")
    }

    override suspend fun guardar(trx: List<Transacciones>): Long {
       dao.insert(trx.map { TransaccionesRoom(MOB_REQUEST_ID = 0 ).fromTransaccion(it) })
        return 1L
    }

    override suspend fun vaciarContenido() {
        dao.vaciarTabla()
    }


}

