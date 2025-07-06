package com.personal.requestmobility.paneles.domain.entidades

import com.personal.requestmobility.core.composables.tabla.Columnas

data class Condiciones(
	val id: Int,
	val columna: Columnas,
	val color: Int,
	val condicionCelda: Int,
	val predicado: String,
	val descripion: String = "",
					  )

//data class CondicionesCelda(val id: Int, val columna : Columnas,  val color: Int, val condicionCelda: Int,  val predicado: String)
