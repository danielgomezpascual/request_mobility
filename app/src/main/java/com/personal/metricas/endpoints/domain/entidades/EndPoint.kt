package com.personal.metricas.endpoints.domain.entidades

import com.personal.metricas.core.utils.Parametros

data class EndPoint(
	val id: Int = 0,
	val nombre: String = "",
	val descripcion: String = "",
	val url: String = "",
	val parametros: Parametros = Parametros(),
	val tabla: String = "",
	val nodoIdentificadorFila : String = ""

)
