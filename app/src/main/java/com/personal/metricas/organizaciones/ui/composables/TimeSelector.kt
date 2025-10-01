package com.personal.metricas.organizaciones.ui.composables
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// La lista de horas sigue siendo la misma constante eficiente.
private val timeSlots: List<LocalTime> = List(48) { index ->
	val totalMinutes = index * 30
	LocalTime.of(totalMinutes / 60, totalMinutes % 60)
}

/**
 * Muestra una cuadrícula de intervalos de tiempo que permite selección múltiple.
 *
 * @param onSelectionChanged La función lambda que se invoca con el conjunto (`Set`)
 * de horas seleccionadas cada vez que cambia la selección.
 * @param modifier El modificador a aplicar a este Composable.
 * @param initialSelection El conjunto de horas que deben aparecer seleccionadas por defecto.
 */
@Composable
fun TimeSelector(

	onSelectionChanged: (Set<LocalTime>) -> Unit,
	modifier: Modifier = Modifier,
	initialSelection: Set<LocalTime> = emptySet()
) {
	// --- CAMBIO 1: El estado ahora es un Set de LocalTime ---
	// Usamos `toMutableSet()` para poder modificarlo.
	var selectedTimes by rememberSaveable(initialSelection) {
		mutableStateOf(initialSelection)
	}

	LazyVerticalGrid(
		columns = GridCells.Adaptive(minSize = 80.dp),
		modifier = modifier.fillMaxWidth().height(200.dp),
		contentPadding = PaddingValues(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(
			items = timeSlots,
			key = { it.toNanoOfDay() }
		) { time ->
			// --- CAMBIO 2: `isSelected` ahora comprueba si la hora está en el Set ---
			val isSelected = time in selectedTimes

			TimeSlot(
				time = time.format(DateTimeFormatter.ofPattern("HH:mm")),
				isSelected = isSelected,
				onClick = {
					// --- CAMBIO 3: La lógica de click ahora añade o quita del Set ---
					val updatedSelection = selectedTimes.toMutableSet()
					if (isSelected) {
						updatedSelection.remove(time)
					} else {
						updatedSelection.add(time)
					}
					selectedTimes = updatedSelection
					onSelectionChanged(updatedSelection)
				}
			)
		}
	}
}

// El Composable `TimeSlot` sigue sin necesitar cambios. ¡Qué bien diseñado estaba! 😉

@Preview(showBackground = true)
@Composable
private fun TimeSelector() {
	// En la preview, pre-seleccionamos algunas horas para ver cómo queda.
	TimeSelector(
		onSelectionChanged = {},
		initialSelection = setOf(
			LocalTime.of(9, 0),
			LocalTime.of(11, 30),
			LocalTime.of(16, 0)
		)
	)
}




/**
 * Convierte un String con horas separadas por punto y coma en un Set de LocalTime.
 *
 * @param timeString El string a parsear (ej: "09:00;10:30;18:00").
 * @return Un Set<LocalTime> con las horas válidas. Ignora las partes mal formadas.
 */
fun ParseTimesToSet(timeString: String?): Set<LocalTime> {
	// Si el string es nulo o está en blanco, devolvemos un conjunto vacío.
	if (timeString.isNullOrBlank()) {
		return emptySet()
	}

	return timeString
		.split(';') // 1. Divide el string en una lista: ["09:00", "10:30", ...]
		.mapNotNull { timeStr -> // 2. Itera y transforma cada elemento
			try {
				// 3. Intenta convertir el texto a LocalTime
				LocalTime.parse(timeStr.trim()) // .trim() elimina espacios accidentales
			} catch (e: DateTimeParseException) {
				// 4. Si falla (formato incorrecto), lo ignoramos devolviendo null
				//    mapNotNull se encargará de descartar estos resultados nulos.
				println("Formato de hora inválido, se omitirá: '$timeStr'")
				null
			}
		}
		.toSet() // 5. Convierte la lista de resultados válidos en un Set
}