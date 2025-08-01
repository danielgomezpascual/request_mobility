package com.personal.metricas.transacciones.data.repositorios

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.core.data.repositorio.BaseRepositorio
import com.personal.metricas.core.room.ResultadoEjecucionSQL
import com.personal.metricas.transacciones.data.ds.IDataSourceTransacciones
import com.personal.metricas.transacciones.domain.entidades.Transacciones
import com.personal.metricas.transacciones.domain.repositorios.IRepoTransacciones

class TransaccionesRepoImp (fuentesDatos: List<IDataSourceTransacciones>):
    BaseRepositorio<IDataSourceTransacciones>(fuentesDatos), IRepoTransacciones {


    override suspend fun ejecutarSQL(sql: String): ResultadoEjecucionSQL {
        TODO("Not yet implemented")
    }

    override suspend fun obtenerTransacciones(): List<Transacciones> {
        TODO("Not yet implemented")
    }

    override suspend fun getTrxOracle(organizacion: String): List<Transacciones>  = dameDS(TIPO_DS.RETROFIT).getAll(organizacion =  organizacion)

    override suspend fun guardar(trx: List<Transacciones>): Long  = dameDS(TIPO_DS.ROOM).guardar(trx)
    override suspend fun vaciarContenido() {
        TODO("Not yet implemented")
    }

     suspend fun eliminarTodo() = dameDS(TIPO_DS.ROOM).eliminarTodas()




}