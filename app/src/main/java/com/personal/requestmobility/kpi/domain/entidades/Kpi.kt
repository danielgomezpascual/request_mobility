package com.personal.requestmobility.kpi.domain.entidades

import com.personal.requestmobility.core.utils.Parametros

data class Kpi(
    val id: Int,
    val titulo: String,
    val descripcion: String = "",
    val origen : String = "",
    val sql: String = "",
    val parametros: Parametros = Parametros()
    ) {
    
    fun esDinamico(): Boolean = sql.contains("#")
}