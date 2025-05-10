package com.personal.requestmobility.transacciones.domain.graficas

import com.personal.requestmobility.core.composables.graficas.ElementoGrafica
import com.personal.requestmobility.transacciones.ui.entidades.TransaccionesUI

class GraficaTransaccionPorTipoCU {

    fun run(listaTransacciones: List<TransaccionesUI>): List<ElementoGrafica> {
        var lista: List<ElementoGrafica> = emptyList()

        val totalTransacciones = listaTransacciones.size.toFloat()
        val res = listaTransacciones.groupBy { it.tipoMov }.mapValues { (str, lista) -> lista.size }



        res.forEach { (clave, valor) ->

            val porcenaje: Float = (valor.toFloat() / totalTransacciones.toFloat()) * 100
            val porcentaje2d = String.format("%.2f", porcenaje)

            lista = lista.plus(
                ElementoGrafica(
                    x = clave,
                    y = valor.toFloat(),
                    "$clave (${porcentaje2d} %)"
                )
            )
        }

        return lista
    }
}