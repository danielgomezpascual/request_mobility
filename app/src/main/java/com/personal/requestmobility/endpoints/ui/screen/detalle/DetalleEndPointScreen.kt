package com.personal.requestmobility.endpoints.ui.screen.detalle

import MA_IconBottom
import MA_Morph
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GeneratingTokens
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
import com.personal.requestmobility.core.composables.MA_Spacer
import com.personal.requestmobility.core.composables.botones.MA_BotonPrincipal
import com.personal.requestmobility.core.composables.botones.MA_BotonSecundario
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.checks.MA_CheckBoxNormal
import com.personal.requestmobility.core.composables.componentes.TituloScreen
import com.personal.requestmobility.core.composables.edittext.MA_TextoEditable
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.imagenes.MA_Icono
import com.personal.requestmobility.core.composables.labels.MA_LabelNegrita
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.labels.MA_LabelTextoDestacado
import com.personal.requestmobility.core.composables.labels.MA_Titulo2
import com.personal.requestmobility.core.composables.listas.MA_Divider
import com.personal.requestmobility.core.composables.listas.MA_Lista
import com.personal.requestmobility.core.composables.modales.MA_BottomSheet
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.dashboards.ui.screen.detalle.DetalleDashboardVM
import com.personal.requestmobility.kpi.ui.screen.detalle.DetalleKpiVM
import com.personal.requestmobility.menu.Features
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel




