package com.personal.metricas.dashboards.data.ds.local.entidades

import com.personal.metricas.paneles.domain.entidades.Panel

data class PanelDashboardRoom(
	val idPanel: Int = 0,
	val seleccionado: Boolean = false,
	val orden: Int = 0,
							 ) {
	companion object {
		fun fromPanel(panel: Panel): PanelDashboardRoom = PanelDashboardRoom(idPanel = panel.id,
																			 seleccionado = panel.seleccionado,
																			 orden = panel.orden
																			)
		
	}
}