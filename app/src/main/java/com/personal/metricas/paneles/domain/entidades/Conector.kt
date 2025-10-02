package com.personal.metricas.paneles.domain.entidades

enum class TIPO_CONECTOR {CONECTAR_DASHBOARD}
data class Conector(val  tipo: TIPO_CONECTOR = TIPO_CONECTOR.CONECTAR_DASHBOARD,
					val identificador: Int = 0,
					val descripcion: String = "",
					val data: String = "")
