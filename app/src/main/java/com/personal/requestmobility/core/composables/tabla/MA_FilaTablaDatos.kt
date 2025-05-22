package com.personal.requestmobility.core.composables.tabla

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.personal.requestmobility.core.composables.componentes.panel.PanelConfiguracion
import com.personal.requestmobility.core.utils.if3

@Composable
fun MA_FilaTablaDatos(fila: Fila, configuracion: PanelConfiguracion, onClick: (Fila) -> Unit) {
    val modifier = Modifier.Companion
    val filasColor = configuracion.filasColor
    val ajustarContenidoAncho = configuracion.ajustarContenidoAncho
    val indicadorColor = configuracion.indicadorColor

    val color: Color = (fila.color).copy(alpha = 0.3f)
    val colorFondo = if3(filasColor, (fila.color).copy(alpha = 0.1f), Color.Companion.Transparent)


    Row(
        modifier = modifier
            .background(if3(fila.seleccionada, color, colorFondo))
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable {
                onClick(fila)
            },
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {


        fila.celdas.forEachIndexed { indice, celda ->

            var modifierFila: Modifier = Modifier.Companion

            if (ajustarContenidoAncho) {
                modifierFila = modifierFila
                    .fillMaxWidth()
                    .weight(1f)
            } else {
                modifierFila = modifierFila.width(fila.size)
            }

            if (indicadorColor && indice == 0) {
                MA_Indicador(modifierFila, fila.color) {
                    celda.contenido(modifierFila)
                }
            } else {
                celda.contenido(modifierFila)
            }

        }

    }
    HorizontalDivider()

}