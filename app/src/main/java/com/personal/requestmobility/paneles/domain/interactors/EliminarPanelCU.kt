package com.personal.requestmobility.paneles.domain.interactors

import com.personal.requestmobility.kpi.domain.repositorios.KpisRepositorio
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.toKpi
import com.personal.requestmobility.paneles.domain.repositorios.PanelesRepositorio
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import com.personal.requestmobility.paneles.ui.entidades.toPanel

class EliminarPanelCU(private val repositorio: PanelesRepositorio) {
    suspend fun eliminar(panelUI: PanelUI) {
        repositorio.eliminar(panelUI.toPanel())
    }
}