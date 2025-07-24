package com.personal.metricas.core.composables.graficas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.labels.MA_LabelNormal

@Composable
fun MA_Indicador(modifier: Modifier = Modifier, texto: String, valor: String, color: Color) {

	Box(modifier = Modifier
		//.defaultMinSize(250.dp, 250.dp)
		.size(150.dp)

		.padding(5.dp)
		.background(color = Color.Black)) {
		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.fillMaxSize()
		) {

			Text(
				text = valor, modifier = Modifier

					.padding(6.dp),
				color = color,
				// style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight(800),
				fontSize = TextUnit(40.0f, TextUnitType.Sp),
				textAlign = TextAlign.Center
			)

			MA_LabelNormal(valor = texto, color = Color.White, modifier = Modifier)
			//   MA_Titulo(valor = y.toString(), color = Color.Green)
			//MA_Titulo(valor =texto, color = Color.White)

		}

	}


}