@Composable
fun DetalleEndPointScreen(
	identificador: Int,
	viewModel: DetalleEndPointVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {
	val uiState by viewModel.uiState.collectAsState()

	// LaunchedEffect con Unit en el ejemplo, pero es mejor usar identificador como key
	// si puede cambiar y queremos recargar. El ejemplo usa Unit
	// .
	LaunchedEffect(Unit) { // Siguiendo el ejemplo
		viewModel.onEvento(DetalleEndPointVM.Eventos.Cargar(identificador))
	}


	// Si el identificador pudiera cambiar mientras la pantalla está en la pila,
	// LaunchedEffect(identificador) { ... } sería más robusto.

	when (val state = uiState) { // Renombrado uiState a state
		is DetalleEndPointVM.UIState.Error   -> ErrorScreen(state.mensaje)
		is DetalleEndPointVM.UIState.Loading -> LoadingScreen(state.mensaje)
		is DetalleEndPointVM.UIState.Success -> DetalleEndPointScreen( // Nombre corregido
			viewModel = viewModel, uiState = state, // Pasando el objeto correcto
			navegacion = navegacion)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleEndPointScreen(
	// Nombre corregido del Composable de éxito
	viewModel: DetalleEndPointVM,
	uiState: DetalleEndPointVM.UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {

	val sheetParametros = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	val scroll = rememberScrollState()
	val scope = rememberCoroutineScope() // Se mantiene dentro del componente


	val endPointUI = uiState.endPointUI
	MA_ScaffoldGenerico(tituloScreen = TituloScreen.EndPointsDetalle,
						volver = false,
						titulo = if (endPointUI.id == 0) "Nuevo End Point" else endPointUI.nombre, // Título adaptado
		// 'navegacion' en ScaffoldGenerico del ejemplo original es la acción del icono de navegación del TopAppBar
						navegacion = { }, // Adaptado para claridad, asume que ScaffoldGenerico tiene 'navigateUp'
						contenidoBottomBar = {
							BottomAppBar() {
								Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom

								) {

									MA_IconBottom(modifier = Modifier.weight(1f), icon = Features.Menu().icono, labelText = Features.Menu().texto, onClick = {
										navegacion(EventosNavegacion.MenuEndPoints)
									})

									Spacer(modifier = Modifier
										.fillMaxWidth()
										.weight(1f))

									MA_IconBottom(modifier = Modifier.weight(1f), icon = Features.Eliminar().icono, labelText = Features.Eliminar().texto, color = Features.Eliminar().color, onClick = {
										viewModel.onEvento(DetalleEndPointVM.Eventos.Eliminar(navegacion))

									})

									MA_IconBottom(modifier = Modifier.weight(1f), icon = Features.Guardar().icono, labelText = Features.Guardar().texto, color = Features.Guardar().color, onClick = {
										viewModel.onEvento(DetalleEndPointVM.Eventos.Guardar(navegacion))
									})

									MA_IconBottom(modifier = Modifier.weight(1f),
												  icon = Features.Probar().icono,
												  labelText = Features.Probar().texto,
												  color = Features.Probar().color, onClick = {
										viewModel.onEvento(DetalleEndPointVM.Eventos.Procesar(navegacion))
									})
								}

							}

						},
						contenido = {


							Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
								.fillMaxSize()
								.verticalScroll(rememberScrollState()) // Para que el contenido sea scrollable
							) {

								if (uiState.trabajando) {
									Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
										MA_Morph()
									}
								}



								MA_Avatar(endPointUI.nombre)
								MA_Titulo2("Informacion")



								MA_Card {
									Column() {
										MA_TextoNormal(valor = endPointUI.nombre, titulo = "Nombre", // Equivalente a "Item"
													   onValueChange = { valor ->
														   viewModel.onEvento(DetalleEndPointVM.Eventos.OnChangeNombre(valor))
													   })

										// No hay CheckBoxNormal para "Global" en Dashboard

										MA_TextoNormal(
											valor = endPointUI.descripcion,
											titulo = "Descripción", // Equivalente a "Proveedor"
											onValueChange = { valor -> viewModel.onEvento(DetalleEndPointVM.Eventos.OnChangeDescripcion(valor)) }

										)


										MA_TextoNormal(
											valor = endPointUI.tabla,
											titulo = "Tabla",
											onValueChange = { valor -> viewModel.onEvento(DetalleEndPointVM.Eventos.OnChangeTabla(valor)) }

										)

										MA_TextoNormal(
											valor = endPointUI.nodoIdentificadorFila,
											titulo = "Nodo identificador fila", // Equivalente a "Proveedor"
											onValueChange = { valor -> viewModel.onEvento(DetalleEndPointVM.Eventos.OnChangeNodoIdentificadorFila(valor)) }

										)



									}

								}



								


								MA_Titulo2("URL")
								MA_Card {
									Column {
										MA_TextoNormal(
											valor = endPointUI.url,
											titulo = "URL",
											onValueChange = { valor -> viewModel.onEvento(DetalleEndPointVM.Eventos.OnChangeURL(valor)) }
										)


									}


								}


								Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly){
									MA_Titulo2("Parámetros", modifier = Modifier.weight(1f))
									MA_IconBottom(modifier = Modifier,icon = Icons.Default.Add) {
										viewModel.onEvento(DetalleEndPointVM.Eventos.NuevoParametro)
										scope.launch { sheetParametros.show() }
									}

									MA_IconBottom(modifier = Modifier, icon = Icons.Default.GeneratingTokens) {
										viewModel.onEvento(DetalleEndPointVM.Eventos.CargarParametrosDefectoMobility)

									}

								}



								MA_Card {
									Column {
										endPointUI.parametros.ps.forEach { parametro ->
											Row(Modifier.fillMaxWidth().padding(8.dp)) {

												Box(Modifier.weight(1f).fillMaxWidth()) {


													MA_LabelTextoDestacado(valor = parametro.key, valorDestacado = parametro.valor)

												}
												Box(
													modifier = Modifier.clickable(onClick = {
														viewModel.onEvento(DetalleEndPointVM.Eventos.EliminarParametro(parametro))

													})
												) {
													MA_Icono(Icons.Default.Delete, color = Color.Red)
												}

											}

										}
										MA_Divider()
									}
								}


							}



							MA_BottomSheet(sheetParametros, onClose = {
								{ scope.launch { sheetParametros.hide() } }
							}, contenido = {
								Column {
									//	MA_BotonPrincipal("Cerrar") { scope.launch { sheetParametros.hide() } }
									MA_Titulo2(valor = "Definición de parámetros para el Api Point")

									Column {

										MA_TextoEditable(valor = uiState.paramtrosSeleccionado.key,
														 titulo = "Key",
														 onValueChange = { viewModel.onEvento(DetalleEndPointVM.Eventos.ModificarClaveParametrosSeleccionado(it)) }


										)
										MA_TextoEditable(valor = uiState.paramtrosSeleccionado.valor, titulo = "Valor") {
											viewModel.onEvento(DetalleEndPointVM.Eventos.ModificarValorParametrosSeleccionado(it))
										}


									}


									Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
										MA_BotonSecundario("Cancelar") {
											scope.launch { sheetParametros.hide() }
										}

										MA_BotonPrincipal("Guardar") {
											viewModel.onEvento(DetalleEndPointVM.Eventos.GuardarParametrosSelecciconado)
											scope.launch { sheetParametros.hide() }
										}

									}


								}

							})
						})
}