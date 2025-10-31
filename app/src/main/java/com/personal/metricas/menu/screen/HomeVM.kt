package com.personal.metricas.menu.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.App
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.dashboards.domain.entidades.Dashboard
import com.personal.metricas.dashboards.domain.interactors.ObtenerDashboardsHomeCU
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.inicializador.domain.InicializadorManager
import com.personal.metricas.paneles.domain.entidades.PanelData
import com.personal.metricas.paneles.domain.entidades.TiposPanel
import com.personal.metricas.paneles.ui.componente.MA_Panel
import com.personal.metricas.paneles.ui.entidades.PanelUI
import com.personal.metricas.paneles.ui.entidades.fromPanel
import com.personal.metricas.transacciones.domain.entidades.ResultadoSQL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.collections.plus

class HomeVM(
	private val inicalizadorManager: InicializadorManager,
	private val obtenerDashboardHomeCU: ObtenerDashboardsHomeCU,
	private val dialog: DialogManager,
) : ViewModel() {


	private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()


	//var kpisUI: List<KpiUI> = emptyList()


	sealed class UIState {
		data class Success(val dsHome : Dashboard?, val paneles: List<PanelUI> = emptyList<PanelUI>()) : UIState()
		data class Error(val message: String) : UIState()
		object Loading : UIState()
	}

	sealed class Eventos {
		object Carga : Eventos()
		object InicializadorMetricas : Eventos()
	}


	fun onEvent(evento: Eventos) {
		when (evento) {
			is Eventos.Carga                 -> cargaHome()
			is Eventos.InicializadorMetricas -> inicilizarMetricas()

		}
	}

	private fun cargaHome() {
		viewModelScope.launch {
			val ds = obtenerDashboardHomeCU.getAllHome().firstOrNull()
			if (ds == null || ds.isEmpty()) {
				_uiState.value = UIState.Success(dsHome = null, paneles = emptyList())
				return@launch
			}


			val d: Dashboard = ds.first()
			val paneles: List<PanelUI> = d.paneles.filter { it.seleccionado == true }.map { PanelUI().fromPanel(it) }


			var pTmp : List<PanelUI> = emptyList()



			_uiState.value = UIState.Success(dsHome = d, paneles = paneles)


		}
	}

	private fun inicilizarMetricas() {
		viewModelScope.launch {

			inicalizadorManager.start()

			dialog.informacion(texto = "Incializacion de datos finalizada") {}
		}

	}
}

