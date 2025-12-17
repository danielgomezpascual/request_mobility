package com.personal.metricas.kpi.ui.screen.listado

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
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.botones.MA_BotonSecundarioSinBorde
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextBuscador
import com.personal.metricas.core.composables.listas.MA_Lista
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion

import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.kpi.ui.composables.KpiListItem
import com.personal.metricas.kpi.ui.screen.listado.KpisListadoVM.UIState
import com.personal.metricas.menu.Features
import org.koin.androidx.compose.koinViewModel


@Composable
fun KpisListadoScreen(
	viewModel: KpisListadoVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {
	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.onEvent(KpisListadoVM.Eventos.Cargar)
	}


	when (uiState) {
		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).mensaje)
		is UIState.Loading -> LoadingScreen((uiState as UIState.Loading).mensaje)
		is UIState.Success -> SucessListadoLectoras(
			viewModel = viewModel,
			uiState = uiState as UIState.Success,
			navegacion = navegacion
		)
	}


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SucessListadoLectoras(
	viewModel: KpisListadoVM,
	uiState: UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {
	var mostrarBuscador by remember { mutableStateOf(false) }

	MA_ScaffoldGenerico(
		tituloScreen = TituloScreen.Kpi,
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
						viewModel.onEvent(KpisListadoVM.Eventos.Buscar(""))
					}
				}
				MA_IconBottom(icon = Features.Menu().icono) { navegacion(EventosNavegacion.MenuHerramientas) }
				MA_IconBottom(icon = Features.Nuevo().icono) { navegacion(EventosNavegacion.NuevoKPI) }
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
							viewModel.onEvent(KpisListadoVM.Eventos.Buscar(it))
						},
					)
				}
				
				MA_Card() {
					Column() {
						MA_Lista(data = uiState.lista) { item ->
							KpiListItem(item,
										onClickItem = { navegacion(EventosNavegacion.CargarKPI(item.id)) })

						}
					}
				}
			}
		}
	)
}