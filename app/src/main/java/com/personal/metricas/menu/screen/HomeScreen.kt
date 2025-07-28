package com.personal.metricas.menu.screen

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.listas.MA_NoData
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.menu.Features
import com.personal.metricas.menu.screen.HomeVM.UIState
import com.personal.metricas.paneles.domain.entidades.PanelData
import com.personal.metricas.paneles.ui.componente.MA_Panel
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(
	viewModel: HomeVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	LaunchedEffect(Unit) {
		viewModel.onEvent(HomeVM.Eventos.Carga)
	}
	val uiState by viewModel.uiState.collectAsState()

	when (uiState) {
		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).message)
		UIState.Loading    -> LoadingScreen()
		is UIState.Success -> {
			SuccessMenu(viewModel, (uiState as UIState.Success), navegacion)
		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessMenu(
	viewModel: HomeVM,
	uiState: UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {


	MA_ScaffoldGenerico(

		tituloScreen = TituloScreen.Home,
		navegacion = navegacion,
		accionesSuperiores = {},
		contenido = {
			val scroll = rememberScrollState()
			Column(modifier = Modifier
				.fillMaxSize()
				.verticalScroll(state = scroll),
				   verticalArrangement = Arrangement.Center,
				   horizontalAlignment = Alignment.CenterHorizontally) {
				if (uiState.paneles.size > 0) {
					val notasManager = NotasManager.instancia()
					uiState.paneles.forEach { panelUI ->
						MA_Card {
							MA_Panel(panelData = PanelData.fromPanelUI(panelUI, notasManager, Parametros()))
						}
					}
				} else {
					MA_NoData()
				}

			}


		}
	)


	// }
}

