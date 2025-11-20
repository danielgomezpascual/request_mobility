package com.personal.metricas.sincronizacion.ui.lista

import MA_IconBottom
import MA_Morph
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.sp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.checks.MA_CheckBoxNormal
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextBuscador
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.listas.MA_Lista
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.menu.Features
import com.personal.metricas.sincronizacion.ui.composables.OrganizacionListItemSincronizar
import com.personal.metricas.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM.UIState
import com.personal.metricas.start.composables.MA_PrimerosPasos
import org.apache.commons.jexl3.JxltEngine
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartOrganizaciones(navegacion: (EventosNavegacion) -> Unit) {


		ListaOrganizacinesSincronizar(navegacion = navegacion, configuracionInicial = true)



}

@Composable
fun ListaOrganizacinesSincronizar(
	configuracionInicial: Boolean = false,
	viewModel: ListaOrganizacionesSincronizarVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit

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
			navegacion,
			configuracionInicial
		)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Success(
	viewModel: ListaOrganizacionesSincronizarVM,
	uiState: UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
	configuracionInicial: Boolean = false,
) {

	var mostrarContenidoDialogoEliminar by remember { mutableStateOf(false) }
	var mostrarContenidoDialogoInformacion by remember { mutableStateOf(false) }

	MA_ScaffoldGenerico(
		mostrarBotonesSuperioresYBarraInferior = !configuracionInicial,
		tituloScreen = TituloScreen.Sincronizar,
		navegacion = navegacion,
		accionesSuperiores = {

				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.End,
					verticalAlignment = Alignment.Top

				) {
					MA_IconBottom(icon = Features.EliminarDatosActuales().icono, color = Features.EliminarDatosActuales().color) { viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.EliminarDatosActuales) }
					MA_IconBottom(icon = Features.Sincronizar().icono, color = Features.Sincronizar().color) { viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.RealizarSincronizacion) }
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


				if (configuracionInicial){
					Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp).fillMaxWidth()) {

						/*MA_LabelNegrita("Primeros pasos")
						MA_Spacer()
						MA_Avatar(texto = "1", size = 70.dp, color =  Color.Gray, fontSize = 40.sp)
						MA_Spacer()
						MA_LabelNormal("Seleccione las organizaciones de las que desea obtener la información y pulse en Sincronizar ", alineacion = TextAlign.Center)
						MA_Spacer()
*/


						MA_PrimerosPasos("Organizaciones",
										 "1",
										 "Seleccione las organizaciones de las que desea obtener la información y pulse en Sincronizar ")


						Row(){
							MA_BotonSecundario("Sincroniar") { viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.RealizarSincronizacion)  }
							MA_BotonPrincipal("Continuar...") {
								navegacion(EventosNavegacion.MenuHerramientasStart)
							}
						}
					}
				}



				// Barra de búsqueda
				MA_TextBuscador(
					searchText = uiState.textoBuscar,
					onSearchTextChanged = { it ->
						viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.Buscar(it))
					},
				)

				MA_LabelNegrita(modifier = Modifier.padding(3.dp), valor = "Organizaciones")
				MA_Card() {
					Column {
						Row(modifier = Modifier
							.horizontalScroll(rememberScrollState())
							.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

							MA_CheckBoxNormal(valor = uiState.todos, titulo = "") {
								viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.AplicarTodos(it))
							}
							uiState.organizaciones.filter { it.seleccionado }.forEach { organizacion ->
								Box(contentAlignment = Alignment.Center) {
									MA_LabelNormal(modifier = Modifier
										.padding(4.dp)
										.background(color = Color(255, 245, 157, 255)),
												   valor = organizacion.organizationCode)
								}
							}
						}




						MA_Lista(data = uiState.organizaciones.filter { it.visible }) { organizacionUI ->
							OrganizacionListItemSincronizar(
								organizacionUI = organizacionUI,
								onClickItem = {
									viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.OnChangeSeleccionCheck(organizacionUI))
								},


								)
						}
					}


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


