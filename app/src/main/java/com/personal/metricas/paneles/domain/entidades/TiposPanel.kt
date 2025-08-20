package com.personal.metricas.paneles.domain.entidades

sealed class TiposPanel {
	object PANEL_KPI : TiposPanel()
	object PANEL_END_POINT : TiposPanel()
	object PANEL_TEXTO : TiposPanel()
	companion object {
		fun getTipos(): List<TiposPanel> = listOf(PANEL_KPI, PANEL_END_POINT, PANEL_TEXTO)


		fun dameIndicePanel(panel: TiposPanel) = when (panel) {
			PANEL_END_POINT -> 1
			PANEL_KPI       -> 0
			PANEL_TEXTO     -> 2
		}


		fun dameTipoPanel(valor: Int) = when (valor) {
			0 -> TiposPanel.PANEL_KPI
			1 -> TiposPanel.PANEL_END_POINT
			2 -> TiposPanel.PANEL_TEXTO
			else -> TiposPanel.PANEL_TEXTO
		}


	}


}


fun TiposPanel.literal(): String = when (this) {
	is TiposPanel.PANEL_KPI       -> "KPI"
	is TiposPanel.PANEL_END_POINT -> "END POINT"
	is TiposPanel.PANEL_TEXTO     -> "TEXTO"
}

