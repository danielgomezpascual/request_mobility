package com.personal.metricas.inicializador.domain

import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.inicializador.domain.comunes.KpisComunes
import com.personal.metricas.inicializador.domain.sqls.SQL
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.paneles.domain.entidades.Alarmas
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel
import com.personal.metricas.paneles.ui.entidades.PanelUI

class InitDahsboardGeneral(
	private val operaciones: InicializadorOperaciones,
	private val comunes: KpisComunes,
) {
	suspend fun init() {



		val dh = operaciones.guardarDashboard(nombre = "General",
											  listOf<PanelUI>(
												  comunes.obtenerPanelTransaccionesErrores(),
												  comunes.obtenerPanelTransaccionesDiarias(),
												  comunes.obtenerPanelTransaccionesHistorico(),

												  /* panelTransaccionesDiarias,
												   panelTransaccionesHistorico,
												   panelConteoTransacciones*/
												  /*  panelTransaccionesDiariasError,
													panelTransaccionesPorOrganizacion,
													panelEvolucionErrores,
													panelConteoTransacciones*/

											  ),
											  etiqueta = Etiquetas.EtiquetaValor("General"),
											  home = true,
											  color = -16744448
		)
	}
}