package com.personal.metricas.organizaciones.ui.lista

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.App
import com.personal.metricas.R
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextBuscador
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.imagenes.MA_ImagenDrawable
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.listas.MA_Lista
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.menu.Features
import com.personal.metricas.menu.screen.HerramientasViewModel
import com.personal.metricas.organizaciones.ui.composables.OrganizacionListItem
import com.personal.metricas.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM
import com.personal.metricas.start.composables.MA_PrimerosPasos
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScreenListaOrganizacionesPlanificacion(navegacion: (EventosNavegacion) -> Unit) {
	ListaOrganizaciones(configuracionInicial = true, navegacion = navegacion)

}

@Composable
fun ListaOrganizaciones(
	configuracionInicial: Boolean = false,
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
			configuracionInicial,
			viewModel, (uiState as ListaOrganizacionesVM.UIState.Success),
			navegacion
		)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaOrganizacionesSuccess(
	configuracionInicial: Boolean,
	viewModel: ListaOrganizacionesVM,
	uiState: ListaOrganizacionesVM.UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
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
				MA_IconBottom(icon = Features.PlanificadorAuto().icono,
							  color = Features.PlanificadorAuto().color) {
					viewModel.onEvent(ListaOrganizacionesVM.Eventos.AutoPlanificacion)
				}
			}
		},
		contenido = {
			Column(
				modifier = Modifier
					.fillMaxWidth()
			) {


				if (configuracionInicial) {
					Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier =Modifier.padding(50.dp).fillMaxSize()) {
						/*MA_ImagenDrawable(imagen = R.drawable.logo, s = 20.dp)
						MA_LabelNegrita("Primeros pasos")
						MA_Spacer()
						MA_Avatar(texto = "3", size = 70.dp, color = Color.Gray, fontSize = 40.sp)
						MA_Spacer()
						MA_LabelNormal("Si quieres cargar unos paneles y dashboard por defecto con las consultas más habituales  pulsa el sigueinte botón", alineacion = TextAlign.Center)
						MA_Spacer()
*/

						MA_PrimerosPasos("Planificaciones",
										 "3",
										 "Si quieres cargar unos paneles y dashboard por defecto con las consultas más habituales  pulsa el sigueinte botón")
						MA_Card {
							Column(horizontalAlignment = Alignment.CenterHorizontally) {

								MA_LabelNormal("Crear planificación")
								MA_IconBottom(icon = Features.PlanificadorAuto().icono,
											  color = Features.PlanificadorAuto().color) {
									viewModel.onEvent(ListaOrganizacionesVM.Eventos.AutoPlanificacion)
								}

							}

						}

						MA_BotonPrincipal("Finalizar") {
							App.sharedPrerfences.put<Boolean>(Preferencias.CONFIGURACION_INICIAL, false)
							navegacion(EventosNavegacion.HomeApp) }
					}


				} else {
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


			}
		})
}


