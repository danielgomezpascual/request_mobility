package com.personal.metricas.transacciones.data.ds

import com.personal.metricas.core.data.ds.IDS
import com.personal.metricas.transacciones.domain.entidades.Transacciones

interface IDataSourceTransacciones: IDS {

    suspend fun getAll(organizacion: String): List<Transacciones>
    suspend fun eliminar(transaccion: Transacciones)
    suspend fun eliminarTodas()
    suspend fun guardar(transaccion: Transacciones)  : Long
    suspend fun guardar(trx: List<Transacciones>)  : Long
    suspend fun obtener(id: Int)  : Transacciones
    
}