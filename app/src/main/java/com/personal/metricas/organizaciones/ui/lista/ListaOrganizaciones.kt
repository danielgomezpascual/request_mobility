package com.personal.metricas.organizaciones.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextBuscador
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.listas.MA_Lista
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.organizaciones.ui.composables.OrganizacionListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListaOrganizaciones(
	viewModel: ListaOrganizacionesVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	LaunchedEffect(Unit) {
		viewModel.onEvent(ListaOrganizacionesVM.Eventos.Cargar)
	}

	// Observando el flujo de estado
	val uiState by viewModel.uiState.collectAsState()
	when (uiState) {
		is ListaOrganizacionesVM.UIState.Error   -> ErrorScreen((uiState as ListaOrganizacionesVM.UIState.Error).message)
		ListaOrganizacionesVM.UIState.Trabajando -> LoadingScreen()
		is ListaOrganizacionesVM.UIState.Success -> ListaOrganizacionesSuccess(
			viewModel, (uiState as ListaOrganizacionesVM.UIState.Success),
			navegacion
		)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaOrganizacionesSuccess(
	viewModel: ListaOrganizacionesVM,
	uiState: ListaOrganizacionesVM.UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {

	var mostrarContenidoDialogoEliminar by remember { mutableStateOf(false) }
	var mostrarContenidoDialogoInformacion by remember { mutableStateOf(false) }

	MA_ScaffoldGenerico(

		tituloScreen = TituloScreen.Sincronizar,
		navegacion = navegacion,
		accionesSuperiores = {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.Top

			) {

			}
		},
		contenido = {
			Column(
				modifier = Modifier
					.fillMaxWidth()
			) {


				// Barra de búsqueda
				MA_TextBuscador(
					searchText = uiState.textoBuscar,
					onSearchTextChanged = { it ->
						viewModel.onEvent(ListaOrganizacionesVM.Eventos.Buscar(it))
					},
				)

				MA_LabelNegrita(modifier = Modifier.padding(3.dp), valor = "Organizaciones")

				MA_Card() {
					Column {


						MA_Lista(data = uiState.organizaciones.filter { it.visible }) { organizacionUI ->
							OrganizacionListItem(
								organizacionUI = organizacionUI,
								onClickItem = {
									navegacion(EventosNavegacion.CargarSincronizacionOrganizacion(organizacionUI.organizationCode))

								},


								)
						}
					}


				}
			}
		})
}


