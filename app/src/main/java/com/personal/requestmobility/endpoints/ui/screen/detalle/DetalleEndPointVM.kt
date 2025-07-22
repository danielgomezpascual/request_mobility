package com.personal.requestmobility.endpoints.ui.screen.detalle


import MA_Morph
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.core.composables.dialogos.DialogosResultado
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.utils.Parametro
import com.personal.requestmobility.core.utils.Parametros
import com.personal.requestmobility.core.utils._t
import com.personal.requestmobility.endpoints.domain.entidades.ResultadoEndPoint
import com.personal.requestmobility.endpoints.domain.interactors.AlmacenarDatosRemotosEndPointCU
import com.personal.requestmobility.endpoints.domain.interactors.EliminarEndPointCU
import com.personal.requestmobility.endpoints.domain.interactors.GuardarEndPointCU
import com.personal.requestmobility.endpoints.domain.interactors.ObtenerEndPointCU
import com.personal.requestmobility.endpoints.ui.entidades.EndPointUI
import com.personal.requestmobility.endpoints.ui.entidades.toDomain
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.screen.detalle.DetalleKpiVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalleEndPointVM(
	private val obtenerEndPointUI: ObtenerEndPointCU,
	private val guardarEndPoint: GuardarEndPointCU,
	private val eliminarEndPoint: EliminarEndPointCU,
	private val  procesarEndPoint: AlmacenarDatosRemotosEndPointCU,
	private val dialog: DialogManager

) : ViewModel() {

	private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando..."))
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()

	sealed class UIState {
		data class Success(
			val endPointUI: EndPointUI,
			val paramtrosSeleccionado: Parametro = Parametro(),
			val trabajando : Boolean = false
		) : UIState()

		data class Error(val mensaje: String) : UIState()
		data class Loading(val mensaje: String) : UIState()
	}

	sealed class Eventos {

		data class Cargar(val identificador: Int) : Eventos()
		data class Eliminar(
			val navegacion: (EventosNavegacion) -> Unit,
		) : Eventos()  // Renombrado para claridad

		data class Guardar(val navegacion: (EventosNavegacion) -> Unit) : Eventos()  // Renombrado para claridad
		data class Procesar(val navegacion: (EventosNavegacion) -> Unit) : Eventos()  // Renombrado para claridad

		data class OnChangeNombre(val valor: String) : Eventos()  // Renombrado para claridad
		data class OnChangeDescripcion(val valor: String) : Eventos()  // Renombrado para claridad
		data class OnChangeURL(val valor: String) : Eventos()  // Renombrado para claridad
		data class OnChangeTabla(val valor: String) : Eventos()  // Renombrado para claridad
		data class OnChangeNodoIdentificadorFila(val valor: String) : Eventos()  // Renombrado para claridad

		data class ModificarClaveParametrosSeleccionado(val valor: String) : Eventos()
		data class ModificarValorParametrosSeleccionado(val valor: String) : Eventos()


		data class SeleccionarParametro(val valor: Parametro) : Eventos()
		data class EliminarParametro(val valor: Parametro) : Eventos()
		data object NuevoParametro : Eventos()
		data object CargarParametrosDefectoMobility : Eventos()
		data object GuardarParametrosSelecciconado : Eventos()


	}


	fun onEvento(evento: Eventos) {
		when (evento) {
			is Eventos.Cargar   -> cargar(evento.identificador)
			is Eventos.Eliminar -> eliminar(evento.navegacion)
			is Eventos.Guardar  -> guardar(evento.navegacion)
			is Eventos.Procesar -> procesar(evento.navegacion)
			else                -> { // Manejo de eventos OnChange
				_uiState.update { estado ->
					if (estado is UIState.Success) {
						when (evento) {
							is Eventos.OnChangeNombre                       -> estado.copy(endPointUI = estado.endPointUI.copy(nombre = evento.valor))
							is Eventos.OnChangeDescripcion                  -> estado.copy(endPointUI = estado.endPointUI.copy(descripcion = evento.valor))
							is Eventos.OnChangeURL                          -> estado.copy(endPointUI = estado.endPointUI.copy(url = evento.valor))
							is Eventos.OnChangeTabla                        -> estado.copy(endPointUI = estado.endPointUI.copy(tabla = evento.valor))
							is Eventos.OnChangeNodoIdentificadorFila        -> estado.copy(endPointUI = estado.endPointUI.copy(nodoIdentificadorFila = evento.valor))

							is Eventos.ModificarClaveParametrosSeleccionado -> estado.copy(paramtrosSeleccionado = estado.paramtrosSeleccionado.copy(key = evento.valor))
							is Eventos.ModificarValorParametrosSeleccionado -> estado.copy(paramtrosSeleccionado = estado.paramtrosSeleccionado.copy(valor = evento.valor))


							is Eventos.SeleccionarParametro                 -> {
								estado.copy(paramtrosSeleccionado = evento.valor)
							}

							is Eventos.EliminarParametro                    -> {
								val paramtrosSeleccionado: Parametro = evento.valor
								var parametrosKpi: List<Parametro> = emptyList()

								estado.endPointUI.parametros.ps.forEach { parametro ->
									if (!parametro.key.equals(paramtrosSeleccionado.key)) {
										parametrosKpi = parametrosKpi.plus(parametro)
									}
								}
								estado.copy(endPointUI = estado.endPointUI.copy(parametros = Parametros(ps = parametrosKpi)), paramtrosSeleccionado = Parametro())
							}

							is Eventos.NuevoParametro                       -> {
								estado.copy(paramtrosSeleccionado = Parametro())
							}

							is Eventos.CargarParametrosDefectoMobility      -> {
								lateinit var ps: Parametros
								var e = estado.copy(endPointUI = estado.endPointUI)
								Parametros.dameParametrosPorDefectoMobility().forEach { parametro ->
									ps = guardarParametro(e.endPointUI, parametro)
									e = e.copy(endPointUI = e.endPointUI.copy(parametros = ps))
								}
								e
							}

							is Eventos.GuardarParametrosSelecciconado       -> {
								val paramtrosSeleccionado: Parametro = estado.paramtrosSeleccionado
								val ps = guardarParametro(estado.endPointUI, paramtrosSeleccionado)
								estado.copy(endPointUI = estado.endPointUI.copy(parametros = ps), paramtrosSeleccionado = Parametro())
							}

							else                                            -> estado // No debería llegar aquí si los eventos están bien definidos


						}
					} else {
						estado // No modificar si no es Success
					}
				}
			}
		}
	}

	private fun procesar(navegacion: (EventosNavegacion) -> Unit){

		if (_uiState.value is DetalleEndPointVM.UIState.Success) {

			_uiState.value = (_uiState.value as UIState.Success).copy(trabajando = true)
			val endPointUI: EndPointUI = (_uiState.value as DetalleEndPointVM.UIState.Success).endPointUI
			viewModelScope.launch {
				async {
					guardarEndPoint.guardar(endPointUI.toDomain())

				}.await()
				val resultado: ResultadoEndPoint = procesarEndPoint.obtenerRemoto(endPointUI.id)
				_uiState.value = (_uiState.value as UIState.Success).copy(trabajando = false)
				dialog.informacion("Procesamiento  ${endPointUI.nombre}  \n" +
								   "${resultado.descripcion } -  Errores: ${resultado.errores}" ){}

			}
		}

	}
	private fun guardarParametro(endPointUI: EndPointUI, parametro: Parametro): Parametros {

		var encontrado: Boolean = false
		var parametrosKpi: List<Parametro> = emptyList()

		endPointUI.parametros.ps.forEach { parametroEndPoint ->
			if (parametroEndPoint.key.equals(parametro.key)) {
				parametrosKpi = parametrosKpi.plus(parametro)
				encontrado = true
			} else {
				parametrosKpi = parametrosKpi.plus(parametroEndPoint)
			}
		}
		if (!encontrado) {
			parametrosKpi = parametrosKpi.plus(parametro)
		}


		return Parametros(ps = parametrosKpi)


	}

	private fun cargar(identificador: Int) {
		viewModelScope.launch {


			try {

				val endPointUI = EndPointUI.fromDomain(obtenerEndPointUI.obtener(identificador))
				_uiState.value = UIState.Success(endPointUI = endPointUI)

			}
			catch (e: Exception) {
				_uiState.value = UIState.Error(mensaje = e.message.toString())
			}
		}

	}

	private fun eliminar(navegacion: (EventosNavegacion) -> Unit) {

		dialog.sino(_t(R.string.seguro_que_desea_elimianr_el_elmento_seleccionado)) { resp ->

			if (resp == DialogosResultado.Si) {
				viewModelScope.launch {
					try { // El ejemplo no tiene try-catch aquí, pero es buena práctica
						withContext(Dispatchers.IO) {
							val endPointUI = (_uiState.value as DetalleEndPointVM.UIState.Success).endPointUI
							eliminarEndPoint.eliminar(endPoint = endPointUI.toDomain())
							dialog.informacion(_t(R.string.elemento_eliminado)) {
								navegacion(EventosNavegacion.MenuDashboard)
							}
						}
					}
					catch (e: Exception) {
						_uiState.value = DetalleEndPointVM.UIState.Error("Error al eliminar: ${e.message}")
					}
				}
			}

		}

	}

	private fun guardar(navegacion: (EventosNavegacion) -> Unit) {
		if (validar()) {
			viewModelScope.launch {
				try { // El ejemplo no tiene try-catch aquí, pero es buena práctica
					withContext(Dispatchers.IO) {
						val endPointUI = (_uiState.value as UIState.Success).endPointUI
						guardarEndPoint.guardar(endPointUI.toDomain())
						dialog.informacion(_t(R.string.elemento_almacenado)) {
							navegacion(EventosNavegacion.MenuDashboard)

						}
					}
				}
				catch (e: Exception) {
					_uiState.value = UIState.Error("Error al guardar: ${e.message}")
				}
			}
		}

	}

	private fun validar(): Boolean {

		return true

	}

}