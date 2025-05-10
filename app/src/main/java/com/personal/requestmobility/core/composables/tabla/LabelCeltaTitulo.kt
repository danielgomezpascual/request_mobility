package com.personal.requestmobility.core.composables.tabla

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
fun LabelCeldaTitulo(
    valor: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fondo : Color = Color.DarkGray,
    alineacion : TextAlign = TextAlign.Unspecified,
    icono: Icons? = null,
) {

    Text(text = valor, modifier = modifier.fillMaxWidth().background(color =fondo),
        color = color, style = MaterialTheme.typography.bodySmall, textAlign = alineacion  )
}