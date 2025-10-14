package com.personal.metricas.core.composables.imagenes

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import java.io.IOException

fun Context.assetExists(path: String): Boolean {
	return try {
		assets.open(path).use {
			// Si el archivo se abre correctamente, significa que existe.
			// El bloque 'use' se encargará de cerrar el InputStream automáticamente.
			true
		}
	} catch (e: IOException) {
		// Si se lanza una IOException, el archivo no existe.
		false
	}
}

@Composable
fun MA_ImagenAssets(nombreArchivo: String, modifier: Modifier = Modifier) {

	// Construimos la ruta completa que Coil entiende
	val rutaCompletaCoil = "file:///android_asset/$nombreArchivo"

	AsyncImage(
		model = rutaCompletaCoil,
		contentDescription = "bnd",
		modifier = modifier
	)
	
	/*val context = LocalContext.current
	val existe = context.assetExists(nombreArchivo)
	var fileAssets =""
	if (!existe) {
		fileAssets= "banderas/es.png"
		return
	} else {
		fileAssets = nombreArchivo
	}
	val inputStream = context.assets.open(fileAssets)
	val bitmap = BitmapFactory.decodeStream(inputStream)
	Image(bitmap = bitmap.asImageBitmap(),
			contentDescription = "Descripción de tu imagen desde assets")
	
	*/
}

@Preview(showBackground = true)
@Composable
fun PreviewImagenDesdeAssets() {
	Column {
		MA_ImagenAssets("banderas/es.png")
	}
	
}