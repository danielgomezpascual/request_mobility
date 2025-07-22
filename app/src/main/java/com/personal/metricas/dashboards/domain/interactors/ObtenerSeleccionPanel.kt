package com.personal.metricas.dashboards.domain.interactors

import com.personal.metricas.paneles.domain.repositorios.PanelesRepositorio

class ObtenerSeleccionPanel(private val repoPaneles: PanelesRepositorio) {

    suspend fun obtenerTodos() = repoPaneles.getAll()
    
}
