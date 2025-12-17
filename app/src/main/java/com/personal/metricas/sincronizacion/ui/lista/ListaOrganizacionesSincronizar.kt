package com.personal.metricas.sincronizacion.ui.lista

import MA_IconBottom
import MA_Morph
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.checks.MA_CheckBoxNormal
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextBuscador
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartOrganizaciones(navegacion: (EventosNavegacion) -> Unit) {

	ListaOrganizacinesSincronizar(navegacion = navegacion, configuracionInicial = true)
}

@Composable
fun ListaOrganizacinesSincronizar(
	configuracionInicial: Boolean = false,
	viewModel: ListaOrganizacionesSincronizarVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	LaunchedEffect(Unit) { viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.Cargar) }

	// Observando el flujo de estado
	val uiState by viewModel.uiState.collectAsState()
	when (uiState) {
		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).message)
		UIState.Trabajando -> LoadingScreen()
		is UIState.Success ->
			Success(viewModel, (uiState as UIState.Success), navegacion, configuracionInicial)
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
	var mostrarBuscador by remember { mutableStateOf(false) }

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
				MA_IconBottom(
					icon = Icons.Default.Search,
					color = Color.DarkGray
				) {
					mostrarBuscador = !mostrarBuscador
					if (!mostrarBuscador) {
						viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.Buscar(""))
					}
				}
				MA_IconBottom(
					icon = Features.EliminarDatosActuales().icono,
					color = Features.EliminarDatosActuales().color
				) {
					viewModel.onEvent(
						ListaOrganizacionesSincronizarVM.Eventos.EliminarDatosActuales
					)
				}
				MA_IconBottom(
					icon = Features.Sincronizar().icono,
					color = Features.Sincronizar().color
				) {
					viewModel.onEvent(
						ListaOrganizacionesSincronizarVM.Eventos.RealizarSincronizacion
					)
				}
			}
		},
		contenido = {
			Column(modifier = Modifier.fillMaxWidth()) {
				if (!uiState.infoSincro.isEmpty()) {
					Box(
						modifier =
							Modifier
								.fillMaxWidth()
								.background(color = Color.Black)
								.padding(4.dp),
						contentAlignment = Alignment.Center
					) {
						MA_LabelNormal(
							uiState.infoSincro,
							color = Color.White,
							alineacion = TextAlign.Center
						)
					}
				}

				if (configuracionInicial) {
					Column(
						verticalArrangement = Arrangement.Top,
						horizontalAlignment = Alignment.CenterHorizontally,
						modifier = Modifier
							.padding(20.dp)
							.fillMaxWidth()
					) {

						/*MA_LabelNegrita("Primeros pasos")
												MA_Spacer()
												MA_Avatar(texto = "1", size = 70.dp, color =  Color.Gray, fontSize = 40.sp)
												MA_Spacer()
												MA_LabelNormal("Seleccione las organizaciones de las que desea obtener la información y pulse en Sincronizar ", alineacion = TextAlign.Center)
												MA_Spacer()
						*/

						MA_PrimerosPasos(
							"Organizaciones",
							"1",
							"Seleccione las organizaciones de las que desea obtener la información y pulse en Sincronizar "
						)

						Row() {
							MA_BotonSecundario("Sincroniar") {
								viewModel.onEvent(
									ListaOrganizacionesSincronizarVM.Eventos
										.RealizarSincronizacion
								)
							}
							MA_BotonPrincipal("Continuar...") {
								navegacion(EventosNavegacion.MenuHerramientasStart)
							}
						}
					}
				}

				// Barra de búsqueda
				AnimatedVisibility(
					visible = mostrarBuscador,
					enter = expandVertically() + fadeIn(),
					exit = shrinkVertically() + fadeOut()
				) {
					MA_TextBuscador(
						searchText = uiState.textoBuscar,
						onSearchTextChanged = { it ->
							viewModel.onEvent(
								ListaOrganizacionesSincronizarVM.Eventos.Buscar(it)
							)
						},
					)
				}

				val selectedCount = uiState.organizaciones.count { it.seleccionado }
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 4.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					MA_LabelNegrita(
						modifier = Modifier.padding(3.dp),
						valor = "Organizaciones ($selectedCount)"
					)
					MA_CheckBoxNormal(valor = uiState.todos, titulo = "Todas") {
						viewModel.onEvent(
							ListaOrganizacionesSincronizarVM.Eventos.AplicarTodos(it)
						)
					}
				}
				MA_Card {


					if (selectedCount > 0) {
						Row(
							modifier =
								Modifier
									.padding(4.dp)
									.horizontalScroll(rememberScrollState())
									.fillMaxWidth(),
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.spacedBy(4.dp)
						) {
							uiState.organizaciones.filter { it.seleccionado }.forEach { organizacion ->
								val colors = listOf(
									Color(0xFFFFCDD2).copy(alpha = 0.5f), // Red 100
									Color(0xFFF8BBD0).copy(alpha = 0.5f), // Pink 100
									Color(0xFFE1BEE7).copy(alpha = 0.5f), // Purple 100
									Color(0xFFD1C4E9).copy(alpha = 0.5f), // Deep Purple 100
									Color(0xFFC5CAE9).copy(alpha = 0.5f), // Indigo 100
									Color(0xFFBBDEFB).copy(alpha = 0.5f), // Blue 100
									Color(0xFFB2EBF2).copy(alpha = 0.5f), // Cyan 100
									Color(0xFFB2DFDB).copy(alpha = 0.5f), // Teal 100
									Color(0xFFC8E6C9).copy(alpha = 0.5f), // Green 100
									Color(0xFFF0F4C3).copy(alpha = 0.5f), // Lime 100
									Color(0xFFFFF9C4).copy(alpha = 0.5f), // Yellow 100
									Color(0xFFFFE0B2).copy(alpha = 0.5f), // Orange 100
									Color(0xFFD7CCC8).copy(alpha = 0.5f)  // Brown 100
								)
								val iconColor = colors[kotlin.math.abs(organizacion.organizationCode.hashCode()) % colors.size]

								Box(
									contentAlignment = Alignment.Center,
									modifier = Modifier
										.clickable {
											viewModel.onEvent(
												ListaOrganizacionesSincronizarVM.Eventos
													.OnChangeSeleccionCheck(organizacion)
											)
										}
										.size(36.dp)
										.clip(RoundedCornerShape(8.dp))
										.background(iconColor)
										.border(
											width = 1.dp,
											color = Color.Transparent, // Removed border or keep it transparent
											shape = RoundedCornerShape(8.dp)
										)
								) {
									Text(
										text = organizacion.organizationCode.take(3).uppercase(),
										color = Color.DarkGray,
										fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
										fontSize = 12.sp
									)
								}
							}
						}
					}
				}
				MA_Card() {
					Column {


						MA_Lista(data = uiState.organizaciones.filter { it.visible }) { organizacionUI ->
							OrganizacionListItemSincronizar(
								organizacionUI = organizacionUI,
								onClickItem = {
									viewModel.onEvent(
										ListaOrganizacionesSincronizarVM.Eventos
											.OnChangeSeleccionCheck(organizacionUI)
									)
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
		}
	)
}
