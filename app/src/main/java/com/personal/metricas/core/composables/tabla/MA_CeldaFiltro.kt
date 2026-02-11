package com.personal.metricas.core.composables.tabla

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.utils.Utils

@Composable
fun MA_CeldaFiltro(
	modifier: Modifier = Modifier,
	celda: Celda,
	alineacion: TextAlign = TextAlign.Unspecified,
	onClickSeleccion: (Celda) -> Unit = {},
	onClickInvertir: (Celda) -> Unit = {},
	onClickAbrirMobility: (Celda) -> Unit = {},
) {
	val colorActivo = Color(0xFFFFB74D) // Un violeta premium
	val colorInvertido = Color(0xFFE91E63) // Rosa para inversión

	Surface(
		modifier = modifier
			.padding(vertical = 4.dp, horizontal = 2.dp)
			.fillMaxWidth(),
		shape = RoundedCornerShape(12.dp),
		color =
			if (celda.seleccionada) colorActivo.copy(alpha = 0.08f)
			else MaterialTheme.colorScheme.surface,
		border =
			BorderStroke(
				width = 1.dp,
				color =
					when {
						celda.filtroInvertido -> colorInvertido
						celda.seleccionada    -> colorActivo
						else                  -> MaterialTheme.colorScheme.outlineVariant
					}
			),
		tonalElevation = if (celda.seleccionada) 2.dp else 0.dp
	) {
		Row(
			modifier = Modifier
				.clickable { onClickSeleccion(celda) }
				.padding(12.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			// Indicador de estado principal (Selección)
			Icon(
				imageVector = Icons.Default.FilterAlt,
				contentDescription = null,
				tint =
					if (celda.seleccionada) colorActivo
					else MaterialTheme.colorScheme.outline,
				modifier = Modifier.size(20.dp)
			)

			MA_Spacer(Modifier.width(12.dp))

			// Texto del filtro
			Column(modifier = Modifier.weight(1f)) {
				MA_LabelNormal(
					valor = celda.titulo,
					color =
						if (celda.seleccionada) colorActivo
						else MaterialTheme.colorScheme.onSurface,
					size = 12.sp
				)
				Text(
					text = celda.valor.toString(),
					style = MaterialTheme.typography.bodyMedium,
					color =
						if (celda.seleccionada)
							colorActivo.copy(alpha = 0.7f)
						else MaterialTheme.colorScheme.onSurfaceVariant,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)

				if (celda.titulo.equals("MOB_REQUEST_ID", ignoreCase = true)) {
					val context =
						androidx.compose.ui.platform.LocalContext.current

					if (Utils.isAppInstalled(context, "com.maxam.maxamgestioninventarioapp")) {
						MA_Spacer(Modifier.padding(top = 8.dp))
						MA_BotonSecundario(
							texto = "Abrir en Mobility",
							onClick = { onClickAbrirMobility(celda) },
							modifier = Modifier.fillMaxWidth()
						)
					}
				}
			}
		}

		// Botón de inversión
		Surface(
			onClick = { onClickInvertir(celda) },
			shape = RoundedCornerShape(8.dp),
			color =
				if (celda.filtroInvertido) colorInvertido
				else Color.Transparent,
			modifier = Modifier.size(36.dp)
		) {
			Box(contentAlignment = Alignment.Center) {
				Icon(
					imageVector = Icons.Default.Block,
					contentDescription = "Invertir",
					tint =
						if (celda.filtroInvertido) Color.White
						else MaterialTheme.colorScheme.outline,
					modifier = Modifier.size(18.dp)
				)
			}
		}
	}
}


