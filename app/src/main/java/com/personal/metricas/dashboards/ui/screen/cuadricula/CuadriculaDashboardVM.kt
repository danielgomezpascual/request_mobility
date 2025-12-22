package com.personal.metricas.dashboards.ui.screen.cuadricula


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.App
import com.personal.metricas.core.composables.dialogos.AppGlobalDialogs
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.dashboards.domain.entidades.Dashboard
import com.personal.metricas.dashboards.domain.entidades.TipoDashboard
import com.personal.metricas.dashboards.domain.interactors.ObtenerDashboardsCU
import com.personal.metricas.dashboards.domain.interactors.ObtenerEtiquetasDashboardCU
import com.personal.metricas.dashboards.ui.entidades.DashboardUI
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.dashboards.ui.entidades.fromDashboard
import com.personal.metricas.dashboards.ui.screen.detalle.DetalleDashboardVM
import com.personal.metricas.transacciones.domain.entidades.ResultadoSQL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CuadriculaDashboardVM(
	private val obtenerDashboardsCU: ObtenerDashboardsCU,
	private val obtenerEtiquetasDashboardCU: ObtenerEtiquetasDashboardCU

	) : ViewModel() {

	private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando"))
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()

	lateinit var listaOriginalDashboard: List<DashboardUI>
	lateinit var listaEtiquetasDisponibles: List<Etiquetas>

	sealed class UIState {
		data class Success(
			val textoBuscar: String = "",
			val lista: List<DashboardUI> = emptyList(),
			val etiquetasDisponibles : List<Etiquetas> = emptyList<Etiquetas>()
		) : UIState()
		data class Error(val mensaje: String) : UIState()
		data class Loading(val mensaje: String) : UIState()
	}

	sealed class Eventos {
		object Cargar : Eventos()
		data class Buscar(val s: String) : Eventos()
		data class FiltrarEtiquetas(val etiqueta: Etiquetas) : Eventos()
	}

	fun onEvento(evento: Eventos) {
		when (evento) {
			Eventos.Cargar    -> cargarDatos()
			is Eventos.Buscar -> buscar(evento.s)
			is Eventos.FiltrarEtiquetas -> filtrarEtiqueta(evento.etiqueta)
		}
	}

	private fun buscar(buscar: String = "") {
		_uiState.update { estado ->
			if (estado is UIState.Success) {
				val listaFiltrada = listaOriginalDashboard
					.filter { it.nombre.contains(buscar, ignoreCase = true) }
					.sortedBy { it.nombre }
				estado.copy(textoBuscar = buscar, lista = listaFiltrada)
			} else {
				estado
			}
		}
	}

	private fun filtrarEtiqueta(etiqueta: Etiquetas) {



		_uiState.update { estado ->
			if (estado is UIState.Success) {
				val busquedaActual : String = (_uiState.value as UIState.Success).textoBuscar


				 listaEtiquetasDisponibles = listaEtiquetasDisponibles.map {  etiquetaDisponible ->
					if (etiquetaDisponible.etiqueta.equals(etiqueta.etiqueta)){
						etiquetaDisponible.copy(seleccionada = !etiquetaDisponible.seleccionada)
					}else{
						etiquetaDisponible
					}
				}


				var listaEtiquetasFiltrar: List<Etiquetas>? =  listaEtiquetasDisponibles.filter { et ->et.seleccionada}
				if (listaEtiquetasFiltrar.isNullOrEmpty()) listaEtiquetasFiltrar = listaEtiquetasDisponibles

				App.log.lista("Etriquetas", listaEtiquetasFiltrar)
				App.log.lista("listaEtiquetasDisponibles", listaEtiquetasDisponibles)

				val l = listaOriginalDashboard.filter { ds ->
					(listaEtiquetasFiltrar.any { etiqueta -> ds.etiqueta.etiqueta == etiqueta.etiqueta })
					&& ds.nombre.contains(busquedaActual, ignoreCase = true)
				}.sortedBy { it.nombre }



				estado.copy(etiquetasDisponibles = listaEtiquetasDisponibles,							lista = l)
			}else{
				estado
			}

		}
	}


	private fun cargarDatos() {
		viewModelScope.launch(Dispatchers.IO) {
			_uiState.value = UIState.Loading("Cargando dashboards...")
			obtenerDashboardsCU.getAll()
				.catch { e -> _uiState.value = UIState.Error("Error al cargar: ${e.message}") }
				.collect { listaDashboardsDomain ->

					val listaDashboardDinamicos = listaDashboardsDomain
						.filter { it.tipo == TipoDashboard.Dinamico() }
						.flatMap { dsh ->
							try {
								ResultadoSQL.fromSqlToTabla(dsh.kpiOrigenDatos.sql).filas.map { f ->
									val params = f.toParametros()
									dsh.copy(
										nombre = Parametros.reemplazar(dsh.nombre, parametrosKpi = params, parametrosDashboard = params),
										parametros = params
									)
								}
							} catch (e: Exception) {
								listOf(dsh)
							}
						}

					val listaDshEstaticos = listaDashboardsDomain.filter { it.tipo == TipoDashboard.Estatico() }

					listaOriginalDashboard = (listaDashboardDinamicos + listaDshEstaticos)
						.map { DashboardUI().fromDashboard(it) }
						.sortedBy { it.nombre }

					listaEtiquetasDisponibles = obtenerEtiquetasDashboardCU.dameEtiquetas()

					_uiState.value = UIState.Success(
						etiquetasDisponibles = listaEtiquetasDisponibles,
						lista = listaOriginalDashboard
					)
				}
		}
	}
}
