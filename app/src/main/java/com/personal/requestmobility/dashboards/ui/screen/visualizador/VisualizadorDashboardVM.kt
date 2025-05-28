package com.personal.requestmobility.dashboards.ui.screen.visualizador


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerKpisDashboard
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class VisualizadorDashboardVM(
    private val obtenerKPI: ObtenerKpisDashboard,
) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()


    var kpisUI: List<KpiUI> = emptyList()


    sealed class UIState {
        data class Success(val kpis: List<KpiUI> = emptyList<KpiUI>()) : UIState()
        data class Error(val message: String) : UIState()
        object Loading : UIState()
    }

    sealed class Eventos {
        data class Carga(val idenificador: Int) : Eventos()
    }


    fun onEvent(evento: Eventos) {
        when (evento) {
            is Eventos.Carga -> cargaInicial(evento.idenificador)

        }
    }

    private fun cargaInicial(idenificadorDS: Int) {
        viewModelScope.launch {
            val listaKpis = obtenerKPI.damePanelesAsociados(idenificadorDS)
            _uiState.value = UIState.Success(kpis = listaKpis)
        }
    }
}










