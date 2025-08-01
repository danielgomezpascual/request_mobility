package com.personal.metricas.dashboards.ui.screen.visualizador


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.App
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils._toObjectFromJson
import com.personal.metricas.dashboards.domain.interactors.ObtenerDashboardCU
import com.personal.metricas.dashboards.ui.entidades.DashboardUI
import com.personal.metricas.dashboards.ui.entidades.fromDashboard

import com.personal.metricas.paneles.ui.entidades.PanelUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class VisualizadorDashboardVM(
	private val obtenerDashboardCU: ObtenerDashboardCU,
							 ) : ViewModel() {
	
	
	private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()
	
	
	//var kpisUI: List<KpiUI> = emptyList()
	
	
	sealed class UIState {
		data class Success(
			val dashboardUI: DashboardUI,
			val paneles: List<PanelUI> = emptyList<PanelUI>(),
						  ) : UIState()
		
		data class Error(val message: String) : UIState()
		object Loading : UIState()
	}
	
	sealed class Eventos {
		data class Carga(val idenificador: Int, val parametrosJSON: String) : Eventos()
	}
	
	
	fun onEvent(evento: Eventos) {
		when (evento) {
			is Eventos.Carga -> cargaInicial(evento.idenificador,
											 evento.parametrosJSON)
		}
	}
	
	private fun cargaInicial(idenificadorDS: Int, parametrosJSON: String) {
		App.log.i("filasDatosJSON: $parametrosJSON")
		App.log.i("obj Fila: ${_toObjectFromJson<Parametros>(parametrosJSON)}")
		
		viewModelScope.launch {
			val ds = obtenerDashboardCU.getID(idenificadorDS)
			var dsUI : DashboardUI= DashboardUI().fromDashboard(ds)

			dsUI.listaPaneles.forEach { panel ->
				App.log.d("${panel.titulo} - ${panel.seleccionado} - ${panel.orden}  ")
			}
			if (!parametrosJSON.isEmpty()) {
				dsUI = dsUI.copy(parametros = _toObjectFromJson<Parametros>(parametrosJSON) ?: Parametros())
			}

			val d: DashboardUI = dsUI.copy(nombre = Parametros.reemplazar(dsUI.nombre, parametrosKpi = dsUI.parametros, parametrosDashboard = dsUI.parametros))

			_uiState.value = UIState.Success(dashboardUI = d, paneles = d.listaPaneles)
			
		}
	}
}










