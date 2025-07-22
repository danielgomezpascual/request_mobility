package com.personal.metricas.dashboards.domain.interactors

import com.personal.metricas.dashboards.domain.entidades.Dashboard
import com.personal.metricas.dashboards.domain.repositorios.DashboardRepositorio
import kotlinx.coroutines.flow.Flow

class ObtenerDashboardsCU(private val repo: DashboardRepositorio) {
    suspend fun getAll(): Flow<List<Dashboard>> = repo.getAll()
}