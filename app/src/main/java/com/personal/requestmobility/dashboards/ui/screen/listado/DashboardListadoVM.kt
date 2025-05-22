package com.personal.requestmobility.dashboards.ui.screen.listado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerDashboardCU
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.fromDashboard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardListadoVM(
    private val obtenerDashboardsCU: ObtenerDashboardCU

    ) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando"))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    var dashboardsUI: List<DashboardUI> = emptyList() // Equivalente a codigosUI

    sealed class UIState {
        data class Success(val textoBuscar: String, val lista: List<DashboardUI> = emptyList()) : UIState()
        data class Error(val mensaje: String) : UIState()
        data class Loading(val mensaje: String) : UIState()
    }

    sealed class Eventos {
        object Cargar : Eventos()
        object Nuevo : Eventos() // Aunque nuevoContendoLocal() esté vacío, el evento existe
        data class Buscar(val s: String) : Eventos()
    }

    fun onEvento(evento: Eventos) {
        when (evento) {
            is Eventos.Buscar -> buscar(evento.s)
            Eventos.Cargar -> cargarDatos()
            Eventos.Nuevo -> nuevoContendoLocal()
        }
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading("Cargando dashboards...")
            obtenerDashboardsCU.getAll()
                .catch { e ->
                    // Manejar error de la recolección del Flow si es necesario aquí,
                    // o asegurarse que el CU maneje y transforme excepciones a un tipo esperado.
                    _uiState.value = UIState.Error("Error al cargar: ${e.message}")
                }
                .collect { listaDashboardsDomain ->
                    dashboardsUI = listaDashboardsDomain.map { DashboardUI().fromDashboard(it) }
                    // Re-aplicar el filtro de búsqueda si existe
                    val currentSearchText = (_uiState.value as? UIState.Success)?.textoBuscar ?: ""
                    val filteredList = if (currentSearchText.isNotEmpty()) {
                        dashboardsUI.filter {
                            it.nombre.contains(currentSearchText, ignoreCase = true) ||
                                    it.descripcion.contains(currentSearchText, ignoreCase = true)
                        }
                    } else {
                        dashboardsUI
                    }
                    _uiState.value = UIState.Success(textoBuscar = currentSearchText, lista = filteredList)
                }
        }
    }

    private fun buscar(s: String) {
        val l = if (s.isNotEmpty()) {
            dashboardsUI.filter {
                it.nombre.contains(s, ignoreCase = true) ||
                        it.descripcion.contains(s, ignoreCase = true)
            }
        } else {
            dashboardsUI
        }
        _uiState.update { estado ->
            when (estado) {
                is UIState.Success -> estado.copy(textoBuscar = s, lista = l)
                // Si está cargando o en error, y se intenta buscar, se podría mantener el estado
                // o transicionar a Success con la búsqueda. El ejemplo original actualiza a Success.
                else -> UIState.Success(textoBuscar = s, lista = l)
            }
        }
    }

    private fun nuevoContendoLocal() {
        // La implementación en el ejemplo está vacía.
        // Generalmente, la navegación para "Nuevo" se maneja desde la UI.
    }
}