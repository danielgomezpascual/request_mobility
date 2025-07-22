package com.personal.metricas.paneles.data.ds

import com.personal.metricas.core.data.ds.IDS
import com.personal.metricas.paneles.domain.entidades.Panel

interface IDataSourcePaneles  : IDS{
    suspend fun getAll(): List<Panel>
    suspend fun eliminar(panel: Panel)
    suspend fun eliminarTodas()
    suspend fun guardar(panel: Panel)  : Long
    suspend fun obtener(id: Int)  : Panel
}