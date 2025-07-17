package com.personal.requestmobility.dashboards.ui.screen.cuadricula

import MA_IconBottom
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.componentes.TituloScreen
import com.personal.requestmobility.core.composables.edittext.MA_TextBuscador
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.imagenes.MA_Icono
import com.personal.requestmobility.core.composables.labels.MA_LabelMini
import com.personal.requestmobility.core.composables.labels.MA_LabelNegrita
import com.personal.requestmobility.core.composables.layouts.MA_Columnas
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.core.utils._toJson
import com.personal.requestmobility.dashboards.ui.screen.listado.DashboardListadoVM
import com.personal.requestmobility.menu.Features
import org.koin.androidx.compose.koinViewModel

@Composable
fun CuadriculDashboardUI(
	viewModel: CuadriculaDashboardVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
						) {
	val uiState by viewModel.uiState.collectAsState()
	
	LaunchedEffect(Unit) {
		viewModel.onEvento(CuadriculaDashboardVM.Eventos.Cargar)
	}
	
	when (val state = uiState) { // Renombrado uiState a state para claridad en el when
		is CuadriculaDashboardVM.UIState.Error   -> ErrorScreen(state.mensaje) // Asume ErrorScreen(mensaje: String)
		is CuadriculaDashboardVM.UIState.Loading -> LoadingScreen(state.mensaje) // Asume LoadingScreen(mensaje: String)
		is CuadriculaDashboardVM.UIState.Success -> SuccessCuadriculaDashboard( // Nombre corregido
				viewModel = viewModel,
				uiState = state,
				navegacion = navegacion
																			  )
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessCuadriculaDashboard(
	viewModel: CuadriculaDashboardVM,
	uiState: CuadriculaDashboardVM.UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
							  ) {
	
	
	MA_ScaffoldGenerico(

			titulo = "C-Dashboards", // Título adaptado
			tituloScreen = TituloScreen.DashboardLista,
			contenidoBottomBar = {
				BottomAppBar {
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
								icon = Features.Dashboard().icono,
								labelText = Features.Dashboard().texto,
								color = Features.Nuevo().color,
								onClick = { navegacion(EventosNavegacion.NuevoDashboard) }
									 )
						
						
					}
					
					
				}
			},
			contenido = {
				Column(
						modifier = Modifier
							.fillMaxWidth() // fillMaxWidth para la columna principal
							.padding(16.dp) // Padding general del contenido como en el ejemplo
					  ) {



					MA_TextBuscador(
						searchText = uiState.textoBuscar,
						onSearchTextChanged = { texto -> // Parámetro renombrado a 'texto'
							viewModel.onEvento(CuadriculaDashboardVM.Eventos.Buscar(texto))
						}
					)


					MA_Columnas(data = uiState.lista) { item ->
						
						MA_Card(
								modifier = Modifier.padding(2.dp).clickable(
										enabled = true,
										onClick = {
											navegacion(EventosNavegacion.VisualizadorDashboard(item.id, _toJson(item.parametros)))
										})
							   ) {
							
							Column(modifier = Modifier.padding(6.dp), verticalArrangement = Arrangement.Center,
								   horizontalAlignment = Alignment.CenterHorizontally) {

								MA_Avatar(item.nombre)
								
								
								Row (verticalAlignment = Alignment.CenterVertically){
									if (item.home) MA_Icono(Icons.Default.Stars, Modifier.size(16.dp))
									MA_LabelNegrita(
											modifier = Modifier.padding(2.dp),
											valor = item.nombre
												  )
								}
								
								MA_LabelMini(
										modifier = Modifier.padding(2.dp),
										valor = item.descripcion
											)
								
								MA_LabelMini(
										modifier = Modifier.fillMaxWidth(),
										valor = "${item.listaPaneles.filter { it.seleccionado }.size} paneles",
										alineacion = TextAlign.End,
										color = MaterialTheme.colorScheme.primary)
							}
						}
						
					}
					
					
				}
				
			})
}