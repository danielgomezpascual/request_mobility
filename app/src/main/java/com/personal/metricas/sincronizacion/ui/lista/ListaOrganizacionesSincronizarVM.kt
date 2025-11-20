package com.personal.metricas.sincronizacion.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.App
import com.personal.metricas.R
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.composables.dialogos.DialogosResultado
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils._t
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.organizaciones.domain.interactors.AlmacenarOrganizacionCU
import com.personal.metricas.organizaciones.domain.interactors.GenerarPlanificacionAutomaticaOrganizaciones
import com.personal.metricas.sincronizacion.domain.interactors.ObtenerOrganizacionesCU

import com.personal.metricas.sincronizacion.domain.interactors.RealizarSincronizacionCU
import com.personal.metricas.sincronizacion.domain.interactors.TIPO_SINCRONIZACION
import com.personal.metricas.sincronizacion.ui.entidades.OrganizacionesSincronizarUI
import com.personal.metricas.sincronizacion.ui.entidades.fromOrganizacion
import com.personal.metricas.sincronizacion.ui.entidades.toOrganizacion
import com.personal.metricas.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.metricas.transacciones.domain.entidades.Transacciones
import com.personal.metricas.transacciones.domain.interactors.GuardarTransacciones
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListaOrganizacionesSincronizarVM(

	private val obtenerOrganizacion: ObtenerOrganizacionesCU,
	private val realizarSincronizacionCU: RealizarSincronizacionCU,
	private val repoTrx: TransaccionesRepoImp,
	//private val guardar: GuardarTransacciones,
	private val notas: NotasManager,
	private val dialog: DialogManager,
	private val guardarOrganizacion: AlmacenarOrganizacionCU,
	private val autoPlanificacion: GenerarPlanificacionAutomaticaOrganizaciones,

	) : ViewModel() {


	private val _uiState = MutableStateFlow<UIState>(UIState.Trabajando)
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()

	var textoBuscar: String = ""

	var listaOrganizacionesSincronizarUI: List<OrganizacionesSincronizarUI> = emptyList()
	var organizacionesOriginal: List<OrganizacionesSincronizarUI> = emptyList()


	sealed class UIState {
		data class Success(
			val organizaciones: List<OrganizacionesSincronizarUI> = emptyList<OrganizacionesSincronizarUI>(),
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
		data class Buscar(val texto: String) : Eventos()
		data class OnChangeSeleccionCheck(val organizacionUI: OrganizacionesSincronizarUI) : Eventos()
		data class AplicarTodos(val valor: Boolean) : Eventos()
		object RealizarSincronizacion : Eventos()
		object EliminarDatosActuales : Eventos()


	}


	fun onEvent(evento: Eventos) {
		when (evento) {
			Eventos.Cargar                    -> cargaInicial()
			is Eventos.Buscar                 -> modificarTextoBusqueta(evento.texto)
			is Eventos.OnChangeSeleccionCheck -> onChangeSeleccion(evento.organizacionUI)
			is Eventos.AplicarTodos           -> aplicarTodos(evento.valor)
			Eventos.RealizarSincronizacion    -> realizarSincronizacion()
			Eventos.EliminarDatosActuales     -> eliminarDatosActuales()


		}
	}


	private fun aplicarTodos(seleccion: Boolean) {
		if (_uiState.value is UIState.Success) {


			val s = seleccion

			listaOrganizacionesSincronizarUI = organizacionesOriginal.map { org ->
				org.copy(seleccionado = s)

			}
			_uiState.value = UIState.Success(todos = s, organizaciones = listaOrganizacionesSincronizarUI,
											 textoBuscar = textoBuscar)
		}
	}

	private fun cargaInicial() {
		viewModelScope.launch {

			listaOrganizacionesSincronizarUI = obtenerOrganizacion.getAll().mapIndexed {indice, organzacion ->
				val orgUI = OrganizacionesSincronizarUI().fromOrganizacion(organzacion)
			//	guardarOrganizacion.guardar(orgUI)
				orgUI
			}
			organizacionesOriginal = listaOrganizacionesSincronizarUI

			val organizacionesStr: String = App.sharedPrerfences.get<String>(K.ORGANIZACIONES, "")
			val organizacionesSeleccionadasPrevias: List<String> = organizacionesStr.split(";")
			listaOrganizacionesSincronizarUI = listaOrganizacionesSincronizarUI.map { organizacion ->
				val seleccionado = organizacionesSeleccionadasPrevias.contains(organizacion.organizationId)
				organizacion.copy(seleccionado = seleccionado)
			}
			_uiState.value = UIState.Success(organizaciones = listaOrganizacionesSincronizarUI)
		}
	}

	private fun onChangeSeleccion(organizacionUI: OrganizacionesSincronizarUI) {
		if (_uiState.value is UIState.Success) {

			listaOrganizacionesSincronizarUI = listaOrganizacionesSincronizarUI.map { org ->
				if (organizacionUI.organizationCode.equals(org.organizationCode)) {
					val estado = organizacionUI.seleccionado
					organizacionUI.copy(seleccionado = !estado)
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


	private fun eliminarDatosActuales() {
		dialog.sino(_t(R.string.seguro_que_desea_elminar_los_datos_actuales_de_la_base_de_dtos)) { resp ->
			if (resp == DialogosResultado.Si) {
				viewModelScope.launch {
					repoTrx.eliminarTodo()
					notas.eliminarNotas()
					dialog.informacion("Datos eliminados") { }
				}

			}
		}

	}


	private fun realizarSincronizacion() {

		if (_uiState.value is UIState.Success) {

			val oraganizciones = (_uiState.value as UIState.Success).organizaciones

			var cadenasOrganizacionesSeleccionadas = ""
			val orgSeleccionadas = oraganizciones.filter { it.seleccionado == true }


			orgSeleccionadas.forEach { cadenasOrganizacionesSeleccionadas += it.organizationId + ";" }

			App.sharedPrerfences.put(K.ORGANIZACIONES, cadenasOrganizacionesSeleccionadas)

			if (orgSeleccionadas.isEmpty()) {
				dialog.informacion(_t(R.string.no_hay_ninguna_organizaci_n_seleccionada_seleccione_alguna_previamnete)) { }
				return
			}


			_uiState.value = (_uiState.value as UIState.Success).copy(trabajando = true)
			viewModelScope.launch {
				val orgSeleccionadas = oraganizciones.filter { it.seleccionado == true }
				var contador = 0;
				val totalOraganizacionesSincronizar = orgSeleccionadas.size
				orgSeleccionadas.forEach { organizacion ->



					async(Dispatchers.IO) {

						var s = "${organizacion.organizationCode} $contador/$totalOraganizacionesSincronizar"
						_uiState.value = (_uiState.value as UIState.Success).copy(infoSincro = s)

						realizarSincronizacionCU.sincronizarOrganizacion(organizacion.toOrganizacion(), TIPO_SINCRONIZACION.MANUAL)

						contador = contador + 1
						 s = "${organizacion.organizationCode} $contador/$totalOraganizacionesSincronizar"

						_uiState.value = (_uiState.value as UIState.Success).copy(infoSincro = s)
						withContext(Dispatchers.Main){
							if (contador == totalOraganizacionesSincronizar) {

								dialog.sino(texto = "Informacion actualizada, ¿Desea generar tambien una sincronziacion en funcion de las transacciones recibidas?"){ resultado ->
									if (resultado == DialogosResultado.Si){
										async(Dispatchers.IO) {
											autoPlanificacion.realizarPlanificacionAutomativa()

										}

									}

								}



								_uiState.value = UIState.Success(organizaciones = oraganizciones, trabajando = false)
							
							}



						}

					}




				}

			}
		}

	}


}
