package com.personal.requestmobility.transacciones.ui.entidades

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.componentes.GraTab.GraTabConfiguracion
import com.personal.requestmobility.core.composables.componentes.GraTab.GraTabData
import com.personal.requestmobility.core.composables.componentes.GraTab.GraTabTipoGrafica
import com.personal.requestmobility.core.composables.graficas.ValoresGrafica
import com.personal.requestmobility.transacciones.domain.entidades.Kpi

data class KpiUI(
    val nombre: String, val sql: String, val graTabData: GraTabData,
) {
    companion object {
        fun from(kpi: Kpi, i: Int): KpiUI {

            val tipoGrafica: GraTabTipoGrafica = when (i) {
                0 -> GraTabTipoGrafica.BARRAS_FINAS_VERTICALES
                1 -> GraTabTipoGrafica.CIRCULAR
                2 -> GraTabTipoGrafica.LINEAS
                3 -> GraTabTipoGrafica.BARRAS_ANCHAS_VERTICALES
                else -> GraTabTipoGrafica.ANILLO
            }

            var configuracion = GraTabConfiguracion(titulo = kpi.nombre, tipo = tipoGrafica)
            var valoresTabla = kpi.resultadoSQL.toValoresTabla()
            if (i == 0) {
                configuracion = configuracion.copy(width = 1400.dp, height = 200.dp,
                    mostrarGrafica = false,
                    agruparValores = false, paddingTablaVertical = PaddingValues(0.dp, 0.dp))
                //val titulos = listOf<Header>(Header("1"),Header("2"), Header("3") ,Header("4"), Header("5"))
                //valoresTabla = valoresTabla.copy(/*titulos =  titulos,*/  filasColor = false)
            }

            val grabTabData = GraTabData(
                graTabConfiguracion = configuracion,
                valoresTabla = valoresTabla
            )

            return KpiUI(
                nombre = kpi.nombre,
                sql = kpi.sql,
                graTabData = grabTabData
            )
        }

    }
}