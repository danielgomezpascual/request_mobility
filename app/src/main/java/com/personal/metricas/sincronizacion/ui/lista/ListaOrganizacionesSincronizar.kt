package com.personal.metricas.sincronizacion.ui.lista

import MA_IconBottom
import MA_Morph
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.checks.MA_CheckBoxNormal
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextBuscador
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.listas.MA_Lista
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.menu.Features
import com.personal.metricas.sincronizacion.ui.composables.OrganizacionListItem
import com.personal.metricas.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM.UIState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListaOrganizacinesSincronizar(
	viewModel: ListaOrganizacionesSincronizarVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	LaunchedEffect(Unit) {
		viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.Cargar)
	}

	// Observando el flujo de estado
	val uiState by viewModel.uiState.collectAsState()
	when (uiState) {
		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).message)
		UIState.Trabajando -> LoadingScreen()
		is UIState.Success -> Success(
			viewModel, (uiState as UIState.Success),
			navegacion
		)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Success(
	viewModel: ListaOrganizacionesSincronizarVM,
	uiState: UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {

	var mostrarContenidoDialogoEliminar by remember { mutableStateOf(false) }
	var mostrarContenidoDialogoInformacion by remember { mutableStateOf(false) }

	MA_ScaffoldGenerico(
		titulo = "Sincroniacion",
		tituloScreen = TituloScreen.Sincronizar,
		navegacion = { navegacion(EventosNavegacion.MenuApp) },
		volver = false,
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
						onClick = { navegacion(EventosNavegacion.MenuApp) }
					)
					Spacer(
						modifier = Modifier
							.fillMaxWidth()
							.weight(1f)
					)

					MA_IconBottom(
						modifier = Modifier.weight(1f),
						icon = Features.EliminarDatosActuales().icono,
						labelText = Features.EliminarDatosActuales().texto,
						onClick = {

							viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.EliminarDatosActuales)
						}
					)


					MA_IconBottom(
						modifier = Modifier.weight(1f),
						icon = Features.Sincronizar().icono,
						labelText = Features.Sincronizar().texto,
						onClick = {

							viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.RealizarSincronizacion)
						}
					)


				}


			}
		},
		contenido = {


			Column(
				modifier = Modifier
					.fillMaxWidth()
			) {



				if (!uiState.infoSincro.isEmpty()) {
					Box(modifier = Modifier
						.fillMaxWidth()
						.background(color = Color.Black)
						.padding(4.dp),
						contentAlignment = Alignment.Center) {
						MA_LabelNormal(uiState.infoSincro, color = Color.White, alineacion = TextAlign.Center)
					}
				}

				MA_CheckBoxNormal(valor = uiState.todos, titulo = "Aplicar a todos") {
					viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.AplicarTodos(it))
				}


				// Barra de búsqueda
				MA_TextBuscador(
					searchText = uiState.textoBuscar,
					onSearchTextChanged = { it ->
						viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.Buscar(it))
					},
				)

				Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

				}




				MA_Lista(data = uiState.organizaciones.filter { it.visible }) { organizacionUI ->
					OrganizacionListItem(organizacionUI = organizacionUI, onClickItem = {
						viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.OnChangeSeleccionCheck(organizacionUI))
					})
				}


			}
			if (uiState.trabajando) {
				// Este es el nuevo componente de carga

				Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					MA_Morph()
				}

			}


		})

}


