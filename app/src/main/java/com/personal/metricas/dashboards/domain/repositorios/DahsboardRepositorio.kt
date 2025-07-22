package com.personal.metricas.dashboards.domain.repositorios

import com.personal.metricas.dashboards.domain.entidades.Dashboard
import kotlinx.coroutines.flow.Flow

interface DashboardRepositorio {
    suspend fun getAllHome(): Flow<List<Dashboard>>
    suspend fun getAll(): Flow<List<Dashboard>>
    suspend fun eliminar(dashboard: Dashboard)
    suspend fun eliminar() // Para eliminar todos
    suspend fun guardar(dashboard: Dashboard): Long
    suspend fun obtener(id: Int): Dashboard
}
