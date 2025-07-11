package com.personal.requestmobility.dashboards.ui.screen.detalle

import MA_IconBottom
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.botones.MA_BotonSecundario
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.checks.MA_SwitchNormal
import com.personal.requestmobility.core.composables.combo.MA_ComboLista
import com.personal.requestmobility.core.composables.componentes.TituloScreen
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.imagenes.MA_Icono
import com.personal.requestmobility.core.composables.labels.MA_Titulo2
import com.personal.requestmobility.core.composables.listas.MA_ListaReordenable_EstiloYouTube
import com.personal.requestmobility.core.composables.modales.MA_BottomSheet
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.core.utils.reemplazaValorFila
import com.personal.requestmobility.dashboards.domain.entidades.TipoDashboard
import com.personal.requestmobility.dashboards.ui.composables.SeleccionPanelItemDashboard
import com.personal.requestmobility.kpi.ui.composables.KpiComboItem
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.paneles.domain.entidades.PanelData
import com.personal.requestmobility.paneles.ui.componente.MA_Panel
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetalleDashboardUI(
	identificador: Int,
	viewModel: DetalleDashboardVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {
	val uiState by viewModel.uiState.collectAsState()

	// LaunchedEffect con Unit en el ejemplo, pero es mejor usar identificador como key
	// si puede cambiar y queremos recargar. El ejemplo usa Unit
	// .
	LaunchedEffect(Unit) { // Siguiendo el ejemplo
		viewModel.onEvento(DetalleDashboardVM.Eventos.Cargar(identificador))
	}


	// Si el identificador pudiera cambiar mientras la pantalla está en la pila,
	// LaunchedEffect(identificador) { ... } sería más robusto.

	when (val state = uiState) { // Renombrado uiState a state
		is DetalleDashboardVM.UIState.Error   -> ErrorScreen(state.mensaje)
		is DetalleDashboardVM.UIState.Loading -> LoadingScreen(state.mensaje)
		is DetalleDashboardVM.UIState.Success -> DetalleDashboardUIScreen( // Nombre corregido
			viewModel = viewModel, uiState = state, // Pasando el objeto correcto
			navegacion = navegacion)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleDashboardUIScreen(
	// Nombre corregido del Composable de éxito
	viewModel: DetalleDashboardVM,
	uiState: DetalleDashboardVM.UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {

	val sheetStateCondicionCelda = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	val scroll = rememberScrollState()
	val scope = rememberCoroutineScope() // Se mantiene dentro del componente


	val dashboardUI = uiState.dashboardUI
	MA_ScaffoldGenerico(tituloScreen = TituloScreen.DashboardDetalle, volver = false, titulo = if (dashboardUI.id == 0) "Nuevo Dashboard" else "Datos Dashboard", // Título adaptado
		// 'navegacion' en ScaffoldGenerico del ejemplo original es la acción del icono de navegación del TopAppBar
						navegacion = { }, // Adaptado para claridad, asume que ScaffoldGenerico tiene 'navigateUp'
						contenidoBottomBar = {
							BottomAppBar() {
								Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom

								) {

									MA_IconBottom(modifier = Modifier.weight(1f), icon = Features.Menu().icono, labelText = Features.Menu().texto, onClick = {
										navegacion(EventosNavegacion.MenuDashboard)
									})


									MA_IconBottom(modifier = Modifier.weight(1f), icon = Features.Previo().icono, labelText = Features.Previo().texto, color = Features.Previo().color, onClick = {
//										navegacion(EventosNavegacion.VisualizadorDashboard(uiState.dashboardUI.id, ""))
										scope.launch { sheetStateCondicionCelda.show() }

									})


									Spacer(modifier = Modifier
										.fillMaxWidth()
										.weight(1f))

									MA_IconBottom(modifier = Modifier.weight(1f), icon = Features.Eliminar().icono, labelText = Features.Eliminar().texto, color = Features.Eliminar().color, onClick = {
										viewModel.onEvento(DetalleDashboardVM.Eventos.Eliminar(navegacion))
										//navegacion(EventosNavegacion.MenuDashboard)
									})

									MA_IconBottom(modifier = Modifier.weight(1f), icon = Features.Guardar().icono, labelText = Features.Guardar().texto, color = Features.Guardar().color, onClick = {
										viewModel.onEvento(DetalleDashboardVM.Eventos.Guardar(navegacion))
										//navegacion(EventosNavegacion.MenuDashboard)
									})


								}

							}

						}, contenido = {
			Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState()) // Para que el contenido sea scrollable
			) {


				MA_Avatar(dashboardUI.nombre)


				Column {
					MA_Titulo2("Informacion")


					MA_SwitchNormal(valor = dashboardUI.home, titulo = "Home", icono = Icons.Default.Star) { valor -> viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeInicial(valor)) }

				}

				MA_Card {
					Column() {
						MA_TextoNormal(valor = dashboardUI.nombre, titulo = "Nombre", // Equivalente a "Item"
									   onValueChange = { valor ->
										   viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeNombre(valor))
									   })

						// No hay CheckBoxNormal para "Global" en Dashboard

						MA_TextoNormal(
							valor = dashboardUI.descripcion,
							titulo = "Descripción", // Equivalente a "Proveedor"
							onValueChange = { valor ->
								viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeDescripcion(valor))
							},

							)


						// No hay más campos como "Codigo Organizacion" o "Codigo" para Dashboard

					}

				}


				MA_Titulo2("KPI")
				MA_Card {
					Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(125.dp)) {


						MA_SwitchNormal(valor = (dashboardUI.tipo == TipoDashboard.Dinamico()), titulo = "Dinamico", icono = Icons.Default.Star) { valor ->
							viewModel.onEvento(DetalleDashboardVM.Eventos.onChangeTipoDashboard(valor))
						}


						if (dashboardUI.tipo == TipoDashboard.Dinamico()) {

							Row(modifier = Modifier.fillMaxWidth()) {


								MA_ComboLista<KpiUI>(modifier = Modifier.weight(1f), titulo = "", descripcion = "Seleccione el KPI a enlazar", valorInicial = {
									KpiComboItem(kpiUI = dashboardUI.kpiOrigen ?: KpiUI())

								}, elementosSeleccionables = uiState.kpisDisponibles, item = { kpiUI ->
									KpiComboItem(kpiUI = kpiUI)
								}, onClickSeleccion = { kpiUI ->
									viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeKpiSeleccionado(kpiUI))
								})

								Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.clickable(enabled = true, onClick = { navegacion(EventosNavegacion.CargarKPI(dashboardUI.kpiOrigen.id)) })) {
									MA_Icono(Icons.Default.DoubleArrow, modifier = Modifier.size(16.dp))
								}


								/*MA_Icono(modifier = Modifier/*.weight(1f)*/, icono = Icons.Default.PlayArrow, onClick = {

								})*/
							}


						}


					}
				}

				MA_Titulo2("Paneles")
				MA_Card {

					Box(modifier = Modifier.height(400.dp)) {

						val paneles: List<PanelUI> = dashboardUI.listaPaneles

						MA_ListaReordenable_EstiloYouTube(data = dashboardUI.listaPaneles.sortedBy { it.orden }, itemContent = { panel, isDragging ->
							// Tu Composable para el ítem.
							// Puedes usar 'isDragging' para cambiar la apariencia si lo deseas
							// ej. MiPanelItem(panel, if (isDragging) Modifier.border(...) else Modifier)
							Row(modifier = Modifier.fillMaxWidth()) {

								Box(modifier = Modifier.weight(1f)) {
									SeleccionPanelItemDashboard(panel) { panelSeleccionado ->
										App.log.d("[PREV] ${panelSeleccionado.seleccionado} ${panelSeleccionado.titulo}")
										val panelesR: List<PanelUI> = paneles.map { panel ->
											if (panel.id == panelSeleccionado.id) {
												App.log.d("Encontrado")
												App.log.d("${!panelSeleccionado.seleccionado} ${panelSeleccionado.titulo}")
												panel.copy(seleccionado = !panelSeleccionado.seleccionado)
											} else {
												panel
											}
										}

										val p = panelesR.first { it.id == panelSeleccionado.id }
										App.log.d("[POST] ${p.seleccionado} ${p.titulo}")
										viewModel.onEvento(DetalleDashboardVM.Eventos.OnActualizarPaneles(panelesR))

									}
								}

								Box(contentAlignment = Alignment.Center, modifier = Modifier

									.clickable(enabled = true, onClick = {
										navegacion(EventosNavegacion.CargarPanel(panel.id))
									})) {
									MA_Icono(Icons.Default.DoubleArrow, modifier = Modifier.size(16.dp))
								}

							}

						}, onItemClick = { /* ... */ }, onListReordered = { listaReordenada ->

							var listaR: List<PanelUI> = emptyList()
							listaReordenada.forEachIndexed { indice, panel ->
								listaR = listaR.plus(panel.copy(orden = indice))
							}
							viewModel.onEvento(DetalleDashboardVM.Eventos.OnActualizarPaneles(listaR))

						}, itemHeight = 72.dp // ¡IMPORTANTE! Ajusta esto a la altura real de tus ítems
						)
					}
				}


				//------------------

				MA_BottomSheet(sheetStateCondicionCelda, onClose = {
					{ scope.launch { sheetStateCondicionCelda.hide() } }
				}, contenido = {

					Box(Modifier) {
						Column(modifier = Modifier.verticalScroll(state = scroll)) {

							MA_BotonSecundario(texto = "Cerrar") { scope.launch { sheetStateCondicionCelda.hide() } }



							dashboardUI.listaPaneles.filter { it.seleccionado }.forEach { panelUI ->
								lateinit var p: PanelUI
								if (uiState.dashboardUI.tipo == TipoDashboard.Dinamico()) {
									val sql = panelUI.kpi.sql
									p = panelUI.copy(kpi = panelUI.kpi.copy(sql = sql.reemplazaValorFila(uiState.dashboardUI.parametros)))
								} else {
									p = panelUI
								}

								MA_Panel(panelData = PanelData.fromPanelUI(p))

							}

						}
					}
				})


				//------------------


			}
		})
}