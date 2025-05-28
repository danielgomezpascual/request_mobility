package com.personal.requestmobility.paneles.domain.entidades

import com.personal.requestmobility.kpi.domain.entidades.Kpi


data class Panel(val id: Int,
                 val titulo: String,
                 val descripcion: String = "",
                 val configuracion: PanelConfiguracion = PanelConfiguracion(),
                 val kpi: Kpi

)