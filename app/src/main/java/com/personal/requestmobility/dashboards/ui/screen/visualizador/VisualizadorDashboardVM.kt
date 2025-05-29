package com.personal.requestmobility.dashboards.ui.screen.visualizador


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerDashboardCU
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.fromDashboard

import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class VisualizadorDashboardVM(
    private val obtenerDashboardCU: ObtenerDashboardCU
) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()


    //var kpisUI: List<KpiUI> = emptyList()


    sealed class UIState {
        data class Success(val paneles: List<PanelUI> = emptyList<PanelUI>()) : UIState()
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
            val ds = obtenerDashboardCU.getID(idenificadorDS)
            val dsUI = DashboardUI().fromDashboard(ds)

            _uiState.value = UIState.Success(paneles = dsUI.listaPaneles)

            //val paneles: List<PanelUI> = ds.paneles.filter {it.}


            //_uiState.value = UIState.Success(kpis = listaKpis)
        }
    }
}










