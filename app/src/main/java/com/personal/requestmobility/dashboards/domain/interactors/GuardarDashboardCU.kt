package com.personal.requestmobility.dashboards.domain.interactors

import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio

class GuardarDashboardCU(private val repo: DashboardRepositorio
) {
    suspend fun guardar(dashboard: Dashboard): Long {

        //todo: comprobar el funcionamiento del guardado del home

        repo.getAll().collect { list ->
            if (list.isNotEmpty()) {
                list.forEach { ds ->
                    repo.guardar(ds.copy(home = false))
                }
            }

        }
        val identificador = repo.guardar(dashboard) // El repositorio.guardar podría devolver un Long (id)
        return identificador

    }
}