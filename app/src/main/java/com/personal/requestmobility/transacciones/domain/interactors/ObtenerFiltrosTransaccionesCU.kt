package com.personal.requestmobility.transacciones.domain.interactors

import com.personal.requestmobility.App
import com.personal.requestmobility.transacciones.ui.entidades.Filtro
import com.personal.requestmobility.transacciones.ui.entidades.FiltrosTransacciones
import com.personal.requestmobility.transacciones.ui.entidades.TransaccionesUI

class ObtenerFiltrosTransaccionesCU {



    fun dameFiltrosTransacion(trxSeleccionada: TransaccionesUI?): FiltrosTransacciones {
        if (trxSeleccionada == null) return FiltrosTransacciones()

        val filtros = listOf<Filtro>(

            Filtro.Busqueda(
                identificador = 0,
                campo = "Text: ${trxSeleccionada.programVersion}",
                condicion = { str, trz ->
                                     trz.numero.contains(str, ignoreCase = true)
                }
            ),
            Filtro.Seleccion(
                identificador = 1,
                campo = "programVersion: ${trxSeleccionada.programVersion}",
                condicion = { trz -> trz.programVersion.equals(trxSeleccionada.programVersion) },
            ),
            Filtro.Seleccion(
                identificador = 2,
                campo = "reqStatus_  ${trxSeleccionada.reqStatus}",
                condicion = { trz -> trz.reqStatus == (trxSeleccionada.reqStatus) },
            ),
            Filtro.Seleccion(
                identificador = 3,
                campo = "numero:  ${trxSeleccionada.numero}",
                condicion = { trz -> trz.numero.equals(trxSeleccionada.numero) },
            ),

            Filtro.Seleccion(
                identificador = 4,
                campo = "MobRequest ID:  ${trxSeleccionada.mobRequestId}",
                condicion = { trz -> trz.mobRequestId == trxSeleccionada.mobRequestId },
            ),

            Filtro.Seleccion(
                identificador = 5,
                campo = "Tipo:  ${trxSeleccionada.tipoMov}",
                condicion = { trz -> trz.tipoMov.equals(trxSeleccionada.tipoMov) },
            ),
        )

        val fts: FiltrosTransacciones = FiltrosTransacciones(filtros = filtros)
        return fts
    }


}