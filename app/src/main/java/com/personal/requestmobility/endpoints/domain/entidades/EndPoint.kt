package com.personal.requestmobility.endpoints.domain.entidades

import com.personal.requestmobility.core.utils.Parametros

data class EndPoint(
	val id: Int,
	val nombre: String = "",
	val descripcion: String = "",
	val url: String,
	val parametros: Parametros,
	val tabla: String = "",
	val nodoIdentificadorFila : String = ""

)
