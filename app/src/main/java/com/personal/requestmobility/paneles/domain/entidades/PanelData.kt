package com.personal.requestmobility.paneles.domain.entidades

import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.ValoresTabla
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import com.personal.requestmobility.transacciones.domain.entidades.ResultadoSQL

fun PanelData.fromPanelUI(panelUI: PanelUI): PanelData {
    val panelConfiguracion = panelUI.configuracion
    val tabla: ValoresTabla = ResultadoSQL.fromSqlToTabla(panelUI.kpi.sql)
    return PanelData(panelConfiguracion = panelConfiguracion,
        valoresTabla = tabla)
}

data class PanelData(
    val panelConfiguracion: PanelConfiguracion = PanelConfiguracion(),
    var valoresTabla: ValoresTabla = ValoresTabla()) {

    fun ordenarElementos() = valoresTabla.dameElementosOrdenados(campoOrdenacionTabla = panelConfiguracion.columnaY)

     fun limiteElementos(): List<Fila> =
        valoresTabla.dameElementosTruncados(panelConfiguracion = panelConfiguracion,
            limite = panelConfiguracion.limiteElementos,
            agrupar = panelConfiguracion.agruparValores,
            indiceCampoSumar = panelConfiguracion.columnaY
        )

     fun establecerColorFilas(): List<Fila>  {
        //establecemos los colores que va a tener cada elmento (teninedo en cuenta que deben al menos tener todos los coles declarados.
        if (panelConfiguracion.colores.isNotEmpty()) {
            val totalColores: Int = panelConfiguracion.colores.size
            valoresTabla.filas = valoresTabla.filas.mapIndexed { index, f ->
                val indiceColor = index % totalColores
                f.copy(color = panelConfiguracion.colores[indiceColor])
            }
        }
         return  valoresTabla.filas
    }
}