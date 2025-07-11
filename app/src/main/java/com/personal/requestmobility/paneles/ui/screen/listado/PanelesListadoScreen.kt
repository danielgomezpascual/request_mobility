package com.personal.requestmobility.paneles.ui.screen.listado

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.componentes.TituloScreen
import com.personal.requestmobility.core.composables.edittext.MA_TextBuscador
import com.personal.requestmobility.core.composables.listas.MA_Lista
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.paneles.ui.componente.PanelListItem
import com.personal.requestmobility.paneles.ui.screen.listado.PanelesListadoVM.UIState
import org.koin.androidx.compose.koinViewModel


@Composable
fun PanelesListadoScreen(
    viewModel: PanelesListadoVM = koinViewModel(),
    navegacion: (EventosNavegacion) -> Unit,
) {
	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.onEvent(PanelesListadoVM.Eventos.Cargar)
	}


	when (uiState) {

		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).mensaje)
		is UIState.Loading -> LoadingScreen((uiState as UIState.Loading).mensaje)
		is UIState.Success -> SuccessListadoPaneles(viewModel = viewModel, uiState = uiState as UIState.Success, navegacion = navegacion)
	}


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessListadoPaneles(
    viewModel: PanelesListadoVM,
    uiState: UIState.Success,
    navegacion: (EventosNavegacion) -> Unit,
) {


	MA_ScaffoldGenerico(titulo = "Paneles",
						tituloScreen = TituloScreen.Paneles,
						navegacion = { navegacion(EventosNavegacion.MenuApp) }, volver = false, contenidoBottomBar = {
		BottomAppBar() {
			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom

			) {

				MA_IconBottom(modifier = Modifier.weight(1f), icon = Features.Menu().icono, labelText = Features.Menu().texto, onClick = { navegacion(EventosNavegacion.MenuHerramientas) })
				Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f))
				MA_IconBottom(modifier = Modifier.weight(1f), icon = Features.Nuevo().icono, labelText = Features.Nuevo().texto, color = Features.Nuevo().color, onClick = { navegacion(EventosNavegacion.NuevoPanel) })


			}


		}
	}, contenido = {

		Column(modifier = Modifier.fillMaxWidth()) {


			MA_Card(){
				Column(){
					MA_TextBuscador(
						searchText = uiState.textoBuscar,
						onSearchTextChanged = { it ->
							viewModel.onEvent(PanelesListadoVM.Eventos.Buscar(it))
						},
					)

					MA_Lista(data = uiState.lista) { item ->
						PanelListItem(item, onClickItem = {
							navegacion(EventosNavegacion.CargarPanel(item.id))
						})
					}
				}

			}


		}

	})

}
