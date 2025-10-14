package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.personal.metricas.App
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * Un Composable que muestra una barra de desplazamiento (Slider) diseñada para paginación.
 * Calcula las páginas basándose en el total de elementos y los elementos por página.
 *
 * @param modifier El modificador a aplicar a este Composable.
 * @param totalItems El número total de elementos a paginar.
 * @param itemsPorPagina El número de elementos que se mostrarán en cada página.
 * @param onPageChange La función lambda que se ejecuta cuando la página cambia. Devuelve
 * la página seleccionada y el rango de índices de los elementos (inclusive y basado en 1).
 */
@Composable
fun SliderDePaginacion(
	modifier: Modifier = Modifier,
	totalItems: Int,
	itemsPorPagina: Int,
	onPageChange: (pagina: Int) -> Unit
) {
	// --- Validación de Entradas ---
	require(totalItems > 0) { "El total de items debe ser mayor que cero." }
	require(itemsPorPagina > 0) { "Los items por página deben ser mayor que cero." }

	// --- Lógica de Paginación ---
	val numeroDePaginas = ceil(totalItems.toFloat() / itemsPorPagina.toFloat()).toInt()

	// Si no hay páginas o solo hay una, no mostramos el slider.
	if (numeroDePaginas <= 1) {
		// Opcional: podrías mostrar un texto o nada en este caso.
		return
	}

	var paginaActual by remember { mutableFloatStateOf(1f) }

	// El número de "saltos" entre el inicio y el fin. Para 5 páginas, hay 3 saltos intermedios.
	val steps = numeroDePaginas - 2

	// --- Cálculo de Índices ---
	val paginaSeleccionada = paginaActual.roundToInt()
	val indiceInicial = (paginaSeleccionada - 1) * itemsPorPagina + 1
	val indiceFinal = (paginaSeleccionada * itemsPorPagina).coerceAtMost(totalItems)

	// --- Efecto para notificar el valor inicial ---
	LaunchedEffect(Unit) {
		onPageChange(paginaSeleccionada)
	}

	// --- UI del Composable ---
	Column(modifier = modifier.padding(horizontal = 16.dp)) {
		Text(
			text = "Página: $paginaSeleccionada de $numeroDePaginas",
			style = MaterialTheme.typography.labelLarge,
			color = MaterialTheme.colorScheme.primary
		)
		Text(
			text = "Mostrando elementos: $indiceInicial - $indiceFinal",
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)

		Spacer(modifier = Modifier.height(8.dp))

		Slider(
			value = paginaActual,
			onValueChange = { nuevoValor ->
				paginaActual = nuevoValor
				// Disparamos el callback solo cuando el valor se establece.
			},
			onValueChangeFinished = {
				val paginaFinal = paginaActual.roundToInt()
				val inicio = (paginaFinal - 1) * itemsPorPagina + 1
				val fin = (paginaFinal * itemsPorPagina).coerceAtMost(totalItems)
				onPageChange(paginaActual.toInt())

			},
			valueRange = 1f..numeroDePaginas.toFloat(),
			steps = steps,
			colors = SliderDefaults.colors(
				thumbColor = MaterialTheme.colorScheme.primary,
				activeTrackColor = MaterialTheme.colorScheme.primary,
				inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
			)
		)
	}
}

/*
@Preview(showBackground = true, widthDp = 320)
@Composable
fun PreviewSliderPaginacion() {
	MaterialTheme {
		SliderDePaginacion(
			totalItems = 15,
			itemsPorPagina = 3,
			onPageChange = { pagina, rango ->
				// En una app real, aquí actualizarías el estado de tu ViewModel
				// con la página seleccionada.
				println("Página cambiada a: $pagina, Rango de items: $rango")
			}
		)
	}
}*/