package com.personal.metricas.paneles.domain.interactors

import com.personal.metricas.paneles.domain.repositorios.PanelesRepositorio
import com.personal.metricas.paneles.ui.entidades.PanelUI
import com.personal.metricas.paneles.ui.entidades.toPanel

class EliminarPanelCU(private val repositorio: PanelesRepositorio) {
    suspend fun eliminar(panelUI: PanelUI) {
        repositorio.eliminar(panelUI.toPanel())
    }
}