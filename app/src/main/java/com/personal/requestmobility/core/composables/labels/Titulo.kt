package com.personal.requestmobility.core.composables.labels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign


@Composable
fun Titulo(
    valor: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    fondo : Color = Color.White,
    alineacion : TextAlign = TextAlign.Start,
    icono: Icons? = null,
) {

    Text(text = valor, modifier = modifier.fillMaxWidth().background(color =fondo),
        color = color, style = MaterialTheme.typography.titleLarge, textAlign = alineacion  )
}