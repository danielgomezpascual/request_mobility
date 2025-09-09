package com.personal.metricas.core.composables.formas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import java.util.Random
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MA_ShapeIrregular(
	corners: Int = 12,
	irregularity: Float = 0.4f,
	seed: Long = 0L,
): Shape {
	// Usamos remember para no recalcular la forma a menos que cambien los parámetros.
	return remember(corners, irregularity, seed) {
		object : Shape {
			// Creamos una instancia de Random con el seed para que el resultado sea determinista.
			private val random = Random(seed)

			override fun createOutline(
				size: Size,
				layoutDirection: LayoutDirection,
				density: Density,
			): Outline {
				return Outline.Generic(
					path = Path().apply {
						val centerX = size.width / 2
						val centerY = size.height / 2
						val radiusX = centerX
						val radiusY = centerY

						// Empezamos generando los puntos de nuestro polígono irregular.
						val points = mutableListOf<Pair<Float, Float>>()
						for (i in 0 until corners) {
							val angle = (2 * PI * i) / corners

							// Calculamos la desviación aleatoria.
							val randomFactor = 1f - (random.nextFloat() * irregularity)

							val x = centerX + (radiusX * cos(angle) * randomFactor).toFloat()
							val y = centerY + (radiusY * sin(angle) * randomFactor).toFloat()
							points.add(Pair(x, y))
						}

						// Movemos el "lápiz" al primer punto.
						moveTo(points.first().first, points.first().second)

						// Dibujamos líneas rectas entre todos los puntos generados.
						points.drop(1).forEach { (x, y) ->
							lineTo(x, y)
						}

						// Cerramos la forma para unir el último punto con el primero.
						close()
					}
				)
			}
		}
	}
}