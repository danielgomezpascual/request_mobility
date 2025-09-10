package com.personal.metricas.core.composables.tabla

import MA_IconBottom
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.botones.MA_BotonSecundarioSinBorde
import com.personal.metricas.core.composables.formas.MA_Circulo
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.utils.if3


@Composable
fun MA_CeldaFiltro(

	modifier: Modifier = Modifier,
	celda: Celda,
	alineacion: TextAlign = TextAlign.Unspecified,
	icono: Icons? = null,
	onClickSeleccion: (Celda) -> Unit = {},
	onClickInvertir: (Celda) -> Unit = {},

	) {

	Row(verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.fillMaxWidth().clickable { onClickSeleccion(celda) }) {

		//MA_Circulo(color = if3(celda.seleccionada, Color.Yellow, Color.LightGray))
		// Column {
		MA_IconBottom(icon = Icons.Default.LightMode,
					  color = if3(celda.seleccionada, Color.Magenta, Color.LightGray), onClick = {
			onClickSeleccion(celda)
		})

		//MA_Icono(Icons.Default.LightMode, color = if3(celda.seleccionada, Color.Magenta, Color.LightGray))
		MA_Spacer()
		MA_IconBottom(icon = Icons.Default.InvertColors,
					  color = if3(celda.filtroInvertido, Color.Magenta, Color.LightGray), onClick = {
			onClickInvertir(celda)
		})
		//MA_BotonSecundarioSinBorde(onClick = {onClickInvertir(celda)},  texto = "INV" )


		//   }
		MA_Spacer()
		MA_LabelMini(valor = "${celda.titulo} = ${celda.valor}")
		/*Text(
			text = "${celda.titulo} = ${celda.valor}",
			modifier = modifier
				.background(celda.fondoCelda)
				.padding(4.dp),

			color = celda.colorCelda,
			style = MaterialTheme.typography.bodySmall,
			textAlign = alineacion
		)*/


		//MA_BotonSecundarioSinBorde(onClick = {onClickInvertir(celda)},  texto = "Invertir" )
	}

}