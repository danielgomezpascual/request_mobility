package com.personal.metricas.endpoints.ui.screen.listado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.endpoints.domain.interactors.ObtenerEndPointsCU
import com.personal.metricas.endpoints.ui.entidades.EndPointUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class EndPointsListadoVM(private val obtenerEndPoins: ObtenerEndPointsCU) : ViewModel() {

	private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando"))
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()

	var listadoEndPoitns: List<EndPointUI> = emptyList()


	sealed class UIState {
		data class Success(val textoBuscar: String, val lista: List<EndPointUI> = emptyList<EndPointUI>()) : UIState()
		data class Error(val mensaje: String) : UIState()
		data class Loading(val mensaje: String) : UIState()
	}


	sealed class Eventos {
		object Cargar : Eventos()

		data class Buscar(val s: String) : Eventos()
	}


	fun onEvent(evento: Eventos) {
		when (evento) {
			Eventos.Cargar    -> cargarEndPoints()
			is Eventos.Buscar -> buscar(evento.s)
		}

	}


	private fun cargarEndPoints() {
		viewModelScope.launch() {
			obtenerEndPoins.getAll()
				.onStart { _uiState.value = UIState.Loading("cargando") }
				.catch { _uiState.value = UIState.Error("Se ha producido un error") }
				.collect { listaEndPoints ->
					listadoEndPoitns = listaEndPoints.map { EndPointUI.fromDomain(it) }
					_uiState.value = UIState.Success(textoBuscar = "", lista = listadoEndPoitns)
				}
		}
	}

	private fun buscar(buscar: String) {

	}

}