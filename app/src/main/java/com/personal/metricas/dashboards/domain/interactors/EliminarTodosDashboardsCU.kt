package com.personal.metricas.dashboards.domain.interactors

import com.personal.metricas.dashboards.domain.repositorios.DashboardRepositorio

class EliminarTodosDashboardsCU(private val repositorio: DashboardRepositorio) {
    suspend fun eliminarTodos() {
        repositorio.eliminar()
    }
}