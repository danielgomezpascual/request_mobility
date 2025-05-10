package com.personal.requestmobility.transacciones.domain.entidades

data class Kpi(val nombre : String, val sql: String, var resultadoSQL: ResultadoSQL = ResultadoSQL()){


}
