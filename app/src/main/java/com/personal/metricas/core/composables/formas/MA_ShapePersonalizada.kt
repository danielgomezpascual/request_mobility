package com.personal.metricas.core.composables.formas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


@Composable
fun MA_ShapePersonalizada(period: Float = 2f, amplitude: Float = 0.2f): Shape {
	// Usamos remember para que la instancia de la forma se conserve entre recomposiciones,
	// siempre que los parámetros (period, amplitude) no cambien.
	return remember(period, amplitude) {
		object : Shape {
			override fun createOutline(
				size: Size,
				layoutDirection: LayoutDirection,
				density: Density,
			): Outline {
				return Outline.Generic(
					path = Path().apply {
						// La lógica del trazado es exactamente la misma que antes.
						moveTo(size.width, size.height * (1 - amplitude))
						lineTo(size.width, 0f)
						lineTo(0f, 0f)
						lineTo(0f, size.height * (1 - amplitude))

						val waveWidth = size.width / period
						val waveAmplitude = size.height * amplitude

						for (i in 0 until period.toInt()) {
							val startX = i * waveWidth
							val controlX = startX + waveWidth / 2f
							val endX = startX + waveWidth

							val controlY = if (i % 2 == 0) {
								size.height * (1 - amplitude) - waveAmplitude
							} else {
								size.height * (1 - amplitude) + waveAmplitude
							}

							quadraticBezierTo(
								x1 = controlX,
								y1 = controlY,
								x2 = endX,
								y2 = size.height * (1 - amplitude)
							)
						}
						close()
					}
				)
			}
		}
	}
}
