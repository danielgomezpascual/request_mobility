package com.personal.requestmobility.kpi.ui.entidades

import androidx.compose.ui.graphics.Color
import com.personal.requestmobility.core.composables.tabla.Columnas
import com.personal.requestmobility.core.composables.tabla.ValoresTabla
import com.personal.requestmobility.core.utils.Parametros
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.kpi.domain.entidades.Kpi
import com.personal.requestmobility.transacciones.domain.entidades.ResultadoSQL
import kotlin.Int

data class KpiUI(
    val id: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val origen : String = "",
    val sql: String = "",
    val dinamico : Boolean = false,
    val parametros: Parametros =  Parametros()
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
                parametros =  kpi.parametros
                
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
    parametros =  this.parametros
  //  configuracion = this.panelData.panelConfiguracion,
)


fun KpiUI.fromKPI(kpi: Kpi) = KpiUI(
    id = kpi.id,
    titulo = kpi.titulo,
    descripcion = kpi.descripcion,
    sql = kpi.sql,
    dinamico =  kpi.esDinamico(),
    parametros = kpi.parametros

   // panelData = PanelData(panelConfiguracion = kpi.configuracion)
)
