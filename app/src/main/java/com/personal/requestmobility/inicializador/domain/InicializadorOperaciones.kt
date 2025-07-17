package com.personal.requestmobility.inicializador.domain

import com.personal.requestmobility.core.utils.Parametros
import com.personal.requestmobility.core.utils.Utils
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.dashboards.domain.entidades.TipoDashboard
import com.personal.requestmobility.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.toDashboard
import com.personal.requestmobility.kpi.domain.interactors.GuardarKpiCU
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion
import com.personal.requestmobility.paneles.domain.interactors.GuardarPanelCU
import com.personal.requestmobility.paneles.ui.entidades.PanelUI

class InicializadorOperaciones(
	private val guardarKpis: GuardarKpiCU,
	private val guardarPaneles: GuardarPanelCU,
	private val guardarDashboard: GuardarDashboardCU,
) {

	suspend fun guardarKpi(kpiUI: KpiUI): KpiUI {
		val identificadorKpi = guardarKpis.guardar(kpiUI)
		val nuevoKpi = kpiUI.copy(id = identificadorKpi.toInt())
		return nuevoKpi
	}


	suspend fun crearPanel(kpiUI: KpiUI, crearKPI: Boolean, configuracion : PanelConfiguracion= PanelConfiguracion()): PanelUI {

		val kpi = if3(crearKPI, guardarKpi(kpiUI), kpiUI)

		val panel = PanelUI.Companion.crearPanelUI(kpi, configuracion)

		val identificadorPanel = guardarPaneles.guardar(panel)
		val nuevoPanel = panel.copy(id = identificadorPanel.toInt())
		return nuevoPanel

	}

	suspend fun guardarDashboard(nombre: String = "", panel: PanelUI, crearPaneles: Boolean = false, kpiOrigen: KpiUI = KpiUI(), crearKPI: Boolean = false): DashboardUI =
		guardarDashboard(nombre, listOf<PanelUI>(panel),crearPaneles,  kpiOrigen, crearKPI)

	suspend fun guardarDashboard(nombre: String = "", paneles: List<PanelUI>, crearPaneles: Boolean = false, kpiOrigen: KpiUI = KpiUI(), crearKPI: Boolean= false): DashboardUI {
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
			descripcion = panelInicial.descripcion,
			kpiOrigen = kpi,
			listaPaneles = listaPaneles,
			parametros = Parametros())

		val identidicador: Long = guardarDashboard.guardar(dashboardUI.toDashboard())
		val nuevoDashboard = dashboardUI.copy(id = identidicador.toInt())
		return nuevoDashboard

	}
}