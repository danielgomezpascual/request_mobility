package com.personal.requestmobility.paneles.domain.entidades

import androidx.compose.ui.util.fastCbrt
import androidx.core.content.pm.ShortcutInfoCompatSaver
import com.personal.requestmobility.kpi.domain.entidades.Kpi


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