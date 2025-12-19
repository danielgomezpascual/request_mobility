package com.personal.metricas.dashboards.domain.interactors

import com.personal.metricas.dashboards.domain.entidades.Dashboard
import com.personal.metricas.dashboards.domain.repositorios.DashboardRepositorio
import kotlinx.coroutines.flow.Flow

class ObtenerDashboardsAccesoDirectoCU(private val repo: DashboardRepositorio) {
    suspend fun execute(): Flow<List<Dashboard>> {
        return repo.getAllAccesoDirecto()
    }
}
