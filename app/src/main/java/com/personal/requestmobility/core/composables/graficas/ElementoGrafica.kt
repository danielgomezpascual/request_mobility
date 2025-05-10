package com.personal.requestmobility.core.composables.graficas

import androidx.compose.ui.graphics.Color
import com.personal.requestmobility.core.composables.componentes.Colores


data class ValoresGrafica(val elementos: List<ElementoGrafica> = emptyList<ElementoGrafica>(), val textoX: String = "Tipo", val textoY: String = "Total") {

    fun dameElementosOrdenados() = elementos.sortedByDescending { it.y }
    fun dameElementosTruncados(limite: Int, agrupar: Boolean): List<ElementoGrafica> {
        if (limite == 0) return elementos
        val elementosHastaLimite = elementos.take(limite)

        if (!agrupar ) return elementosHastaLimite
        if (elementos.size > limite) {
            val elemetosDespuesLimite = elementos.drop(limite)
            val totalResto = elemetosDespuesLimite.sumOf { it.y.toDouble() }.toFloat() // Realizamos la suma en Double y luego convertimos a Float
            val valorResto = ElementoGrafica(x = "Resto", y = totalResto, leyenda = "Resto")
            return (elementosHastaLimite + valorResto)
        } else {
            return elementosHastaLimite
        }

    }

    fun tieneContenido(): Boolean = (elementos.isNotEmpty())

}

data class ElementoGrafica(val x: Any, val y: Float, val leyenda: String = "", var color: Color = Colores.obtenerColorAleatorio())



fun dameValoresTest(): List<ElementoGrafica> {
    var listaValores: List<ElementoGrafica> = emptyList()
    (1..10).forEach {
        listaValores = listaValores.plus(
            ElementoGrafica(x = it.toFloat(), y = it.toFloat(), leyenda = "Ley ${it}", color = Color.Red)
        )
    }
    return listaValores
}