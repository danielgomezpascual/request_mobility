package com.personal.requestmobility.dashboards.domain.interactors

import com.personal.requestmobility.dashboards.ui.entidades.SeleccionPanelUI
import com.personal.requestmobility.kpi.domain.repositorios.KpisRepositorio
import com.personal.requestmobility.paneles.domain.repositorios.PanelesRepositorio
import kotlinx.coroutines.flow.map

class ObtenerSeleccionPanel(private val repoPaneles: PanelesRepositorio) {

    suspend fun obtenerTodos() = repoPaneles.getAll()
    
}
