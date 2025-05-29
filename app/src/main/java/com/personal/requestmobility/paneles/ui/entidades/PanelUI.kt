package com.personal.requestmobility.paneles.ui.entidades

import com.personal.requestmobility.kpi.domain.entidades.Kpi
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.fromKPI
import com.personal.requestmobility.kpi.ui.entidades.toKpi
import com.personal.requestmobility.paneles.domain.entidades.Panel
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion
import kotlin.Int

data class PanelUI(
    val id: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val configuracion: PanelConfiguracion = PanelConfiguracion(),
    val kpi: KpiUI = KpiUI(),
    val seleccionado: Boolean = false

) {


}

fun PanelUI.toPanel() = Panel(
    id = this.id,
    titulo = this.titulo,
    descripcion = this.descripcion,
    configuracion = this.configuracion,
    kpi = this.kpi.toKpi()

)


fun PanelUI.fromPanel(panel: Panel): PanelUI {

    return PanelUI(
        id = panel.id,
        titulo = panel.titulo,
        descripcion = panel.descripcion,
        configuracion = panel.configuracion,
        kpi = KpiUI().fromKPI(panel.kpi),
        seleccionado = false)
}
