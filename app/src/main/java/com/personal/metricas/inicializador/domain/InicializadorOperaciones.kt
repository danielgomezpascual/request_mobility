package com.personal.metricas.inicializador.domain

import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.domain.entidades.TipoDashboard
import com.personal.metricas.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.metricas.dashboards.ui.entidades.DashboardUI
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.dashboards.ui.entidades.toDashboard
import com.personal.metricas.kpi.domain.interactors.GuardarKpiCU
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.paneles.domain.interactors.GuardarPanelCU
import com.personal.metricas.paneles.ui.entidades.PanelUI

class InicializadorOperaciones(
	private val guardarKpis: GuardarKpiCU,
	private val guardarPaneles: GuardarPanelCU,
	private val guardarDashboard: GuardarDashboardCU,
) {

	suspend fun guardarKpi(kpiUI: KpiUI): KpiUI {
		val k = kpiUI.copy(autogenerado = true)
		val identificadorKpi = guardarKpis.guardar(k)
		val nuevoKpi = k.copy(id = identificadorKpi.toInt())
		return nuevoKpi
	}


	suspend fun crearPanel(kpiUI: KpiUI, crearKPI: Boolean, configuracion : PanelConfiguracion= PanelConfiguracion()): PanelUI {
		val kpi = if3(crearKPI, guardarKpi(kpiUI), kpiUI)
		val panel = PanelUI.Companion.crearPanelUI(kpi, configuracion)
		val p = panel.copy(autogenerado = true)
		val identificadorPanel = guardarPaneles.guardar(p)
		val nuevoPanel = p.copy(id = identificadorPanel.toInt())
		return nuevoPanel

	}

	suspend fun guardarDashboard(nombre: String = "", panel: PanelUI, crearPaneles: Boolean = false, kpiOrigen: KpiUI = KpiUI(), crearKPI: Boolean = false): DashboardUI =
		guardarDashboard(nombre = nombre,
						 paneles = listOf<PanelUI>(element = panel),
						 crearPaneles = crearPaneles,
						 kpiOrigen = kpiOrigen,
						 crearKPI = crearKPI)

	suspend fun guardarDashboard(nombre: String = "",   paneles: List<PanelUI>, crearPaneles: Boolean = false, kpiOrigen: KpiUI = KpiUI(), crearKPI: Boolean= false, etiqueta: Etiquetas = Etiquetas.EtiquetaVacia(),): DashboardUI {
		var misPaneles: List<PanelUI> = paneles
		if (crearPaneles) {
			misPaneles = emptyList()
			paneles.forEach { panel ->
				val id = guardarPaneles.guardar(panel)
				val nuevoPanel = panel.copy(id = id.toInt())
				misPaneles = misPaneles.plus(nuevoPanel)
			}
		}

		val listaPaneles: List<PanelUI> = misPaneles.mapIndexed { indice, panel -> panel.copy(seleccionado = true, orden = indice) }




		val kpi = if3(crearKPI, guardarKpi(kpiOrigen), kpiOrigen)

		val panelInicial = listaPaneles.first()
		val dashboardUI: DashboardUI = DashboardUI(
			tipo = if3 (kpiOrigen.equals(KpiUI()), TipoDashboard.Estatico(),  TipoDashboard.Dinamico ()),
			nombre = if3(nombre.isEmpty(), panelInicial.titulo,  nombre),
			logo = "",
			home = false,
			etiqueta = etiqueta ,
			descripcion = panelInicial.descripcion,
			kpiOrigen = kpi,
			listaPaneles = listaPaneles,
			parametros = Parametros(), 
			autogenerado = true)

		val identidicador: Long = guardarDashboard.guardar(dashboardUI.toDashboard())
		val nuevoDashboard = dashboardUI.copy(id = identidicador.toInt())
		return nuevoDashboard

	}
}