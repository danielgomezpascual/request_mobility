package com.personal.metricas.kpi.ui.entidades

import androidx.compose.ui.graphics.Color
import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.core.composables.tabla.ValoresTabla
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils.if3
import com.personal.metricas.kpi.domain.entidades.Kpi
import com.personal.metricas.transacciones.domain.entidades.ResultadoSQL
import kotlin.Int

data class KpiUI(
    val id: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val origen : String = "",
    val sql: String = "",
    val dinamico : Boolean = false,
    val parametros: Parametros =  Parametros(),
    val autogenerado: Boolean = false
    /*var panelData: PanelData = PanelData(),
    var resultadoSQL: ResultadoSQL = ResultadoSQL()*/
) {


    fun dameColorDinamico() = if3(dinamico, Color(0xFF0277BD), Color(0xFFD84315))

    fun dameColumnasSQL(): List<Columnas>{
        val res: ValoresTabla = ResultadoSQL.fromSqlToTabla(this.toKpi())
        return  res.columnas
    }



    companion object {
        fun from(kpi: Kpi, i: Int): KpiUI {
            val kpiUI: KpiUI = KpiUI(
                titulo = kpi.titulo,
                sql = kpi.sql,
                dinamico =  kpi.esDinamico(), 
                parametros =  kpi.parametros,
                autogenerado = kpi.autogenerado

                
            )

           // kpiUI.reloadPanelData()
            return kpiUI
        }


    }
}

fun KpiUI.toKpi() = Kpi(
    id = this.id,
    titulo = this.titulo,
    descripcion = this.descripcion,
    sql = this.sql,
    parametros =  this.parametros, 
    autogenerado = this.autogenerado
  //  configuracion = this.panelData.panelConfiguracion,
)


fun KpiUI.fromKPI(kpi: Kpi) = KpiUI(
    id = kpi.id,
    titulo = kpi.titulo,
    descripcion = kpi.descripcion,
    sql = kpi.sql,
    dinamico =  kpi.esDinamico(),
    parametros = kpi.parametros,
    autogenerado = kpi.autogenerado

   // panelData = PanelData(panelConfiguracion = kpi.configuracion)
)
