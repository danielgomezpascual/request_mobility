package com.personal.requestmobility.dashboards.ui.screen.cuadricula


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerDashboardsCU
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.fromDashboard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CuadriculaDashboardVM(
    private val obtenerDashboardsCU: ObtenerDashboardsCU

) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando"))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()


    sealed class UIState {
        data class Success(val lista: List<DashboardUI> = emptyList()) : UIState()
        data class Error(val mensaje: String) : UIState()
        data class Loading(val mensaje: String) : UIState()
    }

    sealed class Eventos {
        object Cargar : Eventos()
    }

    fun onEvento(evento: Eventos) {
        when (evento) {
            Eventos.Cargar -> cargarDatos()
        }
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading("Cargando dashboards...")
            obtenerDashboardsCU.getAll()
                .catch { e -> _uiState.value = UIState.Error("Error al cargar: ${e.message}") }
                .collect { listaDashboardsDomain ->
                    _uiState.value = UIState.Success(lista = listaDashboardsDomain.map {
                        DashboardUI().fromDashboard(it)
                    })
                }
        }
    }
}
