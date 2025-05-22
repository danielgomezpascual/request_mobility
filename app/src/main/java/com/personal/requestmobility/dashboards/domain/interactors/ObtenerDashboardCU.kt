package com.personal.requestmobility.dashboards.domain.interactors

import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio
import kotlinx.coroutines.flow.Flow

class ObtenerDashboardCU(private val repo: DashboardRepositorio) {
    suspend fun getAll(): Flow<List<Dashboard>> = repo.getAll()
}