package com.personal.requestmobility.kpi.ui.screen.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.kpi.domain.interactors.EliminarKpiCU
import com.personal.requestmobility.kpi.domain.interactors.GuardarKpiCU
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpiCU
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.fromKPI
import com.personal.requestmobility.core.composables.dialogos.DialogosResultado
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.utils.Parametro
import com.personal.requestmobility.core.utils.Parametros
import com.personal.requestmobility.core.utils._t
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion
import com.personal.requestmobility.paneles.domain.interactors.GuardarPanelCU
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
abstract class UIStateBase(val mostrarDialogoSiNO: Boolean = false,
                           val mostrarDialogoConfirmacion: Boolean = false,
                           val textoDialogos: String = "")*/

class DetalleKpiVM(
	private val obtenerKpi: ObtenerKpiCU,
	private val guardarKpi: GuardarKpiCU,
	private val eliminarKpi: EliminarKpiCU,
	private val guardarPanel: GuardarPanelCU,
	private val dialog: DialogManager,

	) : ViewModel() {

	private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()

	/*private val _dialogoActual = MutableStateFlow<Dialogos>(Dialogos.Vacio)
	val dialogoActual: StateFlow<Dialogos> = _dialogoActual.asStateFlow()*/


	sealed class UIState {

		data class Success(val kpiUI: KpiUI, val paramtrosSeleccionado: Parametro = Parametro()) : UIState()
		data class Error(val mensaje: String) : UIState()
		object Loading : UIState()
	}

	sealed class Eventos() {
		data class Cargar(val identificador: Int) : Eventos()
		data class OnChangeTitulo(val titulo: String) : Eventos()
		data class OnChangeDescripcion(val descripcion: String) : Eventos()
		data class OnChangeSQL(val sql: String) : Eventos()


		data class ModificarClaveParametrosSeleccionado(val valor: String) : Eventos()
		data class ModificarValorParametrosSeleccionado(val valor: String) : Eventos()
		data class ModificarValorPorDefectoParametrosSeleccionado(val valor: String) : Eventos()
		data class ModificarValorFijoeParametrosSeleccionado(val valor: Boolean) : Eventos()
		data class SeleccionarParametro(val valor: Parametro) : Eventos()
		data class EliminarParametro(val valor: Parametro) : Eventos()
		data object NuevoParametro : Eventos()
		data object GuardarParametrosSelecciconado : Eventos()


		/* data object EliminarDatosActuales : Eventos()
		 data class CerrarDialogo(val key: String = "", val resultado: ResultadoDialog = ResultadoDialog.Confirmar) : Eventos()
 */

		data class Guardar(val navegacion: (EventosNavegacion) -> Unit) : Eventos()
		data class Eliminar(val navegacion: (EventosNavegacion) -> Unit) : Eventos()
		data class CrearPanel(val navegacion: (EventosNavegacion) -> Unit) : Eventos()
		data class DuplicarKpi(val navegacion: (EventosNavegacion) -> Unit) : Eventos()

	}


	init {
		App.log.d("[INIT] Dentro del view model de los kpis")
	}


	fun onEvent(evento: Eventos) {
		App.log.d("Eventyo ${evento.toString()}")
		when (evento) {
			is Eventos.Cargar      -> cargar(evento.identificador)
			is Eventos.Guardar     -> guardar(evento.navegacion)
			is Eventos.Eliminar    -> eliminar(evento.navegacion)
			is Eventos.CrearPanel  -> crearPanel(evento.navegacion)
			is Eventos.DuplicarKpi -> duplicarKpi(evento.navegacion)

			else                   -> {
				_uiState.update { estado ->
					if (estado is UIState.Success) {


						when (evento) {


							is Eventos.OnChangeTitulo                                 -> estado.copy(kpiUI = estado.kpiUI.copy(titulo = evento.titulo))
							is Eventos.OnChangeDescripcion                            -> estado.copy(kpiUI = estado.kpiUI.copy(descripcion = evento.descripcion))
							is Eventos.OnChangeSQL                                    -> {

								val k = KpiUI(
									id = estado.kpiUI.id,
									titulo = estado.kpiUI.titulo,
									descripcion = estado.kpiUI.descripcion,
									sql = evento.sql
								)

								estado.copy(
									kpiUI = k
								)


							}


							is Eventos.ModificarClaveParametrosSeleccionado           -> {
								App.log.d("Valor recibido : ${evento.valor.toString()}")
								estado.copy(paramtrosSeleccionado = estado.paramtrosSeleccionado.copy(key = evento.valor))
							}

							is Eventos.ModificarValorParametrosSeleccionado           -> {
								estado.copy(paramtrosSeleccionado = estado.paramtrosSeleccionado.copy(valor = evento.valor))
							}

							is Eventos.ModificarValorPorDefectoParametrosSeleccionado -> {
								estado.copy(paramtrosSeleccionado = estado.paramtrosSeleccionado.copy(defecto = evento.valor))
							}

							is Eventos.ModificarValorFijoeParametrosSeleccionado      -> {
								estado.copy(paramtrosSeleccionado = estado.paramtrosSeleccionado.copy(fijo = evento.valor))
							}
							is Eventos.SeleccionarParametro->{estado.copy(paramtrosSeleccionado = evento.valor)}
							is Eventos.EliminarParametro->{

								val paramtrosSeleccionado: Parametro = evento.valor

								var parametrosKpi: List<Parametro> = emptyList()

								estado.kpiUI.parametros.ps.forEach { parametro ->
									if (!parametro.key.equals(paramtrosSeleccionado.key)) {
										parametrosKpi = parametrosKpi.plus(parametro)
									}
								}
								estado.copy(kpiUI = estado.kpiUI.copy(parametros = Parametros(ps = parametrosKpi)), paramtrosSeleccionado = Parametro())
							}

							is Eventos.NuevoParametro->{
								estado.copy(paramtrosSeleccionado = Parametro())
							}

							is Eventos.GuardarParametrosSelecciconado                 -> {
								val paramtrosSeleccionado: Parametro = estado.paramtrosSeleccionado
								var encontrado: Boolean = false
								var parametrosKpi: List<Parametro> = emptyList()

								estado.kpiUI.parametros.ps.forEach { parametro ->
									if (parametro.key.equals(paramtrosSeleccionado.key)) {
										parametrosKpi = parametrosKpi.plus(paramtrosSeleccionado)
										encontrado = true
									}else{
										parametrosKpi = parametrosKpi.plus(parametro)
									}
								}
								if (!encontrado) {
									parametrosKpi = parametrosKpi.plus(paramtrosSeleccionado)
								}




								estado.copy(kpiUI = estado.kpiUI.copy(parametros = Parametros(ps = parametrosKpi)), paramtrosSeleccionado = Parametro())
							}

							//DetalleKpiVM.Eventos.EliminarDatosActuales -> eliminar()
							else                                                      -> estado
						}
					} else {
						estado
					}
				}
			}
		}
	}


	private fun crearPanel(navegacion: (EventosNavegacion) -> Unit) {


		if (_uiState.value is UIState.Success) {
			dialog.sino(_t(R.string.quieres_crear_un_panel_directamente_desde_esta_kpi)) { resp ->

				if (resp == DialogosResultado.Si) {
					viewModelScope.launch {
						val kpiUI: KpiUI = (_uiState.value as UIState.Success).kpiUI
						//val panelUI: PanelUI = PanelUI(id = 0, titulo = kpiUI.titulo, descripcion = kpiUI.descripcion, configuracion = PanelConfiguracion(), kpiUI)

						guardarPanel.guardar(PanelUI.crearPanelUI(kpiUI))

						dialog.informacion(_t(R.string.panel_creado)) {
							navegacion(EventosNavegacion.MenuApp)
						}
					}
				}
			}


		}

	}

	private fun cargar(identificador: Int) {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				if (identificador == 0) {
					_uiState.value = UIState.Success(kpiUI = KpiUI())
				} else {
					val kpiUI: KpiUI = KpiUI().fromKPI(obtenerKpi.obtener(id = identificador))
					_uiState.value = UIState.Success(kpiUI = kpiUI)
				}
			}
		}
	}


	private fun guardar(navegacion: (EventosNavegacion) -> Unit) {

		if (validarDatos()) {
			viewModelScope.launch {
				val id: Long = async(Dispatchers.IO) {
					val kpiUI = (_uiState.value as UIState.Success).kpiUI
					guardarKpi.guardar(kpiUI)
				}.await()

				dialog.informacion(_t(R.string.datos_guardados_correctamente)) {
					navegacion(EventosNavegacion.MenuKpis)

				}
			}
		}

	}

	private fun validarDatos(): Boolean {
		if (_uiState.value is UIState.Success) {
			val kpiUI: KpiUI = (_uiState.value as UIState.Success).kpiUI

			//validacion del titulo
			if (kpiUI.titulo.isEmpty()) {
				dialog.informacion(_t(R.string.debe_proporcionar_un_nombre_para_mejorar_su_localizacion)) { }
				return false
			}

			//validacind e sql
			if (kpiUI.sql.isEmpty()) {
				dialog.informacion(_t(R.string.sentencia_de_consulta_vac_a)) { }
				return false
			}


		}

		return true


	}

	private fun duplicarKpi(navegacion: (EventosNavegacion) -> Unit) {
		dialog.sino("¿Seguro que desea duplicar el KPI?") { resp ->
			if (resp == DialogosResultado.Si) {
				viewModelScope.launch {
					async(Dispatchers.IO) {
						val kpiUI = (_uiState.value as UIState.Success).kpiUI
						val nuevoKpi = kpiUI.copy(id = 0, titulo = "${kpiUI.titulo} - Copia")
						guardarKpi.guardar(nuevoKpi)
					}.await()
					dialog.informacion(_t(R.string.kpi_duplicado)) { navegacion(EventosNavegacion.MenuKpis) }
				}
			}
		}
	}

	private fun eliminar(navegacion: (EventosNavegacion) -> Unit) {

		dialog.sino("¿Seguro que desea eliminar el KPI?") { resp ->
			if (resp == DialogosResultado.Si) {
				viewModelScope.launch {
					async(Dispatchers.IO) {
						val kpiUI = (_uiState.value as UIState.Success).kpiUI
						eliminarKpi.eliminar(kpiUI)
					}.await()
					dialog.informacion(_t(R.string.datos_eliminados_correctamnte)) { navegacion(EventosNavegacion.MenuKpis) }
				}
			}
		}

	}
}