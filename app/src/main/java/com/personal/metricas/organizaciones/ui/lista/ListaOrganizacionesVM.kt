package com.personal.metricas.organizaciones.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.App
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.utils.K
import com.personal.metricas.organizaciones.domain.interactors.AlmacenarOrganizacionCU
import com.personal.metricas.organizaciones.domain.interactors.GenerarPlanificacionAutomaticaOrganizaciones
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesLocalCU
import com.personal.metricas.organizaciones.ui.entidades.OrganizacionUI
import com.personal.metricas.sincronizacion.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.sincronizacion.ui.entidades.OrganizacionesSincronizarUI
import com.personal.metricas.sincronizacion.ui.entidades.fromOrganizacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListaOrganizacionesVM(

	private val obtenerOrganizacion: ObtenerOrganizacionesLocalCU,
	private val guardar: AlmacenarOrganizacionCU,
	private val obtenerOrganizacionesRemoto: ObtenerOrganizacionesCU,
	private val autoPlanificacion: GenerarPlanificacionAutomaticaOrganizaciones,
	private val dialog: DialogManager,
) : ViewModel() {


	private val _uiState = MutableStateFlow<UIState>(UIState.Trabajando)
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()

	var textoBuscar: String = ""

	var listaOrganizacionesSincronizarUI: List<OrganizacionUI> = emptyList()
	var organizacionesOriginal: List<OrganizacionUI> = emptyList()


	sealed class UIState {
		data class Success(
			val organizaciones: List<OrganizacionUI> = emptyList<OrganizacionUI>(),
			val textoBuscar: String = "",
			val trabajando: Boolean = false,
			val mostrarDialogoInformacion: Boolean = false,
			val mostrarDialogoSiNo: Boolean = false,
			val texto: String = "",
			val todos: Boolean = false,
			val infoSincro: String = "",

			) : UIState()

		data class Error(val message: String) : UIState()
		object Trabajando : UIState()
	}

	sealed class Eventos {
		object Cargar : Eventos()
		object AutoPlanificacion : Eventos()
		data class Buscar(val texto: String) : Eventos()
		data class OnChangeSeleccionCheck(val organizacionUI: OrganizacionUI) : Eventos()
		data class AplicarTodos(val valor: Boolean) : Eventos()


	}


	fun onEvent(evento: Eventos) {
		when (evento) {
			Eventos.Cargar                    -> cargaInicial()
			Eventos.AutoPlanificacion         -> autoPlanificacion()
			is Eventos.Buscar                 -> modificarTextoBusqueta(evento.texto)
			is Eventos.OnChangeSeleccionCheck -> onChangeSeleccion(evento.organizacionUI)
			is Eventos.AplicarTodos           -> aplicarTodos(evento.valor)


		}
	}

	private fun autoPlanificacion() {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				autoPlanificacion.realizarPlanificacionAutomativa()
			}
			dialog.informacion("proceso finalizado") { }

		}

	}

	private fun aplicarTodos(seleccion: Boolean) {
		if (_uiState.value is UIState.Success) {


			val s = seleccion

			listaOrganizacionesSincronizarUI = organizacionesOriginal.map { org ->
				org.copy(activo = s)

			}
			_uiState.value = UIState.Success(todos = s, organizaciones = listaOrganizacionesSincronizarUI,
											 textoBuscar = textoBuscar)
		}
	}

	private fun cargaInicial() {
		viewModelScope.launch {
			async (Dispatchers.IO) {
				val organizacionesLocal = obtenerOrganizacion.getAll()
				obtenerOrganizacionesRemoto.getAll().mapIndexed { indice, organzacion ->
					if (organizacionesLocal.firstOrNull { it.organizationCode.equals(organzacion.organizationCode) } == null) {
						guardar.guardar(OrganizacionesSincronizarUI().fromOrganizacion(organzacion))
					}
				}
			}.await()

			listaOrganizacionesSincronizarUI = obtenerOrganizacion.getAll().mapIndexed { indice, organzacion ->
				//val orgUI = OrganizacionesSincronizarUI().fromOrganizacion(organzacion)
				val org = OrganizacionUI.fromOrganizacion(organzacion)
				//	guardarOrganizacion.guardar(orgUI)
				org
			}.sortedBy { it.organizationCode }



			organizacionesOriginal = listaOrganizacionesSincronizarUI

			//val organizacionesStr: String = App.sharedPrerfences.get<String>(K.ORGANIZACIONES, "")
			//val organizacionesSeleccionadasPrevias: List<String> = organizacionesStr.split(";")
			/*	listaOrganizacionesSincronizarUI = listaOrganizacionesSincronizarUI.map { organizacion ->
					val seleccionado = organizacionesSeleccionadasPrevias.contains(organizacion.organizationId)
					organizacion.copy(activo = seleccionado)
				}*/

			//	App.log.lista("ORganizacion", listaOrganizacionesSincronizarUI.filter { it.activo == true })
			_uiState.value = UIState.Success(organizaciones = listaOrganizacionesSincronizarUI)
		}
	}

	private fun onChangeSeleccion(organizacionUI: OrganizacionUI) {
		if (_uiState.value is UIState.Success) {

			listaOrganizacionesSincronizarUI = listaOrganizacionesSincronizarUI.map { org ->
				if (organizacionUI.organizationCode.equals(org.organizationCode)) {
					val seleccion = organizacionUI.activo
					organizacionUI.copy(activo = !seleccion)
				} else {
					org
				}
			}
			_uiState.value = UIState.Success(organizaciones = listaOrganizacionesSincronizarUI, textoBuscar = textoBuscar)
		}
	}


	private fun modificarTextoBusqueta(texto: String) {

		if (_uiState.value is UIState.Success) {
			textoBuscar = texto

			_uiState.update { estado ->
				if (estado is UIState.Success) {
					if (texto.isEmpty()) {
						estado.copy(textoBuscar = "", organizaciones = listaOrganizacionesSincronizarUI.map { it.copy(visible = true) })
					} else {


						estado.copy(textoBuscar = textoBuscar,
									organizaciones = listaOrganizacionesSincronizarUI.map {
										it.copy(visible = (it.organizationCode.contains(textoBuscar, ignoreCase = true) || (it.organizationId.contains(textoBuscar, ignoreCase = true) || it.organizationName.contains(textoBuscar, ignoreCase = true))))
									})
					}

				} else {
					estado
				}

			}


		}
	}


}
