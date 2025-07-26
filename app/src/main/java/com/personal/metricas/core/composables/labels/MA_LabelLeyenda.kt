package com.personal.metricas.core.composables.labels

import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign


@Composable
fun MA_LabelLeyenda(
	valor: String,
	modifier: Modifier = Modifier,
	color: Color = Color.Blue,
	alineacion: TextAlign = TextAlign.Unspecified,
	icono: Icons? = null,

	) {
	Text(text = valor, modifier = modifier, color = color,
		 style = MaterialTheme.typography.bodySmall,
		 fontStyle = FontStyle.Italic, textAlign = alineacion

	)
}