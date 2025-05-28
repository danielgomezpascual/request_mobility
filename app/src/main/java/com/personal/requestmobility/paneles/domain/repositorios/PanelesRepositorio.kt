package com.personal.requestmobility.paneles.domain.repositorios

import com.personal.requestmobility.kpi.domain.entidades.Kpi
import com.personal.requestmobility.paneles.domain.entidades.Panel
import kotlinx.coroutines.flow.Flow

interface PanelesRepositorio {
    suspend fun getAll(): Flow<List<Panel>>
    suspend fun eliminar(panel: Panel)
    suspend fun eliminarTodos()
    suspend fun guardar(panel: Panel): Long
    suspend fun obtener(id: Int): Panel

}