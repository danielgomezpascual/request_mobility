package com.personal.metricas.core.composables.tabla

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.utils.Parametro
import com.personal.metricas.core.utils.Parametros

data class Fila(
	var celdas: List<Celda> = emptyList<Celda>(), val size: Dp = 150.dp,
	val color: Color = Color.Companion.White, val seleccionada: Boolean = false,
	val visible: Boolean = true, val obtenidaDesdeKPI: Boolean = true,

	) {
	fun toParametros() = Parametros(ps = this.celdas.map { celda -> Parametro(celda.titulo, celda.valor) })

}