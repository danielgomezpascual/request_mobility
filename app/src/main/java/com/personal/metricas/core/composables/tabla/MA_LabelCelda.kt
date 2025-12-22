package com.personal.metricas.core.composables.tabla

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun MA_LabelCelda(
        valor: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Black,
        fondo: Color = Color.White,
        alineacion: TextAlign = TextAlign.Unspecified,
        icono: Icons? = null,
        textoCompleto: Boolean = false,
        horizontal: Boolean = false,
) {
        Text(
                text = valor,
                modifier = modifier.padding(4.dp),
                color = color,
                style = MaterialTheme.typography.bodySmall,
                textAlign = alineacion,
                maxLines = if (textoCompleto && !horizontal) Int.MAX_VALUE else 1,
                overflow = if (textoCompleto) TextOverflow.Clip else TextOverflow.Ellipsis,
                softWrap = !horizontal
        )
}
