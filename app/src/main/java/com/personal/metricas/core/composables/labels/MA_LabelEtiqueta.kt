package com.personal.metricas.core.composables.labels

import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun MA_LabelEtiqueta(
    valor: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    sizeLetras: TextUnit = 14.sp,
    alineacion : TextAlign = TextAlign.Unspecified,
    icono: Icons? = null,

    ) {

    Text(text = valor,
         modifier = modifier,
         color = color,
         fontSize = sizeLetras.value.sp,
         textAlign =  alineacion
        )
}