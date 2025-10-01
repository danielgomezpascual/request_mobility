package com.personal.metricas.organizaciones.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.App
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionCU
import com.personal.metricas.organizaciones.ui.entidades.OrganizacionUI
import com.personal.metricas.paneles.ui.entidades.PanelUI
import com.personal.metricas.paneles.ui.entidades.fromPanel
import com.personal.metricas.paneles.ui.screen.detalle.DetallePanelVM
import com.personal.metricas.transacciones.domain.entidades.ResultadoSQL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OrganizacionesDetalleVM(
	private val obtenerOrganizacionCU: ObtenerOrganizacionCU,
	private val dialog: DialogManager,
) : ViewModel() {

	private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()


	sealed class UIState {
		data class Success(
			val organizacionUI: OrganizacionUI,
		) : UIState()

		data class Error(val mensaje: String) : UIState()
		object Loading : UIState()
	}


	sealed class Eventos() {
		data class Cargar(val identificador: String) : Eventos()
		data class Guardar(val navegacion: (EventosNavegacion) -> Unit) : Eventos()
		data class Eliminar(val navegacion: (EventosNavegacion) -> Unit) : Eventos()
		data class ActivarSincronizacion(val activo: Boolean) : Eventos()
		data class OnChangeFormaSincronizar(val metodo: String) : Eventos()

	}

	fun onEvent(evento: OrganizacionesDetalleVM.Eventos) {
		App.log.d("Eventyo ${evento.toString()}")
		when (evento) {
			is Eventos.Cargar   -> cargar(evento.identificador)
			is Eventos.Guardar  -> guardar(evento.navegacion)
			is Eventos.Eliminar -> eliminar(evento.navegacion)


			else                -> {
				_uiState.update { estado ->
					estado
				}
			}
		}
	}


	fun cargar(codigo: String) {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {

				val organizacion: Organizaciones = obtenerOrganizacionCU.get(codigo) ?: Organizaciones()
				val oraganizacionUI = OrganizacionUI.fromOrganizacion(organizacion)
				_uiState.value = OrganizacionesDetalleVM.UIState.Success(organizacionUI = oraganizacionUI)

			}
		}
	}

	fun guardar(navegacion: (EventosNavegacion) -> Unit) {

	}

	fun eliminar(navegacion: (EventosNavegacion) -> Unit) {

	}

}