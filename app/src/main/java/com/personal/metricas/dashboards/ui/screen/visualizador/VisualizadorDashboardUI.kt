package com.personal.metricas.dashboards.ui.screen.visualizador

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.labels.MA_LabelEtiqueta
import com.personal.metricas.core.composables.labels.MA_LabelExtendido
import com.personal.metricas.core.composables.labels.MA_LabelLeyenda
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.dashboards.ui.screen.visualizador.VisualizadorDashboardVM.UIState
import com.personal.metricas.menu.Features
import com.personal.metricas.paneles.domain.entidades.PanelData
import com.personal.metricas.paneles.ui.componente.MA_Panel
import com.personal.metricas.paneles.ui.entidades.PanelUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun VisualizadorDashboardUI(
	identificador: Int,
	paramtrosJSON: String,
	viewModel: VisualizadorDashboardVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	LaunchedEffect(Unit) {
		viewModel.onEvent(VisualizadorDashboardVM.Eventos.Carga(identificador, paramtrosJSON))
	}


	val uiState by viewModel.uiState.collectAsState()
	when (uiState) {
		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).message)
		UIState.Loading    -> LoadingScreen()
		is UIState.Success -> Success(viewModel, (uiState as UIState.Success), navegacion)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Success(
	viewModel: VisualizadorDashboardVM,
	uiState: UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {


	MA_ScaffoldGenerico(
		tituloScreen = TituloScreen.DashboardLista,
		navegacion = navegacion,
		accionesSuperiores = {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.Top
			) {
				MA_IconBottom(icon = Features.Nuevo().icono, color = Features.Nuevo().color) { navegacion(EventosNavegacion.NuevoDashboard) }
			}
		},
		contenido = {

			val scroll = rememberScrollState()
			Box(Modifier) {
				Column(modifier = Modifier.verticalScroll(state = scroll)) {

					MA_Titulo(uiState.dashboardUI.nombre)
					MA_LabelEtiqueta(modifier = Modifier.fillMaxWidth(), alineacion = TextAlign.Center, valor = uiState.dashboardUI.descripcion)


					uiState.paneles.filter { it.seleccionado }.forEach { panelUI ->
						lateinit var p: PanelUI

						//val notasManager = getKoin().get<NotasManager>()
						val notasManager = NotasManager.instancia()
						MA_Panel(panelData = PanelData.fromPanelUI(panelUI,notasManager,  uiState.dashboardUI.parametros))

					}

				}
			}
		}
	)


}




