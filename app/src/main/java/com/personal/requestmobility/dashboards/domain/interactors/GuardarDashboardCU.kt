package com.personal.requestmobility.dashboards.domain.interactors

import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio

class GuardarDashboardCU(private val repo: DashboardRepositorio) {
    suspend fun guardar(dashboard: Dashboard) {
        repo.guardar(dashboard) // El repositorio.guardar podría devolver un Long (id)
    }
}