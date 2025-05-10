package com.personal.requestmobility.transacciones.domain.graficas

import com.personal.requestmobility.core.composables.graficas.ElementoGrafica
import com.personal.requestmobility.transacciones.ui.entidades.EstadoProceso
import com.personal.requestmobility.transacciones.ui.entidades.TransaccionesUI

class GraficaErroresPorTipo {

    fun run(listaTransacciones: List<TransaccionesUI>): List<ElementoGrafica> {
        var lista: List<ElementoGrafica> = emptyList()

        val trxError = listaTransacciones.filter { it.estado == EstadoProceso.ERROR }

        val res = trxError.groupBy { it.tipoMov }.map { (tipoMov, lista) -> tipoMov to lista.size }.toMap()




        res.forEach { (clave, valor) ->
            lista = lista.plus(
                ElementoGrafica(
                    x = clave,
                    y = valor.toFloat(),
                    leyenda = clave,
                )
            )
        }
        return lista

    }
}