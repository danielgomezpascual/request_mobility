package com.personal.metricas.paneles.domain.entidades

sealed class TiposPanel{
	object  PANEL_KPI: TiposPanel()
	object  PANEL_END_POINT: TiposPanel()

}
