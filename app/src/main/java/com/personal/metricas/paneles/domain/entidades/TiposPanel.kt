package com.personal.metricas.paneles.domain.entidades

sealed class TiposPanel{
	object  PANEL_KPI: TiposPanel()
	object  PANEL_END_POINT: TiposPanel()
}


fun TiposPanel.literal(): String = when(this){
			is TiposPanel.PANEL_KPI -> "KPI"
			is TiposPanel.PANEL_END_POINT -> "END POINT"
	}

