package com.personal.requestmobility.menu.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerDashboardsHomeCU
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.fromDashboard
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import com.personal.requestmobility.paneles.ui.entidades.fromPanel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeVM(
    private val obtenerDashboardHomeCU: ObtenerDashboardsHomeCU
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
        object Carga : Eventos()
    }


    fun onEvent(evento: Eventos) {
        when (evento) {
            is Eventos.Carga -> cargaHome()

        }
    }

    private fun cargaHome() {
        viewModelScope.launch {
            val ds = obtenerDashboardHomeCU.getAllHome().firstOrNull()
            if (ds == null || ds.isEmpty() ){
                _uiState.value = UIState.Success(paneles = emptyList())
                return@launch
            }


                val d = ds.first()
                _uiState.value = UIState.Success(paneles = d.paneles.map { PanelUI().fromPanel(it) })


        }
    }
}

