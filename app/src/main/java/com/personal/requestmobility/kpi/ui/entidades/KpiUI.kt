package com.personal.requestmobility.kpi.ui.entidades

import androidx.compose.ui.graphics.Color
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.kpi.domain.entidades.Kpi
import kotlin.Int

data class KpiUI(
    val id: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val origen : String = "",
    val sql: String = "",
    val dinamico : Boolean = false
    /*var panelData: PanelData = PanelData(),
    var resultadoSQL: ResultadoSQL = ResultadoSQL()*/
) {

//    fun esDinamico(): Boolean = sql.contains("$")
    fun dameColorDinamico() = if3(dinamico, Color(0xFF0277BD), Color(0xFFD84315))
    
   /* fun reloadPanelData(): KpiUI {

        this.resultadoSQL = SqlToListString(sql)
        this.panelData = PanelData(
            panelConfiguracion = panelData.panelConfiguracion.copy(titulo = titulo, descripcion = descripcion),
            valoresTabla = resultadoSQL.toValoresTabla()
        )
        return this
    }*/

    companion object {
        fun from(kpi: Kpi, i: Int): KpiUI {
            val kpiUI: KpiUI = KpiUI(
                titulo = kpi.titulo,
                sql = kpi.sql,
                dinamico =  kpi.esDinamico()
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
  //  configuracion = this.panelData.panelConfiguracion,
)


fun KpiUI.fromKPI(kpi: Kpi) = KpiUI(
    id = kpi.id,
    titulo = kpi.titulo,
    descripcion = kpi.descripcion,
    sql = kpi.sql,
    dinamico =  kpi.esDinamico()
   // panelData = PanelData(panelConfiguracion = kpi.configuracion)
)
