package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.formas.MA_ShapeIrregular
import com.personal.metricas.core.composables.labels.MA_LabelNormal

@Composable
fun MA_SeleccionColor(color: Color) {

	/*Box(
		modifier =
			Modifier
				.padding(3.dp)
				//.border(width = 1.dp, color = Color.Black)
				.width(36.dp)
				.height(36.dp)
				.clip(wavyBottomShape(period = 3f, amplitude = 0.15f))
				.background(color = color)
	) */
	Box(
		modifier = Modifier
			.size(width = 36.dp, height = 36.dp)
			// Le damos una ligera rotación para un toque más natural.
			.rotate(-2f)
			.background(
				color = color,
				// ¡Llamamos a nuestra función!
				// Cambia el 'seed' para obtener una forma diferente.
				shape = MA_ShapeIrregular(corners = 30, irregularity = 0.2f, seed = 2L)
			)
	){
		MA_LabelNormal("")
	}
}


