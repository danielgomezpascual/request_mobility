package com.personal.metricas.core.composables.tabla

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.utils.esNumerico
import com.personal.metricas.core.utils.if3
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.FuncionesCondicionCelda

data class Celda(
	val valor: String = "",
	var size: Dp = 50.dp,
	val colorCelda: Color = Color.Companion.Blue,
	val fondoCelda: Color = Color.Companion.White,
	val contenido: @Composable (Modifier) -> Unit = { modifier ->
		MA_LabelCelda(modifier = modifier, valor = valor,/* color = colorCelda,*/
					  alineacion = if3(valor.esNumerico(),
									   TextAlign.Companion.End, TextAlign.Companion.Start))
	},
	val titulo: String = "", val colorTitulo: Color = Color.Black,
	val fondoTitulo: Color = Color(0xFFF5F5F5),
	val celdaTitulo: @Composable (Modifier) -> Unit = { modifierTitulo ->
		//MA_LabelCeldaTitulo(valor = titulo, color = colorTitulo, fondo = fondoTitulo)

		MA_LabelCelda(modifier = modifierTitulo, valor = titulo,/* color = colorCelda,*/
					  color = colorTitulo, fondo = fondoTitulo,
					  alineacion = if3(valor.esNumerico(),
									   TextAlign.Companion.End, TextAlign.Companion.Start))
	},
	val seleccionada: Boolean = false,
	val filtroInvertido: Boolean = false,
	val condicion: Condiciones  = Condiciones(0, columna = Columnas(""),
											  color = 0 , condicionCelda = 0, predicado = "")
)