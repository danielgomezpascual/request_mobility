package com.personal.metricas.kpi.ui.screen.listado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.kpi.domain.interactors.ObtenerKpisCU
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.kpi.ui.entidades.fromKPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KpisListadoVM(
    private val obtenerKpis: ObtenerKpisCU,

) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando"))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    var listaKpisUI: List<KpiUI> = emptyList()


    sealed class UIState {
        data class Success(val textoBuscar: String, val lista: List<KpiUI> = emptyList<KpiUI>()) : UIState()
        data class Error(val mensaje: String) : UIState()
        data class Loading(val mensaje: String) : UIState()
    }


    sealed class Eventos {
        object Cargar : Eventos()
        data class NuevoContenidoLocal(val onClickNuevo: (KpiUI) -> Unit) : Eventos()
        data class Buscar(val s: String) : Eventos()
    }


    fun onEvent(evento: Eventos) {
        when (evento) {
            Eventos.Cargar -> cargarKpis()
            is Eventos.Buscar -> buscar(evento.s)
            is Eventos.NuevoContenidoLocal -> nuevoContenidoLocal(evento.onClickNuevo)
        }

    }


    private fun cargarKpis() {
        viewModelScope.launch() {
            obtenerKpis.getAll()
                .onStart { _uiState.value = UIState.Loading("cargando") }
                .catch { _uiState.value = UIState.Error("Se ha producido un error") }
                .collect { listaKpis ->
                    listaKpisUI = listaKpis.map { KpiUI().fromKPI(it) }
                    _uiState.value = UIState.Success(textoBuscar = "", lista = listaKpisUI)
                }
        }
    }

    private fun buscar(s: String) {
        val l = if (!s.isEmpty()) {
            listaKpisUI.filter { it.titulo.contains(other = s, ignoreCase = true) }
        } else {
            listaKpisUI
        }

        _uiState.update { estado ->
            if (estado is UIState.Success) {
                estado.copy(textoBuscar = s, lista = l)
            } else {
                estado
            }
        }
    }


    private fun nuevoContenidoLocal(onClickNuevo: (KpiUI) -> Unit) {
        onClickNuevo(KpiUI())
    }
}