package com.personal.requestmobility.dashboards.domain.interactors

import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio
import kotlinx.coroutines.flow.Flow

class ObtenerDashboardsCU(private val repo: DashboardRepositorio) {
    suspend fun getAll(): Flow<List<Dashboard>> = repo.getAll()
}