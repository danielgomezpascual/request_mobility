package com.personal.requestmobility.transacciones.domain.graficas

import com.personal.requestmobility.core.composables.graficas.ElementoGrafica
import com.personal.requestmobility.transacciones.ui.entidades.EstadoProceso
import com.personal.requestmobility.transacciones.ui.entidades.TransaccionesUI

class GraficaTransaccionesEstadoCU {

    fun run(listaTransacciones: List<TransaccionesUI>): List<ElementoGrafica> {
        var lista: List<ElementoGrafica> = emptyList()

        val totalTransacciones = listaTransacciones.size.toFloat()
        val res = listaTransacciones.groupBy { it.reqStatus }.mapValues { (int, lista) -> lista.size }


        res.forEach { (clave, valor) ->

            val porcenaje: Float = (valor.toFloat() / totalTransacciones.toFloat()) * 100
            val porcentaje2d = String.format("%.2f", porcenaje)

            val textoEstado = EstadoProceso.fromReqStatus(clave).toString()
            val txt = "$porcentaje2d%"

            lista = lista.plus(
                ElementoGrafica(
                    x = textoEstado,
                    y = valor.toFloat(),
                    leyenda = txt,
                )
            )
        }
        return lista

    }
}