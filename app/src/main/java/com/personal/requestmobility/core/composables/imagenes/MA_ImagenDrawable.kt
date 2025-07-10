package com.personal.requestmobility.core.composables.imagenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.R


@Preview
@Composable
fun TestImagen() {
    MA_ImagenDrawable(imagen = R.drawable.ic_launcher_foreground)
}

@Composable
fun MA_ImagenDrawable( imagen: Int, s: Dp = 48.dp) {
    Image(
        modifier = Modifier.width(s),
        painter = painterResource(id = imagen),
        contentDescription = "Descripción de tu imagen"
    )
}