package com.personal.requestmobility.core.composables.tabla

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.formas.MA_Circulo
import com.personal.requestmobility.core.utils.if3


@Composable
fun MA_CeldaFiltro(

    modifier: Modifier = Modifier,
    celda: Celda,
    alineacion: TextAlign = TextAlign.Unspecified,
    icono: Icons? = null,
    onClickSeleccion: (Celda) -> Unit = {},
    onClickInvertir: (Celda) -> Unit = {},

) {

    Row(modifier = Modifier.clickable { onClickSeleccion(celda) },) {

        MA_Circulo(color = if3(celda.seleccionada, Color.Yellow, Color.LightGray))
        Text(
            text = "[${celda.seleccionada}]:  ${celda.titulo} = ${celda.valor}",
            modifier = modifier
                .background(celda.fondoCelda)
                .padding(4.dp),

            color = celda.colorCelda,
            style = MaterialTheme.typography.bodySmall,
            textAlign = alineacion
        )
        Button(onClick = {onClickInvertir(celda)}) { MA_LabelCelda("Invertir")}
    }

}