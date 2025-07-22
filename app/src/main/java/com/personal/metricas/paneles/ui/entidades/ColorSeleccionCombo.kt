package com.personal.metricas.paneles.ui.entidades

import androidx.compose.ui.graphics.Color
import com.personal.metricas.paneles.domain.entidades.EsquemaColores

data class ColorSeleccionCombo(val indice: Int, val color: Color, val descricion: String)

class ColoresSeleccion {

   /* fun get(esquema: Int) = EsquemaColores().get(esquema).colores.mapIndexed { indice, color ->
        ColorSeleccionCombo(indice = indice, color = color, descricion = "")
    }*/


    fun get(esquema: Int) = EsquemaColores().dameEsquemaCondiciones().colores.mapIndexed { indice, color ->
        ColorSeleccionCombo(indice = indice, color = color, descricion = "")
    }


}