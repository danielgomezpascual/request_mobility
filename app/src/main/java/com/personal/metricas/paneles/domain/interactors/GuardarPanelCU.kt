package com.personal.metricas.paneles.domain.interactors

import com.personal.metricas.paneles.domain.repositorios.PanelesRepositorio
import com.personal.metricas.paneles.ui.entidades.PanelUI
import com.personal.metricas.paneles.ui.entidades.toPanel

class GuardarPanelCU(private val repo: PanelesRepositorio) {
    suspend fun guardar(panelUI: PanelUI) = repo.guardar(panelUI.toPanel())

}