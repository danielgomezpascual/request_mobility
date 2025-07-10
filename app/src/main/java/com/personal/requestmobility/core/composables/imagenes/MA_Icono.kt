package com.personal.requestmobility.core.composables.imagenes

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MA_Icono(
	icono: ImageVector, modifier: Modifier = Modifier, descripcion: String = "",
	color: Color = MaterialTheme.colorScheme.primary,
			onClick: ()->Unit ={} ) {
	Icon(imageVector = icono, contentDescription =
		descripcion, tint = color, modifier = modifier)
}