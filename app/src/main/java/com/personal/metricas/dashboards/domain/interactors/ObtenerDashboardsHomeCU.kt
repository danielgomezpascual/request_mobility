package com.personal.metricas.dashboards.domain.interactors

import com.personal.metricas.dashboards.domain.entidades.Dashboard
import com.personal.metricas.dashboards.domain.repositorios.DashboardRepositorio
import kotlinx.coroutines.flow.Flow

class ObtenerDashboardsHomeCU(private val repo: DashboardRepositorio) {
    suspend fun getAllHome(): Flow<List<Dashboard>> = repo.getAllHome()
}