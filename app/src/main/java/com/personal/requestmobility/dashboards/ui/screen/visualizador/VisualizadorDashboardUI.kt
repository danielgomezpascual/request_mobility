package com.personal.requestmobility.dashboards.ui.screen.visualizador

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
import com.personal.requestmobility.core.composables.componentes.TituloScreen
import com.personal.requestmobility.core.composables.labels.MA_LabelMini
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.core.utils.reemplazaValorFila
import com.personal.requestmobility.dashboards.domain.entidades.TipoDashboard
import com.personal.requestmobility.dashboards.ui.screen.visualizador.VisualizadorDashboardVM.UIState
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.paneles.domain.entidades.PanelData
import com.personal.requestmobility.paneles.ui.componente.MA_Panel
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
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
			titulo = "",
			tituloScreen = TituloScreen.DashboardLista,
			navegacion = {},
			contenidoBottomBar = {
				
				BottomAppBar() {
					Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.Start,
							verticalAlignment = Alignment.Bottom
					   
					   ) {
						
						MA_IconBottom(
								modifier = Modifier.weight(1f),
								icon = Features.Menu().icono,
								labelText = Features.Menu().texto,
								onClick = { navegacion(EventosNavegacion.MenuVisualizadorDashboard) }
									 )
						Spacer(modifier = Modifier
							.fillMaxWidth()
							.weight(1f))
						
						
					}
					
					
				}
			},
			contenido = {
				
				val scroll = rememberScrollState()
				Box(Modifier) {
					Column(modifier = Modifier.verticalScroll(state = scroll)) {


						uiState.paneles.filter { it.seleccionado }.forEach { panelUI ->
							lateinit var p: PanelUI
							MA_Panel(panelData = PanelData.fromPanelUI(panelUI, uiState.dashboardUI.parametros))
							
						}
						
					}
				}
			}
					   )
	
	
}




