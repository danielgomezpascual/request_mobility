package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.tabla.MA_LabelCelda
import com.personal.metricas.paneles.domain.entidades.EsquemaColores

@Preview
@Composable
fun Test_MASelectorColores() {



    MA_SelectorEsquemaColores(EsquemaColores().get(EsquemaColores.Paletas.NORMAL.valor))
}

@Composable
fun MA_SelectorEsquemaColores(esquema: EsquemaColores) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {

            esquema.colores.forEach { c ->
                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .border(width = 1.dp, color = Color.Black)
                        .background(c)
                        .width(24.dp)
                        .height(24.dp)

                )

            }
        }

        MA_LabelCelda(esquema.nombre)

    }

}