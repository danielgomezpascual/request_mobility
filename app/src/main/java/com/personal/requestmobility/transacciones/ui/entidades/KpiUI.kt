package com.personal.requestmobility.transacciones.ui.entidades

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.componentes.panel.PanelConfiguracion
import com.personal.requestmobility.core.composables.componentes.panel.PanelData
import com.personal.requestmobility.core.composables.componentes.panel.PanelTipoGrafica
import com.personal.requestmobility.transacciones.domain.entidades.Kpi

data class KpiUI(
    val nombre: String, val sql: String, val panelData: PanelData,
) {
    companion object {
        fun from(kpi: Kpi, i: Int): KpiUI {

            val tipoGrafica: PanelTipoGrafica = when (i) {
                0 -> PanelTipoGrafica.BARRAS_FINAS_VERTICALES
                1 -> PanelTipoGrafica.CIRCULAR
                2 -> PanelTipoGrafica.LINEAS
                3 -> PanelTipoGrafica.BARRAS_ANCHAS_VERTICALES
                else -> PanelTipoGrafica.ANILLO
            }

            var configuracion = PanelConfiguracion(titulo = kpi.nombre, tipo = tipoGrafica)
            var valoresTabla = kpi.resultadoSQL.toValoresTabla()
            if (i == 0) {
                configuracion = configuracion.copy(width = 1400.dp, height = 200.dp,
                    mostrarGrafica = false,
                    agruparValores = false, paddingTablaVertical = PaddingValues(0.dp, 0.dp))
                //val titulos = listOf<Header>(Header("1"),Header("2"), Header("3") ,Header("4"), Header("5"))
                //valoresTabla = valoresTabla.copy(/*titulos =  titulos,*/  filasColor = false)
            }

            val grabTabData = PanelData(
                panelConfiguracion = configuracion,
                valoresTabla = valoresTabla
            )

            return KpiUI(
                nombre = kpi.nombre,
                sql = kpi.sql,
                panelData = grabTabData
            )
        }

    }
}