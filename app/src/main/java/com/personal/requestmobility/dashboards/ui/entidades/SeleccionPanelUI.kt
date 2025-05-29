package com.personal.requestmobility.dashboards.ui.entidades

import com.personal.requestmobility.kpi.domain.entidades.Kpi
import com.personal.requestmobility.paneles.domain.entidades.Panel

data class SeleccionPanelUI(val identificador: Int,
                            val titulo: String,
                            val descripcion: String,
                            val seleccionado: Boolean = false) {
    companion object {
        fun from(panel: Panel): SeleccionPanelUI {

            val seleccionPanelUI: SeleccionPanelUI = SeleccionPanelUI(
                identificador = panel.id,
                titulo = panel.titulo,
                descripcion = panel.descripcion
            )

            return seleccionPanelUI
        }

    }
}