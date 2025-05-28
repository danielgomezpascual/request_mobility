package com.personal.requestmobility.dashboards.domain.entidades

import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.KpiSeleccionPanel
import kotlin.Int

data class KpiPaneles(
    val identificador: Int,
    val titulo: String,
    val descripcion: String,
    val seleccionado: Boolean = false){
    companion object{
        // Mapper from Domain to UI
// Se usa como: DashboardUI().fromDashboard(dashboardDomain)
        fun fromKpiSeleccionPanel(seleccionPanel: KpiSeleccionPanel): KpiPaneles =
            KpiPaneles(
                identificador = seleccionPanel.identificador,
                titulo = seleccionPanel.titulo,
                descripcion = seleccionPanel.descripcion,
                seleccionado = seleccionPanel.seleccionado,

                )
    }
}



// Mapper from UI to Domain
// Se usa como: dashboardUi.toDashboard()
fun KpiPaneles.toKpiSeleccionPanel(): KpiSeleccionPanel = KpiSeleccionPanel(
    identificador = this.identificador,
    titulo = this.titulo,
    descripcion = this.descripcion,
    seleccionado = this.seleccionado,
)
