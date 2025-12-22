package com.personal.metricas.core.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Utilidades para el cálculo de dimensiones en tablas y otros componentes. */
object MA_DimensionUtils {

    /**
     * Calcula el ancho estimado de una celda basado en la longitud del texto.
     * @param texto El contenido de la celda.
     * @param pixelesPorCaracter Cantidad de pixels aproximada por carácter (default 6).
     * @param paddingAdicional Espacio extra en pixels para márgenes (default 20).
     * @return El ancho calculado en Dp.
     */
    fun calcularAnchoEstimadoTexto(
            texto: String,
            pixelesPorCaracter: Int = 6,
            paddingAdicional: Int = 20
    ): Dp {
        if (texto.isEmpty()) return paddingAdicional.dp

        // Un algoritmo simple basado en la longitud del string
        // Se puede mejorar considerando caracteres anchos (W, M) vs estrechos (i, l)
        val longitud = texto.length
        val anchoCalculado = (longitud * pixelesPorCaracter) + paddingAdicional

        return anchoCalculado.dp
    }
}
