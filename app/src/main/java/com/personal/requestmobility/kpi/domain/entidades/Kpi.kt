package com.personal.requestmobility.kpi.domain.entidades

import com.personal.requestmobility.core.composables.componentes.panel.PanelConfiguracion
import com.personal.requestmobility.transacciones.domain.entidades.ResultadoSQL

data class Kpi(
    val id: Int,
    val titulo: String,
    val descripcion: String = "",
    val sql: String = "",
    val configuracion: PanelConfiguracion = PanelConfiguracion(),
    ) {
}