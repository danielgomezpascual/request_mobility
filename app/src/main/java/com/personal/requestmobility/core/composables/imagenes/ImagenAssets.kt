package com.personal.requestmobility.core.composables.imagenes

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.core.net.toUri
import com.personal.requestmobility.R


@Composable
fun ImagenDesdeAssets(nombreArchivo: String, modifier: Modifier = Modifier) {
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
        ImagenDesdeAssets("banderas/es.png")
    }

}