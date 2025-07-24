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
import org.koin.mp.KoinPlatform.getKoin


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
	/*  Column(
		  verticalArrangement = Arrangement.Center,
		  horizontalAlignment = Alignment.CenterHorizontally,
		  modifier = Modifier.fillMaxSize()
	  ) {*/

	MA_ScaffoldGenerico(
		titulo = "",
		tituloScreen = TituloScreen.Home,
		volver = false,
		navegacion = { },
		contenidoBottomBar = {

			BottomAppBar() {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.Bottom

				) {

					MA_IconBottom(
						//   modifier = Modifier.weight(1f),
						icon = Features.Sincronizar().icono,
						labelText = Features.Sincronizar().texto,
						seleccionado = false,
						destacado = false,
						onClick = { navegacion(EventosNavegacion.Sincronizacion) }
					)


					MA_IconBottom(
						//   modifier = Modifier.weight(1f),
						icon = Features.EndPoints().icono,
						labelText = Features.EndPoints().texto,
						seleccionado = false,
						destacado = false,
						onClick = { navegacion(EventosNavegacion.MenuEndPoints) }
					)


					MA_IconBottom(
						//   modifier = Modifier.weight(1f),
						icon = Features.Cuadriculas().icono,
						labelText = Features.Cuadriculas().texto,
						seleccionado = true,
						destacado = true,
						onClick = { navegacion(EventosNavegacion.MenuVisualizadorDashboard) }
					)

					MA_IconBottom(
						//   modifier = Modifier.weight(1f),
						icon = Features.Herramientas().icono,
						labelText = Features.Herramientas().texto,
						seleccionado = false,
						destacado = false,
						onClick = { navegacion(EventosNavegacion.MenuHerramientas) }
					)

				}


			}
		},
		contenido = {


			val scroll = rememberScrollState()


			Column(modifier = Modifier
				.fillMaxSize()
				.verticalScroll(state = scroll),
				   verticalArrangement = Arrangement.Center,
				   horizontalAlignment = Alignment.CenterHorizontally) {
				if (uiState.paneles.size > 0) {
					//val notasManager = getKoin().get<NotasManager>()

					val notasManager = NotasManager.instancia()

					uiState.paneles.forEach { panelUI ->
						MA_Card {
							MA_Panel(panelData = PanelData.fromPanelUI(panelUI,notasManager, Parametros()))
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

