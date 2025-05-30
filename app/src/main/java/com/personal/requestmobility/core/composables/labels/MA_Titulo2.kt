package com.personal.requestmobility.core.composables.labels

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun MA_Titulo2(
    valor: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    fondo: Color = Color.White,
    alineacion: TextAlign = TextAlign.Start,
    icono: Icons? = null,
) {

        Text(
            text = valor, modifier = modifier.fillMaxWidth().padding(8.dp),
            color = color, style = MaterialTheme.typography.titleMedium, textAlign = alineacion
        )
    HorizontalDivider()

}
