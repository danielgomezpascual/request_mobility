package com.personal.requestmobility.dashboards.ui.entidades

import com.personal.requestmobility.core.utils.Parametros
import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.entidades.TipoDashboard
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.fromKPI
import com.personal.requestmobility.kpi.ui.entidades.toKpi
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import com.personal.requestmobility.paneles.ui.entidades.fromPanel
import com.personal.requestmobility.paneles.ui.entidades.toPanel

data class DashboardUI(
	val id: Int = 0,
	val tipo: TipoDashboard = TipoDashboard.Estatico(),
	val nombre: String = "",
	val logo: String = "",
	val home: Boolean = false,
	val descripcion: String = "",
	val kpiOrigen: KpiUI = KpiUI(),
	val listaPaneles: List<PanelUI> = emptyList<PanelUI>(),
	val parametros: Parametros = Parametros(),
					  )

// Mapper from Domain to UI
// Se usa como: DashboardUI().fromDashboard(dashboardDomain)
fun DashboardUI.fromDashboard(dashboard: Dashboard): DashboardUI {




	val nombre = Parametros.reemplazar(dashboard.nombre, dashboard.kpiOrigenDatos.parametros, dashboard.parametros)
	return DashboardUI(
		id = dashboard.id,
		tipo = dashboard.tipo,
		nombre = nombre,
		home = dashboard.home,
		logo = dashboard.logo,
		descripcion = dashboard.descripcion,
		kpiOrigen = KpiUI().fromKPI(dashboard.kpiOrigenDatos),
		listaPaneles = dashboard.paneles.map { PanelUI().fromPanel(it) },
		parametros = dashboard.parametros
					  )
	
}

// Mapper from UI to Domain
// Se usa como: dashboardUi.toDashboard()
fun DashboardUI.toDashboard(): Dashboard {
	
	//val panelesDinamcos = (this.listaPaneles.filter { it.esDinamico() })
	//val tipo = if3 ((panelesDinamcos.isEmpty()),  TipoDashboard.Estatico() , TipoDashboard.Dinamico())
	
	
	return Dashboard(
		id = this.id,
		tipo = this.tipo,
		nombre = this.nombre,
		home = this.home,
		logo = this.logo,
		descripcion = this.descripcion,
		kpiOrigenDatos = this.kpiOrigen.toKpi(),
		paneles = this.listaPaneles.map {
				it.toPanel()
			},
		parametros = this.parametros
			 )
}


