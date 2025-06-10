package com.personal.requestmobility.paneles.ui.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.tabla.MA_LabelCelda
import com.personal.requestmobility.paneles.domain.entidades.EsquemaColores

@Preview
@Composable
fun Test_MASelectorColores() {



    MA_SelectorEsquemaColores(EsquemaColores().get(0))
}

@Composable
fun MA_SelectorEsquemaColores(esquema: EsquemaColores) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            esquema.colores.forEach { c ->
                Box(
                    modifier = Modifier
                        .background(c)
                        .width(10.dp)
                        .height(10.dp)
                )

            }
        }

        MA_LabelCelda(esquema.nombre)

    }

}