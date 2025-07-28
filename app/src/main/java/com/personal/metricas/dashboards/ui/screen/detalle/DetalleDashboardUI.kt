package com.personal.metricas.dashboards.ui.screen.detalle

import MA_IconBottom
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.metricas.App
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.checks.MA_SwitchNormal
import com.personal.metricas.core.composables.combo.MA_ComboLista
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextBuscador
import com.personal.metricas.core.composables.edittext.MA_TextoEditable
import com.personal.metricas.core.composables.edittext.MA_TextoNormal
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.labels.MA_Titulo2
import com.personal.metricas.core.composables.listas.MA_ListaReordenable_EstiloYouTube
import com.personal.metricas.core.composables.modales.MA_BottomSheet
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.dashboards.domain.entidades.TipoDashboard
import com.personal.metricas.dashboards.ui.composables.MA_EtiquetaItem
import com.personal.metricas.dashboards.ui.composables.SeleccionPanelItemDashboard
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.kpi.ui.composables.KpiComboItem
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.menu.Features
import com.personal.metricas.paneles.domain.entidades.PanelData
import com.personal.metricas.paneles.ui.componente.MA_Panel
import com.personal.metricas.paneles.ui.entidades.PanelUI
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.mp.KoinPlatform.getKoin

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
	val sheetEtiqueta = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	val scroll = rememberScrollState()
	val scope = rememberCoroutineScope() // Se mantiene dentro del componente


	val dashboardUI = uiState.dashboardUI
	MA_ScaffoldGenerico(tituloScreen = TituloScreen.DashboardDetalle,
						navegacion = navegacion, // Adaptado para claridad, asume que ScaffoldGenerico tiene 'navigateUp'
						accionesSuperiores = {
							Row(
								modifier = Modifier.fillMaxWidth(),
								horizontalArrangement = Arrangement.End,
								verticalAlignment = Alignment.Top

							) {
								MA_IconBottom(icon = Features.Previo().icono, color = Features.Previo().color) { scope.launch { sheetStateCondicionCelda.show() } }
								MA_Spacer()
								MA_IconBottom(icon = Features.Eliminar().icono, color = Features.Eliminar().color) { viewModel.onEvento(DetalleDashboardVM.Eventos.Eliminar(navegacion)) }
								MA_IconBottom(icon = Features.Guardar().icono, color = Features.Guardar().color) { scope.launch { viewModel.onEvento(DetalleDashboardVM.Eventos.Guardar(navegacion)) } }
							}
						},


						contenido = {
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






								MA_Titulo2("Etiqueta")

								MA_Card {
									Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
										.fillMaxWidth()
									)
									{
										MA_IconBottom(icon = Icons.Default.Add) {
											viewModel.onEvento(DetalleDashboardVM.Eventos.OnNuevaEtiqueta)
											scope.launch { sheetEtiqueta.show() }
										}


										MA_ComboLista<Etiquetas>(modifier = Modifier
											.fillMaxWidth()
											.weight(1f),
																 titulo = "",
																 descripcion = "Seleccione la etiqueta para el elemento",
																 valorInicial = {
																	 MA_EtiquetaItem(uiState.etiquetaSeleccionada)
																 },
																 elementosSeleccionables = uiState.etiquetasDisponibles,
																 item = { etiqueta ->
																	 MA_EtiquetaItem(etiqueta)

																 },
																 onClickSeleccion = { etiqueta ->
																	 viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeEtiqueta(etiqueta))
																 })

										MA_IconBottom(icon = Icons.Default.Edit) {
											viewModel.onEvento(DetalleDashboardVM.Eventos.OnEditarEtiqueta(uiState.etiquetaSeleccionada))
											scope.launch { sheetEtiqueta.show() }
										}

									}
								}

								MA_Titulo2("KPI")
								MA_Card {
									Column {
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
									val sc = rememberScrollState()
									Row(modifier = Modifier
										.horizontalScroll(sc)
										.background(color = Color(255, 248, 225, 255))) {
										dashboardUI.kpiOrigen.dameColumnasSQL().forEach { columna ->
											MA_LabelMini(columna.nombre)
										}
									}


								}

								MA_Titulo2("Paneles")
								MA_Card {

									Box(modifier = Modifier.height(400.dp)) {

										val paneles: List<PanelUI> = dashboardUI.listaPaneles

										Column() {
											MA_TextBuscador(
												searchText = uiState.textoBuscarPaneles,
												onSearchTextChanged = { texto -> // Parámetro renombrado a 'texto'
													viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeBuscadorPaneles(texto))
												}
											)


											MA_ListaReordenable_EstiloYouTube(data = dashboardUI.listaPaneles.filter { it.visible }.sortedBy { it.orden }, itemContent = { panel, isDragging ->
												// Tu Composable para el ítem.
												// Puedes usar 'isDragging' para cambiar la apariencia si lo deseas
												// ej. MiPanelItem(panel, if (isDragging) Modifier.border(...) else Modifier)
												Row(modifier = Modifier.fillMaxWidth()) {

													Box(modifier = Modifier.weight(1f)) {
														SeleccionPanelItemDashboard(panel, dashboardUI.kpiOrigen.dameColumnasSQL()) { panelSeleccionado ->
															val panelesR: List<PanelUI> = paneles.map { panel ->
																if (panel.id == panelSeleccionado.id) {
																	panel.copy(seleccionado = !panelSeleccionado.seleccionado)
																} else {
																	panel
																}
															}

															val p = panelesR.first { it.id == panelSeleccionado.id }
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
								}


								//------------------

								MA_BottomSheet(sheetStateCondicionCelda, onClose = {
									{ scope.launch { sheetStateCondicionCelda.hide() } }
								}, contenido = {

									Box(Modifier) {
										Column(modifier = Modifier.verticalScroll(state = scroll)) {

											MA_BotonSecundario(texto = "Cerrar") { scope.launch { sheetStateCondicionCelda.hide() } }



											dashboardUI.listaPaneles.filter { it.seleccionado }.forEach { panelUI ->
												//val notasManager = getKoin().get<NotasManager>()
												val notasManager = NotasManager.instancia()
												MA_Panel(panelData = PanelData.fromPanelUI(panelUI, notasManager, dashboardUI.parametros))

											}

										}
									}
								})


								//------------------


								MA_BottomSheet(sheetEtiqueta, onClose = {
									{ scope.launch { sheetEtiqueta.hide() } }
								}, contenido = {

									Box(Modifier) {
										Column(modifier = Modifier.verticalScroll(state = scroll)) {


											MA_TextoEditable(valor = uiState.etiquetaSeleccionada.etiqueta, titulo = "Etiqueta") {
												viewModel.onEvento(DetalleDashboardVM.Eventos.ModificarValorEtiqueta(it))
											}


											Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
												MA_BotonSecundario("Cancelar") {
													scope.launch { sheetEtiqueta.hide() }
												}




												MA_BotonPrincipal("Guardar") {
													viewModel.onEvento(DetalleDashboardVM.Eventos.OnGuardarEtiqueta)
													scope.launch { sheetEtiqueta.hide() }
												}

											}

										}
									}
								})

								//------------------


							}
						})
}