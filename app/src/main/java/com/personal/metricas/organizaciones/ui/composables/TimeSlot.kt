package com.personal.metricas.organizaciones.ui.composables
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Representa un único intervalo de tiempo seleccionable.
 *
 * @param time La cadena de texto de la hora a mostrar (ej: "14:30").
 * @param isSelected Indica si este intervalo está actualmente seleccionado.
 * @param onClick La función lambda que se invoca cuando el usuario pulsa el intervalo.
 * @param modifier El modificador a aplicar a este Composable.
 */
@Composable
fun TimeSlot(
	time: String,
	isSelected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	// Definimos los colores basados en el estado de selección
	val backgroundColor = if (isSelected) {
		MaterialTheme.colorScheme.primary
	} else {
		MaterialTheme.colorScheme.surface
	}

	val contentColor = if (isSelected) {
		MaterialTheme.colorScheme.onPrimary
	} else {
		MaterialTheme.colorScheme.onSurface
	}

	val borderColor = if (isSelected) {
		MaterialTheme.colorScheme.primary
	} else {
		MaterialTheme.colorScheme.outline
	}

	Box(
		modifier = modifier
			.clip(RoundedCornerShape(8.dp)) // Bordes redondeados
			.background(backgroundColor)
			.border(
				width = 1.dp,
				color = borderColor,
				shape = RoundedCornerShape(8.dp)
			)
			.clickable(onClick = onClick) // Hacemos que sea pulsable
			.padding(horizontal = 16.dp, vertical = 8.dp),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = time,
			color = contentColor,
			style = MaterialTheme.typography.bodyMedium
		)
	}
}

// Preview para ver cómo queda nuestro TimeSlot en ambos estados
@Preview(showBackground = true)
@Composable
private fun TimeSlotSelectedPreview() {
	TimeSlot(time = "10:00", isSelected = true, onClick = {})
}

@Preview(showBackground = true)
@Composable
private fun TimeSlotUnselectedPreview() {
	TimeSlot(time = "10:30", isSelected = false, onClick = {})
}