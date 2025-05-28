package com.personal.requestmobility.paneles.domain.interactors

import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.toKpi
import com.personal.requestmobility.paneles.domain.repositorios.PanelesRepositorio
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import com.personal.requestmobility.paneles.ui.entidades.toPanel

class GuardarPanelCU(private val repo: PanelesRepositorio) {
    suspend fun guardar(panelUI: PanelUI) = repo.guardar(panelUI.toPanel())

}