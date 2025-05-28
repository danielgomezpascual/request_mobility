package com.personal.requestmobility.dashboards.domain.interactors

import com.personal.requestmobility.dashboards.ui.entidades.KpiSeleccionPanel
import com.personal.requestmobility.kpi.domain.repositorios.KpisRepositorio
import kotlinx.coroutines.flow.map

class ObtenerKpisSeleccionPanel(private val repoKpi: KpisRepositorio) {

    suspend fun obtenerTodos() = repoKpi.getAll().map { listaKpis ->

        listaKpis.mapIndexed { index, kpi ->
            KpiSeleccionPanel.from(kpi)
        }
    }
}
