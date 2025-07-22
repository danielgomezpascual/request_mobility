package com.personal.metricas.kpi.domain.entidades

import com.personal.metricas.core.utils.Parametros

data class Kpi(
    val id: Int,
    val titulo: String,
    val descripcion: String = "",
    val origen : String = "",
    val sql: String = "",
    val parametros: Parametros = Parametros(),
    val autogenerado: Boolean = false
    ) {
    
    fun esDinamico(): Boolean = sql.contains("#")
}