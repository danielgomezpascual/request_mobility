package com.personal.metricas.transacciones.domain.graficas

import com.personal.metricas.core.composables.graficas.ElementoGrafica
import com.personal.metricas.transacciones.ui.entidades.EstadoProceso
import com.personal.metricas.transacciones.ui.entidades.TransaccionesUI

class GraficaDistribucionErrores {

    fun run(listaTransacciones: List<TransaccionesUI>): List<ElementoGrafica> {
        var lista: List<ElementoGrafica> = emptyList()

        val trxError = listaTransacciones.filter { it.estado == EstadoProceso.ERROR }
        val totalTransacciones = trxError.size.toFloat()
        val res = trxError.groupBy { it.tipoMov }.mapValues { (int, lista) -> lista.size }


        res.forEach { (clave, valor) ->

            val porcenaje: Float = (valor.toFloat() / totalTransacciones.toFloat()) * 100
            val porcentaje2d = String.format("%.2f", porcenaje)


            val txt = "$clave ($porcentaje2d%)"

            lista = lista.plus(
                ElementoGrafica(
                    x = valor.toFloat(),
                    y = valor.toFloat(),
                    leyenda = txt,
                )
            )
        }
        return lista

    }
}