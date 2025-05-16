package com.personal.requestmobility.kpi.ui.entidades

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.componentes.panel.PanelConfiguracion
import com.personal.requestmobility.core.composables.componentes.panel.PanelData
import com.personal.requestmobility.core.composables.componentes.panel.PanelTipoGrafica
import com.personal.requestmobility.kpi.domain.entidades.Kpi
import com.personal.requestmobility.transacciones.domain.entidades.ResultadoSQL
import kotlin.Int

data class KpiUI(
    val id: Int = 0,
    val titulo: String ="",
    val descripcion: String = "",
    val sql: String = "",
    val panelData: PanelData = PanelData(),
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

            var configuracion = PanelConfiguracion(titulo = kpi.titulo, tipo = tipoGrafica)
            var valoresTabla = kpi.resultadoSQL.toValoresTabla()
            if (i == 0) {
                configuracion = configuracion.copy(
                    width = 1400.dp, height = 200.dp,
                    mostrarGrafica = false,
                    agruparValores = false, paddingTablaVertical = PaddingValues(0.dp, 0.dp)
                )
            }

            val grabTabData = PanelData(
                panelConfiguracion = configuracion,
                valoresTabla = valoresTabla
            )

            return KpiUI(
                titulo = kpi.titulo,
                sql = kpi.sql,
                panelData = grabTabData
            )
        }

    }
}

fun KpiUI.toKpi() = Kpi(
    id = this.id,
    titulo = this.titulo,
    descripcion = this.descripcion,
    sql = this.sql,
    configuracion = this.panelData.panelConfiguracion,
    )


fun KpiUI.fromKPI(kpi: Kpi) = KpiUI(
     id = kpi.id,
     titulo = kpi.titulo,
     descripcion = kpi.descripcion,
     sql = kpi.sql
    // configuracion = kpi.configuracion,

)
