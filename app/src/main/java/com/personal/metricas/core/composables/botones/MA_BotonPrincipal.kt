package com.personal.metricas.core.composables.botones

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.imagenes.MA_Icono

@Composable
fun MA_BotonPrincipal(
	texto: String,
	modifier: Modifier = Modifier,
	icono: ImageVector? = null,
	onClick: () -> Unit,
) {
	Button(onClick = onClick, modifier = modifier.padding(3.dp)) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			if (icono != null) {
				MA_Icono(icono = icono, color = MaterialTheme.colorScheme.inversePrimary)
			}
			Text(text = texto)
		}

	}
}