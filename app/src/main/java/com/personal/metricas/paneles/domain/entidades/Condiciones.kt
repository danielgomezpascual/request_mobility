package com.personal.metricas.paneles.domain.entidades

import com.personal.metricas.core.composables.tabla.Columnas

data class Condiciones(
	val id: Int,
	val columna: Columnas,
	val color: Int,
	val condicionCelda: Int,
	val predicado: String,
	val descripion: String = "",
					  )

//data class CondicionesCelda(val id: Int, val columna : Columnas,  val color: Int, val condicionCelda: Int,  val predicado: String)
