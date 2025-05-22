package com.personal.requestmobility.core.composables.imagenes

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun MA_ImagenAssets(nombreArchivo: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val inputStream = context.assets.open(nombreArchivo)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Descripción de tu imagen desde assets"
    )



}

@Preview(showBackground = true)
@Composable
fun PreviewImagenDesdeAssets() {
    Column {
        MA_ImagenAssets("banderas/es.png")
    }

}