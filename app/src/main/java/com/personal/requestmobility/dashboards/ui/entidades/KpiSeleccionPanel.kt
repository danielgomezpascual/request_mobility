package com.personal.requestmobility.dashboards.ui.entidades

import com.personal.requestmobility.kpi.domain.entidades.Kpi

data class KpiSeleccionPanel(val identificador: Int,
                             val titulo: String,
                             val descripcion: String,
                             val seleccionado: Boolean = false) {
    companion object {
        fun from(kpi: Kpi): KpiSeleccionPanel {

            val kpiSeleccionPanel: KpiSeleccionPanel = KpiSeleccionPanel(
                identificador = kpi.id,
                titulo = kpi.titulo,
                descripcion = kpi.descripcion
            )

            return kpiSeleccionPanel
        }

    }
}