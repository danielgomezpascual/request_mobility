package com.personal.metricas.core.composables.imagenes

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MA_ImagenCirculoURL(url: String, size: Dp = 46.dp, descripcion: String = "Imagen URL"){
	AsyncImage(
		model = url,
		contentDescription = descripcion,
		modifier = Modifier.size(size).clip(RoundedCornerShape(50.dp)) // Define el tamaño de la imagen,

	)
}
