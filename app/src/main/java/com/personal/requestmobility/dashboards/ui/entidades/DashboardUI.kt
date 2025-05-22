package com.personal.requestmobility.dashboards.ui.entidades

import com.personal.requestmobility.dashboards.domain.entidades.Dashboard

data class DashboardUI(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = ""
)

// Mapper from Domain to UI
// Se usa como: DashboardUI().fromDashboard(dashboardDomain)
fun DashboardUI.fromDashboard(dashboard: Dashboard): DashboardUI = DashboardUI(
    id = dashboard.id,
    nombre = dashboard.nombre,
    descripcion = dashboard.descripcion
)

// Mapper from UI to Domain
// Se usa como: dashboardUi.toDashboard()
fun DashboardUI.toDashboard(): Dashboard = Dashboard(
    id = this.id,
    nombre = this.nombre,
    descripcion = this.descripcion
)