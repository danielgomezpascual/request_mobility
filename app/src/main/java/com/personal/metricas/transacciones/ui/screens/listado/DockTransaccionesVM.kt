package com.personal.metricas.transacciones.ui.screens.listado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.kpi.domain.interactors.ObtenerKpisCU
import com.personal.metricas.kpi.ui.entidades.KpiUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class DockTransaccionesVM(
    /* private val aplicarFiltrosTransaccionesCU: AplicarFiltrosTransaccionesCU,
     private val obtenerTransaccionesCU: ObtenerTransaccionesCU,*/

    private val obtenerKPI: ObtenerKpisCU,
    /* private val resumenTrxCU: ResumenTrx,
     private val contarTransaccionesTipoCU: GraficaTransaccionPorTipoCU,
     private val graficaTransaccionesEstadoCU: GraficaTransaccionesEstadoCU,
     private val distribucionErrores: GraficaDistribucionErrores,
     private val erroresTipo: GraficaErroresPorTipo*/

) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    //  var textoBuscar: String = ""
    // var listaTransacciones: List<TransaccionesUI> = emptyList()
    // var filtrosTrx: FiltrosTransacciones = FiltrosTransacciones()
    var kpisUI: List<KpiUI> = emptyList()


    sealed class UIState {
        data class Success(
//val textoBuscar: String = "",
            //val transacciones: List<TransaccionesUI>,
            //val filtrosTransaccion: FiltrosTransacciones,
            val kpis: List<KpiUI> = emptyList<KpiUI>(),

            /*  val valoresResumenTransacciones: List<ElementoGrafica> = emptyList<ElementoGrafica>(),
              val valoresGraficaTiposTransacciones: List<ElementoGrafica> = emptyList<ElementoGrafica>(),
              val valoresGraficaEstadosTiposProcesamiento: List<ElementoGrafica> = emptyList<ElementoGrafica>(),
              val valoresGraficaDistribucionErroresPorTipo: List<ElementoGrafica> = emptyList<ElementoGrafica>(),
              val valoresGraficaErroresTipo: List<ElementoGrafica> = emptyList<ElementoGrafica>()*/
        ) : UIState()

        data class Error(val message: String) : UIState()
        object Loading : UIState()
    }

    sealed class Eventos {
        object Cargar : Eventos()
        /* object CalcularGraficasTransaccionesPresentadas : Eventos()
         object BorrarFiltrosAplicados : Eventos()
      /  data class ModicarSeleccionFiltro(val filtro: Filtro.Seleccion) : Eventos()
         data class InvertirSeleccionFiltro(val filtro: Filtro.Seleccion) : Eventos()
         data class ModificarTextoBuscar(val texto: String) : Eventos()
         data class ModificarSeleccionTransaccion(val trx: TransaccionesUI) : Eventos()*/
    }


    fun onEvent(evento: Eventos) {
        when (evento) {
            Eventos.Cargar -> cargaInicial()

            /*    is Eventos.ModificarSeleccionTransaccion -> modificarTransaccionSeleccionada(evento.trx)
                is Eventos.InvertirSeleccionFiltro -> invertirFiltro(evento.filtro)
                is Eventos.ModicarSeleccionFiltro -> modificarSeleccionFiltro(evento.filtro)
                is Eventos.ModificarTextoBuscar -> modificarTextoBusqueta(evento.texto)
                Eventos.BorrarFiltrosAplicados -> borrarFiltrosAplicados()
                Eventos.CalcularGraficasTransaccionesPresentadas -> recalcularGraficasTrx()*/
        }
    }
    /*
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

        }*/


    private fun cargaInicial() {
        viewModelScope.launch {
            obtenerKPI.getAll().collect { listaKpis ->
                kpisUI = listaKpis.mapIndexed { index, kpi -> KpiUI.from(kpi, index) }
                _uiState.value = UIState.Success(
                    kpis = kpisUI
                )
            }
        }
    }
}










