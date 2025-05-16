package com.personal.requestmobility.kpi.data.ds

import com.personal.requestmobility.core.data.ds.IDS
import com.personal.requestmobility.kpi.domain.entidades.Kpi

interface IDataSourceKpis  : IDS{
    suspend fun getAll(): List<Kpi>
    suspend fun eliminar(kpi: Kpi)
    suspend fun eliminarTodas()
    suspend fun guardar(kpi: Kpi)  : Long
    suspend fun obtener(id: Int)  : Kpi
}