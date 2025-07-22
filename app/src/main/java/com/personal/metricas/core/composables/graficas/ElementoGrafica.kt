package com.personal.metricas.core.composables.graficas

import androidx.compose.ui.graphics.Color
import com.personal.metricas.core.composables.componentes.MA_Colores
import com.personal.metricas.core.composables.tabla.Fila


data class ValoresGrafica(val elementos: List<ElementoGrafica> = emptyList<ElementoGrafica>(),
                          val textoX: String = "Tipo", val textoY: String = "Total") {

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

    companion object {

        fun fromValoresTabla(filas : List<Fila>,columnaTexto: Int, columnaValor: Int): ValoresGrafica {
            var elementos: List<ElementoGrafica> = filas.filter { it.visible }.map { fila ->

                var valorTexto = fila.celdas[columnaTexto].valor
                var valor: Float = fila.celdas[columnaValor].valor.toFloat()
                val color = MA_Colores.obtenerColorAleatorio()
                ElementoGrafica(x = valorTexto, y = valor, leyenda = valorTexto, color = color)

            }


            return ValoresGrafica(elementos = elementos, textoX = "Tipo", textoY = "Valor")
        }

    }

}

data class ElementoGrafica(val x: Any, val y: Float, val leyenda: String = "", var color: Color = MA_Colores.obtenerColorAleatorio())



fun dameValoresTest(): List<ElementoGrafica> {
    var listaValores: List<ElementoGrafica> = emptyList()
    (1..10).forEach {
        listaValores = listaValores.plus(
            ElementoGrafica(x = it.toFloat(), y = it.toFloat(), leyenda = "Ley ${it}", color = Color.Red)
        )
    }
    return listaValores
}