package com.personal.requestmobility.paneles.data.ds

import com.personal.requestmobility.core.data.ds.IDS
import com.personal.requestmobility.kpi.domain.entidades.Kpi
import com.personal.requestmobility.paneles.domain.entidades.Panel

interface IDataSourcePaneles  : IDS{
    suspend fun getAll(): List<Panel>
    suspend fun eliminar(panel: Panel)
    suspend fun eliminarTodas()
    suspend fun guardar(panel: Panel)  : Long
    suspend fun obtener(id: Int)  : Panel
}