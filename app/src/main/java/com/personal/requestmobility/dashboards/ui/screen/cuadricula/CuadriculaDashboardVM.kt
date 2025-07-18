package com.personal.requestmobility.dashboards.ui.screen.cuadricula


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
import com.personal.requestmobility.core.utils.Parametros
import com.personal.requestmobility.core.utils.reemplazaValorFila
import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.entidades.TipoDashboard
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerDashboardsCU
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.fromDashboard
import com.personal.requestmobility.transacciones.domain.entidades.ResultadoSQL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CuadriculaDashboardVM(
	private val obtenerDashboardsCU: ObtenerDashboardsCU,

	) : ViewModel() {

	private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando"))
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()

	lateinit var listaOriginalDashboard: List<DashboardUI>

	sealed class UIState {
		data class Success(val textoBuscar: String = "", val lista: List<DashboardUI> = emptyList()) : UIState()
		data class Error(val mensaje: String) : UIState()
		data class Loading(val mensaje: String) : UIState()
	}

	sealed class Eventos {
		object Cargar : Eventos()
		data class Buscar(val s: String) : Eventos()
	}

	fun onEvento(evento: Eventos) {
		when (evento) {
			Eventos.Cargar    -> cargarDatos()
			is Eventos.Buscar -> buscar(evento.s)
		}
	}

	private fun buscar(buscar: String = "") {
		_uiState.value = UIState.Success(textoBuscar = buscar, lista = listaOriginalDashboard.filter { it.nombre.contains(buscar, ignoreCase = true) })


	}

	private fun cargarDatos() {
		viewModelScope.launch {
			_uiState.value = UIState.Loading("Cargando dashboards...")
			obtenerDashboardsCU.getAll()
				.catch { e -> _uiState.value = UIState.Error("Error al cargar: ${e.message}") }
				.collect { listaDashboardsDomain ->


					var listaDashboard: List<Dashboard> = emptyList()
					listaDashboardsDomain.filter { it.tipo == TipoDashboard.Dinamico() }.forEach { dsh ->

						ResultadoSQL.fromSqlToTabla(dsh.kpiOrigenDatos.sql).filas.forEach { f ->
							//val ds: Dashboard = dsh.copy(nombre = dsh.nombre.reemplazaValorFila(f.toParametros(), addComillas = false))

							App.log.i("!->  ${f.toParametros()}")
							val ds: Dashboard = dsh.copy(nombre = Parametros.reemplazar(dsh.nombre, parametrosKpi = f.toParametros(), parametrosDashboard = f.toParametros()))


							listaDashboard = listaDashboard.plus(ds.copy(parametros = f.toParametros()))
						}
					}


					val listaDshEstaticos =
						listaDashboardsDomain.filter { it.tipo == TipoDashboard.Estatico() }

					listaDashboard = listaDashboard.plus(listaDshEstaticos)
					listaOriginalDashboard = listaDashboard.map {
						DashboardUI().fromDashboard(it)
					}

					_uiState.value = UIState.Success(lista = listaOriginalDashboard)

				}
		}
	}
}


