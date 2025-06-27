package com.personal.requestmobility.paneles.ui.entidades

import androidx.compose.ui.graphics.Color
import com.personal.requestmobility.core.composables.tabla.Celda
import com.personal.requestmobility.core.composables.tabla.Columnas

data class Condiciones(val id: Int, val color: Int, val predicado: String)

data class CondicionesCelda(val id: Int, val columna : Columnas,  val color: Int, val condicionCelda: Int,  val predicado: String)
