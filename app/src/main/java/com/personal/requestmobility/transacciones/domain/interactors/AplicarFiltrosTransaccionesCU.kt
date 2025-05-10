package com.personal.requestmobility.transacciones.domain.interactors

import com.personal.requestmobility.transacciones.ui.entidades.Filtro
import com.personal.requestmobility.transacciones.ui.entidades.FiltrosTransacciones
import com.personal.requestmobility.transacciones.ui.entidades.TransaccionesUI

class AplicarFiltrosTransaccionesCU(private val obtenerFiltrosTransaccionesCU: ObtenerFiltrosTransaccionesCU) {

    fun obtenerFiltrosPorTransaccion(trx: TransaccionesUI?) : FiltrosTransacciones{
        return  obtenerFiltrosTransaccionesCU.dameFiltrosTransacion(trx)
    }

    fun borrarFiltros(ft: FiltrosTransacciones): FiltrosTransacciones {

        val ul = ft.filtros.map { f ->
            when (f) {
                is Filtro.Busqueda -> f.copy(seleccionado = false)
                is Filtro.Seleccion -> f.copy(seleccionado = false, invertido = false)
            }
        }

        return FiltrosTransacciones(filtros =  ul)
    }


    fun seleccionFiltroBusqueda(filtros: FiltrosTransacciones, textoBuscar: String): List<Filtro> {

        return filtros.filtros.map { filtro ->
            if (filtro is Filtro.Busqueda) {
                filtro.copy(seleccionado = !textoBuscar.isEmpty(), valorBuscar = textoBuscar)
            } else {
                filtro
            }
        }

    }

    fun seleccionFiltroPropiedad(filtros: FiltrosTransacciones, filtroSeleccionado: Filtro.Seleccion): List<Filtro> {
        return filtros.filtros.map { filtro ->
            if (filtro is Filtro.Seleccion && filtro.identificador == filtroSeleccionado.identificador) {
                filtroSeleccionado.copy(seleccionado = !filtroSeleccionado.seleccionado)
            } else {
                filtro
            }
        }
    }

    fun invertirFiltroPropiedad(filtros: FiltrosTransacciones, filtroSeleccionado: Filtro.Seleccion): List<Filtro> {
        return filtros.filtros.map { filtro ->
            if (filtro is Filtro.Seleccion && filtro.identificador == filtroSeleccionado.identificador) {
                filtroSeleccionado.copy(invertido = !filtroSeleccionado.invertido)
            } else {
                filtro
            }
        }
    }


    fun aplicarFiltrosTransacciones(

        filtros: FiltrosTransacciones,
        listaTransacciones: List<TransaccionesUI>): List<TransaccionesUI> {

        val upd = listaTransacciones.map { trx ->
            var visible: Boolean = true
            filtros.filtros.forEach { filtro ->

                when (filtro) {
                    is Filtro.Busqueda -> {
                        val textoBuscar: String = filtro.valorBuscar
                        if (!textoBuscar.isEmpty()) {
                            if (!filtro.condicion(textoBuscar, trx)) {
                                visible = false
                            }
                        }
                    }

                    is Filtro.Seleccion -> {
                        if (filtro.seleccionado) {
                            if (filtro.invertido) {
                                if (filtro.condicion(trx)) {
                                    visible = false
                                }
                            } else {
                                if (!filtro.condicion(trx)) {
                                    visible = false
                                }
                            }
                        }
                    }


                }
            }

            trx.copy(visible = visible)
        }
        return upd.filter { it.visible }

    }
}