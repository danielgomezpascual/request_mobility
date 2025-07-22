package com.personal.metricas.paneles.ui.entidades

import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.kpi.ui.entidades.fromKPI
import com.personal.metricas.kpi.ui.entidades.toKpi
import com.personal.metricas.paneles.domain.entidades.Panel
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import kotlin.Int

data class PanelUI(
    val id: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val configuracion: PanelConfiguracion = PanelConfiguracion(),
    val kpi: KpiUI = KpiUI(),
    val seleccionado: Boolean = false,
    val orden : Int = 0,
    val visible : Boolean = true,
    val autogenerado: Boolean = false
){
    fun esDinamico () = kpi.dinamico

    companion object{
        fun crearPanelUI(kpiUI: KpiUI, configuracion: PanelConfiguracion= PanelConfiguracion()): PanelUI{
           return PanelUI(id = 0,
                          titulo = kpiUI.titulo,
                          descripcion = kpiUI.descripcion,
                          configuracion = configuracion,
						  kpi = kpiUI)

        }
    }
}

fun PanelUI.toPanel() = Panel(
    id = this.id,
    titulo = this.titulo,
    descripcion = this.descripcion,
    configuracion = this.configuracion,
    kpi = this.kpi.toKpi(),
    orden = this.orden,
    seleccionado = this.seleccionado,
    autogenerado = this.autogenerado

)


fun PanelUI.fromPanel(panel: Panel): PanelUI {

    return PanelUI(
        id = panel.id,
        titulo = panel.titulo,
        descripcion = panel.descripcion,
        configuracion = panel.configuracion,
        kpi = KpiUI().fromKPI(panel.kpi),
        orden = panel.orden,
        seleccionado = panel.seleccionado,
        autogenerado = panel.autogenerado
    )
}
