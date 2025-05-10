package com.personal.requestmobility.transacciones.ui.screens.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.botones.BotonNormal
import com.personal.requestmobility.core.composables.labels.LabelNormal
import com.personal.requestmobility.transacciones.ui.entidades.Filtro


@Composable
fun ItemFiltroTransaccion(filtro: Filtro.Seleccion, onClick: (Filtro.Seleccion) -> Unit,
                          onLongClick: (Filtro.Seleccion) -> Unit) {
    Column() {

        var estado by remember { mutableStateOf<Boolean>(filtro.seleccionado) }
        var invertir by remember { mutableStateOf<Boolean>(filtro.invertido) }

        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .background(
                    color = if (estado) {
                        Color.Yellow
                    } else {
                        Color.White
                    }
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            App.log.d("Long clkick")
                            invertir = !invertir
                            onLongClick(filtro)
                        }
                    )
                }
                .clickable {
                    App.log.d("Cklic")
                    estado = !estado
                    onClick(filtro)
                }
                .padding(4.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {


            LabelNormal(estado.toString() + "|")
            LabelNormal(filtro.campo)
            LabelNormal(filtro.descripcion)
            BotonNormal("Invertir") {
                onLongClick(filtro)
            }


        }

        HorizontalDivider()
    }
}