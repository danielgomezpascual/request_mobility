package com.personal.requestmobility.dashboards.ui.entidades

import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.entidades.KpiPaneles
import com.personal.requestmobility.dashboards.domain.entidades.toKpiSeleccionPanel

data class DashboardUI(
    val id: Int = 0,
    val nombre: String = "",
    val logo : String = "",
    val descripcion: String = "",
    val listaPaneles: List<KpiSeleccionPanel> = emptyList<KpiSeleccionPanel>()
)

// Mapper from Domain to UI
// Se usa como: DashboardUI().fromDashboard(dashboardDomain)
fun DashboardUI.fromDashboard(dashboard: Dashboard): DashboardUI {

    return DashboardUI(
        id = dashboard.id,
        nombre = dashboard.nombre,
        logo = dashboard.logo,
        descripcion = dashboard.descripcion,
        listaPaneles = dashboard.paneles.map { it.toKpiSeleccionPanel() }
    )

}

// Mapper from UI to Domain
// Se usa como: dashboardUi.toDashboard()
fun DashboardUI.toDashboard(): Dashboard = Dashboard(
    id = this.id,
    nombre = this.nombre,
    logo =  this.logo,
    descripcion = this.descripcion,
    paneles = this.listaPaneles.map { KpiPaneles.fromKpiSeleccionPanel(it) }
)