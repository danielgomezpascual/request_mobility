package com.personal.metricas.paneles.domain.entidades

import com.personal.metricas.kpi.domain.entidades.Kpi


data class Panel(val id: Int,
                 val titulo: String,
                 val descripcion: String = "",
                 val configuracion: PanelConfiguracion = PanelConfiguracion(),
                 val kpi: Kpi,
                 val orden: Int = 0,
                 val seleccionado: Boolean = false,
                 val autogenerado : Boolean = false
){
    fun esDinamico(): Boolean = kpi.esDinamico()
}