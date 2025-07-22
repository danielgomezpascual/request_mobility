package com.personal.metricas.dashboards.domain.interactors

import com.personal.metricas.dashboards.domain.entidades.Dashboard
import com.personal.metricas.dashboards.domain.repositorios.DashboardRepositorio

class EliminarDashboardCU(private val repositorio: DashboardRepositorio) {
    suspend fun eliminar(dashboard: Dashboard) {
        repositorio.eliminar(dashboard)
    }
}