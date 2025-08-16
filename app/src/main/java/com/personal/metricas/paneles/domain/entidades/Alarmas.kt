package com.personal.metricas.paneles.domain.entidades

data class Alarmas(
	val id : String = "0",
	val activa: Boolean = false,
	val titulo: String = "",
	val texto: String = "",
	val unica: Boolean = true,
	val color: Int = 0,
)