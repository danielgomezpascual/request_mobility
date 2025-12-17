package com.personal.metricas.endpoints.ui.screen.listado

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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextBuscador
import com.personal.metricas.core.composables.listas.MA_Lista
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.endpoints.ui.composables.EndPointListItem

import com.personal.metricas.menu.Features
import org.koin.androidx.compose.koinViewModel


@Composable
fun EndPointsListadoScreen(
	viewModel: EndPointsListadoVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.onEvent(EndPointsListadoVM.Eventos.Cargar)
	}


	when (uiState) {
		is EndPointsListadoVM.UIState.Error   -> ErrorScreen((uiState as EndPointsListadoVM.UIState.Error).mensaje)
		is EndPointsListadoVM.UIState.Loading -> LoadingScreen((uiState as EndPointsListadoVM.UIState.Loading).mensaje)
		is EndPointsListadoVM.UIState.Success -> SuccessListadoEndPoints(
			viewModel = viewModel,
			uiState = uiState as EndPointsListadoVM.UIState.Success,
			navegacion = navegacion
		)
	}


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessListadoEndPoints(
	viewModel: EndPointsListadoVM,
	uiState: EndPointsListadoVM.UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {
	var mostrarBuscador by remember { mutableStateOf(false) }

	MA_ScaffoldGenerico(
		tituloScreen = TituloScreen.EndPoints,
		navegacion = navegacion,
		accionesSuperiores = {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.Top

			) {
				MA_IconBottom(
					icon = Icons.Default.Search,
					color = Color.DarkGray
				) {
					mostrarBuscador = !mostrarBuscador
					if (!mostrarBuscador) {
						viewModel.onEvent(EndPointsListadoVM.Eventos.Buscar(""))
					}
				}
				MA_IconBottom(icon = Features.Nuevo().icono, color = Features.Nuevo().color) { navegacion(EventosNavegacion.NuevoEndPonint) }
			}
		},
		contenido = {
			Column(
				modifier = Modifier
					.fillMaxWidth()

			) {
				// Barra de búsqueda
				AnimatedVisibility(
					visible = mostrarBuscador,
					enter = expandVertically() + fadeIn(),
					exit = shrinkVertically() + fadeOut()
				) {
					MA_TextBuscador(
						searchText = uiState.textoBuscar,
						onSearchTextChanged = { it ->
							viewModel.onEvent(EndPointsListadoVM.Eventos.Buscar(it))
						},
					)
				}
				
				MA_Card() {
					Column() {
						MA_Lista(data = uiState.lista) { item ->
							EndPointListItem(endpointUI = item,
											 onClickItem = { navegacion(EventosNavegacion.CargarEndPoint(item.id)) })

						}
					}
				}

			}


		}
	)
}