package com.personal.requestmobility.dashboards.domain.interactors

import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio

class EliminarTodosDashboardsCU(private val repositorio: DashboardRepositorio) {
    suspend fun eliminarTodos() {
        repositorio.eliminar()
    }
}