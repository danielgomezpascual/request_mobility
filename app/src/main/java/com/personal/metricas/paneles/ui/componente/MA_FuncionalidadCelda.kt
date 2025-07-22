package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun MA_FuncionalidadCelda(
    valor: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
    alineacion : TextAlign = TextAlign.Unspecified,
    icono: Icons? = null,

    ) {
    Box(Modifier.padding(8.dp    )){
        Row() {
            Icon(
                imageVector =  Icons.Default.Functions,
                contentDescription = "", // La descripción de contenido se maneja en el Column clickeable
                modifier = Modifier.size( 24.dp) // Tamaño estándar para iconos en Material 3
            )

            Text(
                text = valor, modifier = modifier, color = color,
                textAlign =  alineacion,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }

}