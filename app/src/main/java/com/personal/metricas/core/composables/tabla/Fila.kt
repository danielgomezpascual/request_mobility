package com.personal.metricas.core.composables.tabla

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.utils.Parametro
import com.personal.metricas.core.utils.Parametros
import okhttp3.internal.notify

data class Fila(
	var celdas: List<Celda> = emptyList<Celda>(), val size: Dp = 150.dp,
	val color: Color = Color.Companion.White, val seleccionada: Boolean = false,
	val visible: Boolean = true, val obtenidaDesdeKPI: Boolean = true,

	) {
	fun toParametros() = Parametros(ps = this.celdas.map { celda -> Parametro(celda.titulo, celda.valor) })


	fun dameColumnaVacia(titulo: String): Columnas? {

		var columna: Columnas? = null
		this.celdas.forEachIndexed { indice, celda ->
			if (celda.titulo.equals(titulo, true)) {
				columna = Columnas(celda.titulo, indice, valores = emptyList())
			}

		}
		return columna
	}

	fun dameValor(nomberColumna: String ) : String {
		return this.celdas.firstOrNull { celda -> celda.titulo.equals(nomberColumna, true)}?.valor.toString()
	}

}