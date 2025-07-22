package com.personal.metricas.dashboards.domain.interactors

import com.personal.metricas.dashboards.domain.repositorios.DashboardRepositorio
import com.personal.metricas.kpi.domain.interactors.ObtenerKpiCU

class ObtenerKpisDashboard(
    private val repoDashboard: DashboardRepositorio,
    private val obtenerKPI: ObtenerKpiCU
) {

   /* suspend fun damePanelesAsociados(identificadorDashboard: Int): List<KpiUI> {
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

    }*/


}