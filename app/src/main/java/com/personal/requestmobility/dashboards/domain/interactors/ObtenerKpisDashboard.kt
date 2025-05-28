package com.personal.requestmobility.dashboards.domain.interactors

import com.personal.requestmobility.App
import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpiCU
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.fromKPI

class ObtenerKpisDashboard(
    private val repoDashboard: DashboardRepositorio,
    private val obtenerKPI: ObtenerKpiCU
) {

    suspend fun damePanelesAsociados(identificadorDashboard: Int): List<KpiUI> {
        var listaKpis: List<KpiUI> = emptyList()
        val ds: Dashboard = repoDashboard.obtener(identificadorDashboard)

        ds.paneles.forEach { panel ->
            if (panel.seleccionado) {
                val identificadorKPI = panel.identificador
                val kpi = KpiUI().fromKPI(obtenerKPI.obtener(identificadorKPI))
                listaKpis = listaKpis.plus(kpi)
            }
            App.log.d(panel.identificador)
        }
        return listaKpis

    }


}