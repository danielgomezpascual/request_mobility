package com.personal.metricas.dashboards.domain.interactors

import com.personal.metricas.dashboards.domain.entidades.Dashboard
import com.personal.metricas.dashboards.domain.repositorios.DashboardRepositorio

class ObtenerDashboardCU(private val repo: DashboardRepositorio) {

    suspend fun getID(id: Int ) : Dashboard = repo.obtener(id)



}