package com.personal.requestmobility.transacciones.ui.screens.listado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.core.composables.graficas.ElementoGrafica
import com.personal.requestmobility.transacciones.domain.graficas.GraficaDistribucionErrores
import com.personal.requestmobility.transacciones.domain.graficas.GraficaErroresPorTipo
import com.personal.requestmobility.transacciones.domain.interactors.AplicarFiltrosTransaccionesCU
import com.personal.requestmobility.transacciones.domain.graficas.GraficaTransaccionPorTipoCU
import com.personal.requestmobility.transacciones.domain.interactors.ObtenerTransaccionesCU
import com.personal.requestmobility.transacciones.domain.graficas.GraficaTransaccionesEstadoCU
import com.personal.requestmobility.transacciones.domain.graficas.ResumenTrx
import com.personal.requestmobility.transacciones.domain.interactors.ObtenerKPIsCU
import com.personal.requestmobility.transacciones.ui.entidades.Filtro
import com.personal.requestmobility.transacciones.ui.entidades.FiltrosTransacciones
import com.personal.requestmobility.transacciones.ui.entidades.KpiUI
import com.personal.requestmobility.transacciones.ui.entidades.TransaccionesUI
import com.personal.requestmobility.transacciones.ui.entidades.fromTransacciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DockTransaccionesVM(
    private val aplicarFiltrosTransaccionesCU: AplicarFiltrosTransaccionesCU,
    private val obtenerTransaccionesCU: ObtenerTransaccionesCU,

    private val obtenerKPI: ObtenerKPIsCU,
    private val resumenTrxCU: ResumenTrx,
    private val contarTransaccionesTipoCU: GraficaTransaccionPorTipoCU,
    private val graficaTransaccionesEstadoCU: GraficaTransaccionesEstadoCU,
    private val distribucionErrores: GraficaDistribucionErrores,
    private val erroresTipo: GraficaErroresPorTipo

) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    var textoBuscar: String = ""
    var listaTransacciones: List<TransaccionesUI> = emptyList()
    var filtrosTrx: FiltrosTransacciones = FiltrosTransacciones()
    var kpisUI: List<KpiUI> = emptyList()


    sealed class UIState {
        data class Success(val textoBuscar: String = "",
                           val transacciones: List<TransaccionesUI>,
                           val filtrosTransaccion: FiltrosTransacciones,
                           val kpis  : List<KpiUI> = emptyList<KpiUI>(),

                           val valoresResumenTransacciones: List<ElementoGrafica> = emptyList<ElementoGrafica>(),
                           val valoresGraficaTiposTransacciones: List<ElementoGrafica> = emptyList<ElementoGrafica>(),
                           val valoresGraficaEstadosTiposProcesamiento: List<ElementoGrafica> = emptyList<ElementoGrafica>(),
                           val valoresGraficaDistribucionErroresPorTipo: List<ElementoGrafica> = emptyList<ElementoGrafica>(),
                           val valoresGraficaErroresTipo: List<ElementoGrafica> = emptyList<ElementoGrafica>()
        ) : UIState()

        data class Error(val message: String) : UIState()
        object Loading : UIState()
    }

    sealed class Eventos {
        object Cargar : Eventos()
        object CalcularGraficasTransaccionesPresentadas : Eventos()
        object BorrarFiltrosAplicados : Eventos()
        data class ModicarSeleccionFiltro(val filtro: Filtro.Seleccion) : Eventos()
        data class InvertirSeleccionFiltro(val filtro: Filtro.Seleccion) : Eventos()
        data class ModificarTextoBuscar(val texto: String) : Eventos()
        data class ModificarSeleccionTransaccion(val trx: TransaccionesUI) : Eventos()
    }


    fun onEvent(evento: Eventos) {
        when (evento) {
            Eventos.Cargar -> cargaInicial()

            is Eventos.ModificarSeleccionTransaccion -> modificarTransaccionSeleccionada(evento.trx)
            is Eventos.InvertirSeleccionFiltro -> invertirFiltro(evento.filtro)
            is Eventos.ModicarSeleccionFiltro -> modificarSeleccionFiltro(evento.filtro)
            is Eventos.ModificarTextoBuscar -> modificarTextoBusqueta(evento.texto)
            Eventos.BorrarFiltrosAplicados -> borrarFiltrosAplicados()
            Eventos.CalcularGraficasTransaccionesPresentadas -> recalcularGraficasTrx()
        }
    }

    private fun recalcularGraficasTrx(lt: List<TransaccionesUI> = emptyList<TransaccionesUI>() ) {

        var updTrx : List<TransaccionesUI> =  lt
        if (lt.isEmpty()){
             updTrx = aplicarFiltrosTransaccionesCU.aplicarFiltrosTransacciones(
                filtros = filtrosTrx,
                listaTransacciones = listaTransacciones
            )
        }



        _uiState.update { estado ->
            if (estado is UIState.Success) {



                estado.copy(
                    valoresGraficaTiposTransacciones = contarTransaccionesTipoCU.run(updTrx),
                    valoresGraficaEstadosTiposProcesamiento = graficaTransaccionesEstadoCU.run(updTrx),
                    valoresGraficaDistribucionErroresPorTipo = distribucionErrores.run(updTrx),
                    valoresGraficaErroresTipo = erroresTipo.run(updTrx)
                )

            } else {
                estado
            }
        }

    }


    private fun cargaInicial() {
        viewModelScope.launch {

            listaTransacciones = obtenerTransaccionesCU.getTransacciones().map { TransaccionesUI().fromTransacciones(it) }.toMutableList()
            filtrosTrx = aplicarFiltrosTransaccionesCU.obtenerFiltrosPorTransaccion(listaTransacciones.firstOrNull())// obtenerFiltrosTransaccionesCU.dameFiltrosTransacion(listaTransacciones.firstOrNull())

            kpisUI = obtenerKPI.obtenerListaKPIs().mapIndexed { i, kpi -> KpiUI.from(kpi, i ) }


            _uiState.value = UIState.Success(
                textoBuscar = "",
                transacciones = listaTransacciones,
                filtrosTransaccion = filtrosTrx,
                kpis =  kpisUI,

                valoresGraficaTiposTransacciones = contarTransaccionesTipoCU.run(listaTransacciones),
                valoresGraficaEstadosTiposProcesamiento = graficaTransaccionesEstadoCU.run(listaTransacciones),
                valoresGraficaDistribucionErroresPorTipo = distribucionErrores.run(listaTransacciones),
                valoresGraficaErroresTipo = erroresTipo.run(listaTransacciones),

                )
        }
    }


    private fun modificarTransaccionSeleccionada(trx: TransaccionesUI) {
        val upd = listaTransacciones.map { t -> t.copy(seleccionada = t.mobRequestId == trx.mobRequestId) }
        val ft: FiltrosTransacciones = aplicarFiltrosTransaccionesCU.obtenerFiltrosPorTransaccion(trx)
        actualizarEstadoFiltros(ft, upd)
    }

    private fun modificarTextoBusqueta(texto: String) {
        textoBuscar = texto
        val ft: FiltrosTransacciones = filtrosTrx.copy(filtros = aplicarFiltrosTransaccionesCU.seleccionFiltroBusqueda(filtrosTrx, texto))
        actualizarEstadoFiltros(ft, listaTransacciones)
    }

    private fun borrarFiltrosAplicados() {

        val ft = aplicarFiltrosTransaccionesCU.borrarFiltros(filtrosTrx)
        val upd = listaTransacciones.map { it.copy(visible = true) }
        actualizarEstadoFiltros(ft, upd)

    }


    private fun modificarSeleccionFiltro(filtro: Filtro.Seleccion) {
        val ft: FiltrosTransacciones = filtrosTrx.copy(filtros = aplicarFiltrosTransaccionesCU.seleccionFiltroPropiedad(filtrosTrx, filtro))
        actualizarEstadoFiltros(ft, listaTransacciones)
    }

    private fun invertirFiltro(filtro: Filtro.Seleccion) {
        val ft: FiltrosTransacciones = filtrosTrx.copy(filtros = aplicarFiltrosTransaccionesCU.invertirFiltroPropiedad(filtrosTrx, filtro))
        actualizarEstadoFiltros(ft, listaTransacciones)
    }


    private fun actualizarEstadoFiltros(fs: FiltrosTransacciones, trx: List<TransaccionesUI>) {

        listaTransacciones = trx
        filtrosTrx = fs

        val updTrx = aplicarFiltrosTransaccionesCU.aplicarFiltrosTransacciones(
            filtros = filtrosTrx,
            listaTransacciones = listaTransacciones
        )
        //      App.log.lista("Filtyros", FiltrosTransacciones(filtros = fs).filtros)
        _uiState.update { estado ->
            if (estado is UIState.Success) {
               // recalcularGraficasTrx(updTrx)
                estado.copy(
                    textoBuscar = textoBuscar,
                    filtrosTransaccion = filtrosTrx,
                    transacciones = updTrx,

                    valoresGraficaTiposTransacciones = contarTransaccionesTipoCU.run(updTrx),
                    valoresGraficaEstadosTiposProcesamiento = graficaTransaccionesEstadoCU.run(updTrx),
                    valoresGraficaDistribucionErroresPorTipo = distribucionErrores.run(updTrx),
                    valoresGraficaErroresTipo = erroresTipo.run(updTrx)


                )


            } else {
                estado
            }
        }
    }


}



