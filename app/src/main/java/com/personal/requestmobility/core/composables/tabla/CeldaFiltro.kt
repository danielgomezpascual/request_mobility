package com.personal.requestmobility.core.composables.tabla

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun CeldaFiltro(

    modifier: Modifier = Modifier,
    celda: Celda,
    alineacion: TextAlign = TextAlign.Unspecified,
    icono: Icons? = null,
    onClick: (Celda) -> Unit = {}

) {

    Text(
        text = "[${celda.seleccionada}]:  ${celda.titulo} = ${celda.valor}",
        modifier = modifier
            .background(celda.fondoCelda)
            .padding(4.dp)
            .clickable { onClick(celda) },
        color = celda.colorCelda,
        style = MaterialTheme.typography.bodySmall,
        textAlign = alineacion
    )
}