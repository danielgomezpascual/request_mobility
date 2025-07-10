package com.personal.requestmobility.kpi.domain.entidades

data class Kpi(
    val id: Int,
    val titulo: String,
    val descripcion: String = "",
    val origen : String = "",
    val sql: String = "",
    //val configuracion: PanelConfiguracion = PanelConfiguracion(),
    ) {
    
    fun esDinamico(): Boolean = sql.contains("$")
}