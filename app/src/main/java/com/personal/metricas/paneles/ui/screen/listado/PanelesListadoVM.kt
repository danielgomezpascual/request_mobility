package com.personal.metricas.paneles.ui.screen.listado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.paneles.domain.interactors.ObtenerPanelesCU
import com.personal.metricas.paneles.ui.entidades.PanelUI
import com.personal.metricas.paneles.ui.entidades.fromPanel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PanelesListadoVM(
    private val obtenerPaneles: ObtenerPanelesCU,

) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando"))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    var listaPaneles: List<PanelUI> = emptyList()


    sealed class UIState {
        data class Success(val textoBuscar: String, val lista: List<PanelUI> = emptyList<PanelUI>()) : UIState()
        data class Error(val mensaje: String) : UIState()
        data class Loading(val mensaje: String) : UIState()
    }


    sealed class Eventos {
        object Cargar : Eventos()
        data class NuevoContenidoLocal(val onClickNuevo: (PanelUI) -> Unit) : Eventos()
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
            obtenerPaneles.getAll()
                .onStart { _uiState.value = UIState.Loading("cargando") }
                .catch { _uiState.value = UIState.Error("Se ha producido un error") }
                .collect { lp ->
                    listaPaneles = lp.map { PanelUI().fromPanel(it) }.sortedBy { it.titulo }
                    _uiState.value = UIState.Success(textoBuscar = "", lista = listaPaneles)
                }
        }
    }

    private fun buscar(s: String) {
        val l = if (!s.isEmpty()) {
            listaPaneles.filter { it.titulo.contains(other = s, ignoreCase = true) }
        } else {
            listaPaneles
        }

        _uiState.update { estado ->
            if (estado is UIState.Success) {
                estado.copy(textoBuscar = s, lista = l)
            } else {
                estado
            }
        }
    }


    private fun nuevoContenidoLocal(onClickNuevo: (PanelUI) -> Unit) {
        onClickNuevo(PanelUI())
    }
}