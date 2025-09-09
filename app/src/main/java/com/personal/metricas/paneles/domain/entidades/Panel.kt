package com.personal.metricas.paneles.domain.entidades

import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.kpi.domain.entidades.Kpi


data class Panel(val id: Int = 0 ,
                 val titulo: String = "",
                 val descripcion: String = "",
                 val configuracion: PanelConfiguracion = PanelConfiguracion(),
                 val kpi: Kpi = Kpi(),
                 val orden: Int = 0,
                 val seleccionado: Boolean = false,
                 val autogenerado : Boolean = false,
                 val endPoint: EndPoint = EndPoint(),
                 val tipoPanel: TiposPanel = TiposPanel.PANEL_KPI,
                 val color: Int = 0
){
    fun esDinamico(): Boolean = kpi.esDinamico()
}