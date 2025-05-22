package com.personal.requestmobility.dashboards.domain.interactors

import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio

class EliminarDashboardCU(private val repositorio: DashboardRepositorio) {
    suspend fun eliminar(dashboard: Dashboard) {
        repositorio.eliminar(dashboard)
    }
}