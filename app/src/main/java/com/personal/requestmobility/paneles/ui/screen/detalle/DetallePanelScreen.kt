package com.personal.requestmobility.paneles.ui.screen.detalle


import MA_IconBottom
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TableView
import androidx.compose.material.icons.filled.Textsms
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
import com.personal.requestmobility.core.composables.botones.MA_BotonPrincipal
import com.personal.requestmobility.core.composables.botones.MA_BotonSecundario
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.checks.MA_SwitchNormal
import com.personal.requestmobility.core.composables.combo.MA_Combo
import com.personal.requestmobility.core.composables.combo.MA_ComboLista
import com.personal.requestmobility.core.composables.componentes.TituloScreen
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.imagenes.MA_Icono
import com.personal.requestmobility.core.composables.labels.MA_Titulo
import com.personal.requestmobility.core.composables.labels.MA_Titulo2
import com.personal.requestmobility.core.composables.layouts.MA_2ColumnasHorizontales
import com.personal.requestmobility.core.composables.modales.MA_BottomSheet
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.composables.tabla.Columnas
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.kpi.ui.composables.KpiComboItem
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.paneles.domain.entidades.Condiciones
import com.personal.requestmobility.paneles.domain.entidades.EsquemaColores
import com.personal.requestmobility.paneles.domain.entidades.PanelData
import com.personal.requestmobility.paneles.domain.entidades.PanelTipoGrafica
import com.personal.requestmobility.paneles.ui.componente.MA_ColumnaItemSeleccionable
import com.personal.requestmobility.paneles.ui.componente.MA_CondicionCeldaPanel
import com.personal.requestmobility.paneles.ui.componente.MA_CondicionCeldaPanelLista
import com.personal.requestmobility.paneles.ui.componente.MA_CondicionPanel
import com.personal.requestmobility.paneles.ui.componente.MA_CondicionPanelLista
import com.personal.requestmobility.paneles.ui.componente.MA_Panel
import com.personal.requestmobility.paneles.ui.componente.MA_SeleccionTipoGrafica
import com.personal.requestmobility.paneles.ui.componente.MA_SelectorEsquemaColores
import com.personal.requestmobility.paneles.ui.entidades.toPanel
import com.personal.requestmobility.paneles.ui.screen.detalle.DetallePanelVM.UIState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun DetallePanelScreen(
	identificador: Int, viewModel: DetallePanelVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
					  ) {
	
	val uiState by viewModel.uiState.collectAsState()
	
	LaunchedEffect(Unit) {
		viewModel.onEvent(DetallePanelVM.Eventos.Cargar(identificador))
	}
	
	
	when (uiState) {
		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).mensaje)
		UIState.Loading    -> LoadingScreen()
		is UIState.Success -> SuccessScreenDetalleKpi(viewModel,
													  uiState as UIState.Success,
													  navegacion)
		
	}
	
	
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreenDetalleKpi(
	viewModel: DetallePanelVM, uiState: UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
						   ) {
	
	val panelUI = uiState.panelUI
	val valoresTabla = uiState.valoresTabla
	
	
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	val sheetStateCondicionFila = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	val sheetStateCondicionCelda = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	
	val scope = rememberCoroutineScope() // Se mantiene dentro del componente
	
	
	
	
	MA_ScaffoldGenerico(titulo = "",
						tituloScreen = TituloScreen.Paneles,
						navegacion = { }, volver = false, contenidoBottomBar = {
		
		BottomAppBar() {
			Row(modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Start,
				verticalAlignment = Alignment.Bottom
			   
			   ) {
				
				MA_IconBottom(modifier = Modifier.weight(1f),
							  icon = Features.Menu().icono,
							  labelText = Features.Menu().texto,
							  onClick = {
								  navegacion(EventosNavegacion.MenuPaneles)
							  })
				
				MA_IconBottom(modifier = Modifier.weight(1f),
							  icon = Features.Previo().icono,
							  labelText = Features.Previo().texto,
							  onClick = {
								  
								  scope.launch { sheetState.show() }
							  })
				
				
				Spacer(modifier = Modifier
					.fillMaxWidth()
					.weight(1f))
				
				
				MA_IconBottom(modifier = Modifier.weight(1f),
							  icon = Features.Duplicar().icono,
							  labelText = Features.Duplicar().texto,
							  onClick = {
								  viewModel.onEvent(DetallePanelVM.Eventos.Duplicar(navegacion))
								  
							  })
				
				
				MA_IconBottom(modifier = Modifier.weight(1f),
							  icon = Features.Eliminar().icono,
							  labelText = Features.Eliminar().texto,
							  color = Features.Eliminar().color,
							  onClick = {
								  viewModel.onEvent(DetallePanelVM.Eventos.Eliminar(navegacion))
								  
							  })
				
				MA_IconBottom(modifier = Modifier.weight(1f),
							  icon = Features.Guardar().icono,
							  labelText = Features.Guardar().texto,
							  color = Features.Guardar().color,
							  onClick = {
								  viewModel.onEvent(DetallePanelVM.Eventos.Guardar(navegacion))
								  
							  })
				
				
			}
			
			
		}
	}, contenido = {
		Column(modifier = Modifier
			.fillMaxWidth()
			.verticalScroll(rememberScrollState())) {
			
			Column(modifier = Modifier.fillMaxWidth(),
				   horizontalAlignment = Alignment.CenterHorizontally) {
				MA_Avatar(panelUI.titulo)
				MA_Titulo(panelUI.titulo)
			}
			
			
			/*
							MA_TextoNormal(
								valor = panelUI.id.toString(), "ID",
								onValueChange = { valor -> null }
							)*/
			
			
			MA_Titulo2("Información")
			
			MA_Card(modifier = Modifier.fillMaxWidth()) {
				Column {
					MA_TextoNormal(valor = panelUI.titulo,
								   titulo = "Nombre",
								   onValueChange = { valor ->
									   viewModel.onEvent(DetallePanelVM.Eventos.OnChangeTitulo(valor))
								   })
					MA_TextoNormal(valor = panelUI.descripcion,
								   titulo = "Descripcion",
								   onValueChange = { valor ->
									   viewModel.onEvent(DetallePanelVM.Eventos.OnChangeDescripcion(valor))
								   })
					
				}
			}
			
			MA_Titulo2("KPI")
			MA_Card {
				Box(modifier = Modifier.height(100.dp)) {
					Row(verticalAlignment = Alignment.CenterVertically){
						MA_ComboLista<KpiUI>(modifier = Modifier.weight(1f), titulo = "",
											 descripcion = "Seleccione el KPI a enlazar",
											 valorInicial = {
												 KpiComboItem(kpiUI = panelUI.kpi)

											 },
											 elementosSeleccionables = uiState.kpiDisponibles,
											 item = { kpiUI ->
												 KpiComboItem(kpiUI = kpiUI)
											 },
											 onClickSeleccion = { kpiUI ->
												 viewModel.onEvent(DetallePanelVM.Eventos.OnChangeKpiSeleccionado(
													 kpiUI.id))
											 })

						Box( modifier = Modifier
							.clickable(enabled = true, onClick = {
								navegacion(EventosNavegacion.CargarKPI(panelUI.kpi.id)) })) {
							MA_Icono(Icons.Default.DoubleArrow, modifier = Modifier.size(16.dp))
						}

					}



				}


			}
			
			MA_Titulo2(valor = "Generales")
			
			MA_Card {
				MA_2ColumnasHorizontales(titulo = "", elementos = listOf(
						
						
						{
							MA_Combo(icono = Icons.Filled.HorizontalRule,                    //modifier = Modifier.weight(1f),
									 titulo = "Ancho",
									 descripcion = "Ancho a ocupar en (DP)",
									 valorInicial = (panelUI.configuracion.width.value.toInt()).toString(),
									 elementosSeleccionables = (200..1000 step 50).map { it.toString() },
									 onClickSeleccion = { str, indice ->
										 viewModel.onEvent(DetallePanelVM.Eventos.onChangeAncho(str))
									 })
						},
						
						{
							MA_Combo(icono = Icons.Filled.Height,                    // modifier = Modifier.weight(1f),
									 titulo = "Alto",
									 descripcion = "Alto a ocupar en (DP)",
									 valorInicial = (panelUI.configuracion.height.value.toInt()).toString(),
									 elementosSeleccionables = (200..1000 step 50).map { it.toString() },
									 onClickSeleccion = { str, indice ->
										 viewModel.onEvent(DetallePanelVM.Eventos.onChangeAlto(str))
									 })
						},
						
						{
							MA_ComboLista<EsquemaColores>(titulo = "Esquema de colores ",
														  descripcion = "Esquema de colores que se van a mostrar en la gráfica",
														  valorInicial = {
															  panelUI.configuracion.colores
															  MA_SelectorEsquemaColores(EsquemaColores().get(panelUI.configuracion.colores))
														  },
														  elementosSeleccionables = EsquemaColores().dameListasDisponibles(),
														  item = { esquema ->
															  MA_SelectorEsquemaColores(esquema)
														  },
														  onClickSeleccion = { columna ->
															  viewModel.onEvent(DetallePanelVM.Eventos.onChangeEsquemaColores(
																	  columna.id))
														  })
						},
						
						{
							MA_Combo(icono = Icons.Filled.Directions,
									 modifier = Modifier.weight(1f),
									 titulo = "Orientacion",
									 descripcion = "Seleccione  como quiere que se presenten la grafica y la tabla",
									 valorInicial = panelUI.configuracion.orientacion.name,
									 elementosSeleccionables = listOf("HORIZONTAL", "VERTICAL"),
									 onClickSeleccion = { str, indice ->
										 viewModel.onEvent(DetallePanelVM.Eventos.onChangeOrientacion(
												 str))
									 })
						}))
			}
			
			
			//Tabla
			MA_Titulo2("Tabla")
			
			

			
			
			
			MA_Card {
				MA_2ColumnasHorizontales(titulo = "", elementos = listOf(

					{
						MA_SwitchNormal(valor = panelUI.configuracion.mostrarTabla,
										titulo = "Tabla",
										icono = Icons.Filled.TableView,
										modifier = Modifier.weight(1f),
										onValueChange = { valor ->
											viewModel.onEvent(DetallePanelVM.Eventos.onChangeMosrtarTabla(valor))
										})
					},
					{

																			 MA_Combo(                                        //    modifier = Modifier.weight(1f),
																					 icono = Icons.Filled.TableView,
																					 titulo = "Espacio Tabla",
																					 descripcion = "Porcentaje del espaci que va  utilizar la gráfica en el panel",
																					 valorInicial = (panelUI.configuracion.espacioTabla * 100).toString(),
																					 elementosSeleccionables = (0..100 step 10).map { it.toString() },
																					 onClickSeleccion = { str, indice ->
																						 viewModel.onEvent(DetallePanelVM.Eventos.onChangeEspacioTabla(str))
																					 })
																		 },
																		 
																		 {
																			 MA_Combo(
																					 
																					 titulo = "Máximo",
																					 descripcion = "Límite de elementos que se pueden presentar en la lista",
																					 icono = Icons.Filled.FrontHand,
																					 valorInicial = panelUI.configuracion.limiteElementos.toString(),
																					 elementosSeleccionables = (0..10).map { it.toString() },
																					 onClickSeleccion = { str, indice ->
																						 viewModel.onEvent(DetallePanelVM.Eventos.onChangeLimiteElementos(
																								 str))
																					 })
																		 },
																		 
																		 {
																			 MA_SwitchNormal(titulo = "Agrupar  en 'Resto' ",
																							 valor = panelUI.configuracion.agruparResto,
																							 icono = Icons.Filled.ContentCut,
																							 onValueChange = { valor ->
																								 viewModel.onEvent(DetallePanelVM.Eventos.OnChangeAgruparResto(
																										 valor))
																							 })
																		 },
																		 
																		 
																		 {
																			 MA_SwitchNormal(titulo = "Ordenados",
																							 valor = panelUI.configuracion.ordenado,
																							 icono = Icons.Filled.ArrowDownward,
																							 onValueChange = { valor ->
																								 viewModel.onEvent(DetallePanelVM.Eventos.onChangeMostrarOrdenado(
																										 valor))
																							 })
																		 },
																		 
																		 {
																			 MA_SwitchNormal(titulo = "Todo el espacio",
																							 valor = panelUI.configuracion.ocuparTodoEspacio,
																							 icono = Icons.Default.SwapHoriz,
																							 onValueChange = { valor ->
																								 viewModel.onEvent(DetallePanelVM.Eventos.onChangeOcuparTodoEspacio(
																										 valor))
																							 })
																		 },
																		 
																		 {
																			 MA_SwitchNormal(titulo = "Título Tabla",
																							 icono = Icons.Default.FormatColorText,
																							 valor = panelUI.configuracion.mostrarTituloTabla,
																							 onValueChange = { valor ->
																								 viewModel.onEvent(DetallePanelVM.Eventos.onChangeMostrarTitulosTabla(
																										 valor))
																							 })
																		 },
																		 
																		 {
																			 MA_SwitchNormal(icono = Icons.Default.SpaceBar,
																							 titulo = "Ajustar Ancho",
																							 valor = panelUI.configuracion.ajustarContenidoAncho,
																							 onValueChange = { valor ->
																								 viewModel.onEvent(DetallePanelVM.Eventos.onChangeAjustarContenido(
																										 valor))
																							 })
																		 },
																		 
																		 {
																			 MA_SwitchNormal(titulo = "Indicador Color",
																							 icono = Icons.Default.ColorLens,
																							 valor = panelUI.configuracion.indicadorColor,
																							 onValueChange = { valor ->
																								 viewModel.onEvent(DetallePanelVM.Eventos.onChangeIndicadorColor(
																										 valor))
																							 })
																		 },
																		 
																		 {
																			 MA_SwitchNormal(icono = Icons.Default.FormatColorFill,
																							 titulo = "Filas de color",
																							 valor = panelUI.configuracion.filasColor,
																							 onValueChange = { valor ->
																								 viewModel.onEvent(DetallePanelVM.Eventos.onChangeFilasColor(
																										 valor))
																							 })
																		 }))
				
			}
			
			
			//Grafica
			MA_Titulo2("Grafica")

			MA_Card {
				
				
				MA_2ColumnasHorizontales(titulo = "", elementos = listOf(
					{
						MA_SwitchNormal(valor = panelUI.configuracion.mostrarGrafica,
										titulo = "Grafica",
										icono = Icons.Filled.AutoGraph,
										modifier = Modifier.weight(1f),
										onValueChange = { valor ->
											viewModel.onEvent(DetallePanelVM.Eventos.onChangeMostrarGrafica(valor))
										})
					},
						
						{
							
							
							MA_ComboLista<PanelTipoGrafica>(modifier = Modifier.weight(1f),
															titulo = "Tipo de Gráfica",
															descripcion = "Seleccione  el tipo de gráfica a utilizar",
															valorInicial = {
																
																App.log.d(panelUI.configuracion.tipo)
																
																MA_SeleccionTipoGrafica(panelUI.configuracion.tipo)
															},
															elementosSeleccionables = PanelTipoGrafica.dameTipos(),
															item = { tipo ->
																App.log.d(panelUI.configuracion.tipo)
																MA_SeleccionTipoGrafica(tipo)
															},
															onClickSeleccion = { tipo ->
																viewModel.onEvent(DetallePanelVM.Eventos.onChangeTipoGrafica(tipo))
															})
							
							
						},
						
						
						{
							MA_ComboLista<Columnas>(modifier = Modifier.weight(1f),
													titulo = "Eje X ",
													descripcion = "Campo  por el que se van a agrupar los valores cuando la tabla se encuentre limitada",
													valorInicial = {
														MA_ColumnaItemSeleccionable(valoresTabla.dameColumnaPosicion(
																panelUI.configuracion.columnaX))
													},
													elementosSeleccionables = valoresTabla.dameColumnas(),
													item = { columna -> MA_ColumnaItemSeleccionable(columna) },
													onClickSeleccion = { columna ->
														viewModel.onEvent(DetallePanelVM.Eventos.onChangeCampoAgrupacionTabla(
																columna.posicion.toString()))
													})
						},
						
						{
							MA_ComboLista<Columnas>(
									modifier = Modifier.weight(1f),
									titulo = "Eje Y ",
									descripcion = "Campo  por el que se van a agrupar los valores cuando la tabla se encuentre limitada",
									valorInicial = {
										MA_ColumnaItemSeleccionable(valoresTabla.dameColumnaPosicion(
												panelUI.configuracion.columnaY))
									},
									elementosSeleccionables = valoresTabla.dameColumnasNumericas(),
									item = { columna -> MA_ColumnaItemSeleccionable(columna) },
									onClickSeleccion = { columna ->
										viewModel.onEvent(DetallePanelVM.Eventos.onChangeCampoSumaTabla(
												columna.posicion.toString()))
									},
												   )
						},
						
						
						{
							MA_Combo(                    //  modifier = Modifier.weight(1f),
									icono = Icons.Filled.AutoGraph,
									titulo = "Espacio Gráfica",
									descripcion = "Porcentaje del espaci que va  utilizar la gráfica en el panel",
									valorInicial = (panelUI.configuracion.espacioGrafica * 100).toString(),
									elementosSeleccionables = (0..100 step 10).map { it.toString() },
									onClickSeleccion = { str, indice ->
										viewModel.onEvent(DetallePanelVM.Eventos.onChangeEspacioGrafica(
												str))
									})
						}))
			}
			
			MA_SwitchNormal(titulo = "Etiquetas",
							valor = panelUI.configuracion.mostrarEtiquetas,
							icono = Icons.Filled.Textsms,
							onValueChange = { valor ->
								viewModel.onEvent(DetallePanelVM.Eventos.onChangeMostrarEtiquetas(valor))
							})                //----------------------------------------------------------------------------------
			MA_Titulo2("Codiciones sobre la celda ${panelUI.configuracion.condicionesCeldas.size}")
			LazyRow(modifier = Modifier.height(300.dp),
					horizontalArrangement = Arrangement.spacedBy(4.dp)) {
				items(items = panelUI.configuracion.condicionesCeldas,
					  key = { item -> item.id }) { condicion ->
					MA_CondicionCeldaPanelLista(                            //columnas = valoresTabla.dameColumnas(),
							condicion = condicion, onClickAceptar = { condicionUI ->
						viewModel.onEvent(DetallePanelVM.Eventos.SeleccionarCondicionCelda(
								condicionUI))
						scope.launch { sheetStateCondicionCelda.show() }
					}, onClickCancelar = { condicionUI ->
						viewModel.onEvent(DetallePanelVM.Eventos.EliminarCondicionCelda(condicionUI))
					})
				}
			}
			
			MA_BotonSecundario(texto = "Nueva Condición Celda",
							   modifier = Modifier
								   .fillMaxWidth()
								   .padding(5.dp)) {
				viewModel.onEvent(DetallePanelVM.Eventos.AgregarCondicionCelda(Condiciones(id = 0,
																						   columna = Columnas(nombre = "", posicion = -1),
																						   color = 1,
																						   condicionCelda = 0,
																						   predicado = "")))
				scope.launch { sheetStateCondicionCelda.show() }
			}
			
			
			//----------------------------------------------------------------------------------
			MA_Titulo2("Codiciones sobre las filas ${panelUI.configuracion.condiciones.size}")
			
			//LazyColumn(
			LazyRow(modifier = Modifier.height(200.dp),                    // verticalArrangement = Arrangement.spacedBy(4.dp)
					horizontalArrangement = Arrangement.Center) {
				items(items = panelUI.configuracion.condiciones,
					  key = { item -> item.id }) { condicion ->
					
					MA_CondicionPanelLista(esquemaColores = EsquemaColores().dameEsquemaCondiciones(),
										   condicion = condicion,
										   onClickAceptar = { condicionUI ->
											   viewModel.onEvent(DetallePanelVM.Eventos.SeleccionarCondicion(
													   condicionUI))
											   scope.launch { sheetStateCondicionFila.show() }
										   },
										   onClickCancelar = { condicionUI ->
											   viewModel.onEvent(DetallePanelVM.Eventos.EliminarCondicion(
													   condicionUI))
										   })
				}
			}
			
			
			MA_BotonSecundario(texto = "Nueva Condición",
							   modifier = Modifier
								   .fillMaxWidth()
								   .padding(5.dp)) {
				viewModel.onEvent(DetallePanelVM.Eventos.AgregarCondicion(Condiciones(0,
																					  columna = Columnas(nombre = "", posicion = 1, valores = emptyList()),
																					  color = 1,
																					  condicionCelda = 0,
																					  predicado = "")))
				scope.launch { sheetStateCondicionFila.show() }
			}                //----------------------------------------------------------------------------------
			
			
		}
		
		
		if (uiState.condicionFila != null) {
			MA_BottomSheet(sheetStateCondicionFila, onClose = {
				{ scope.launch { sheetStateCondicionFila.hide() } }
			}, contenido = {
				Column {
					
					MA_CondicionPanel(columnas = valoresTabla.dameColumnas(),
									  esquemaColores = EsquemaColores().dameEsquemaCondiciones(),
									  condicion = uiState.condicionFila,
									  onClickAceptar = { condicionUI ->
										  App.log.d("Condicion - > ${condicionUI.id}  - ${condicionUI.color} - ${condicionUI.predicado}")
										  viewModel.onEvent(DetallePanelVM.Eventos.GuardarCondicion(
												  condicionUI))
										  scope.launch { sheetStateCondicionFila.hide() }
									  },
									  onClickCancelar = { condicionUI ->
										  App.log.d("${condicionUI.id}  - ${condicionUI.color} - ${condicionUI.predicado}")                                    //condiciones = condiciones.filterNot { it.id != condicionUI.id }
										  // panelUI.configuracion.condiciones = condiciones
										  // viewModel.onEvent(DetallePanelVM.Eventos.EliminarCondicion(condicionUI))
										  scope.launch { sheetStateCondicionFila.hide() }
									  })
					
					
				}
				
			})
		}
		
		
		if (uiState.condicionCelda != null) {
			MA_BottomSheet(sheetStateCondicionCelda, onClose = {
				{ scope.launch { sheetStateCondicionCelda.hide() } }
			}, contenido = {
				Column {
					
					MA_CondicionCeldaPanel(columnas = valoresTabla.dameColumnas(),
										   condicion = uiState.condicionCelda,
										   onClickAceptar = { condicionUI ->
											   viewModel.onEvent(DetallePanelVM.Eventos.GuardarCondicionCelda(
													   condicionUI))
											   scope.launch { sheetStateCondicionCelda.hide() }
										   },
										   onClickCancelar = { condicionUI ->                                /*viewModel.onEvent(DetallePanelVM.Eventos.EliminarCondicionCelda(
										condicionUI))*/
											   scope.launch { sheetStateCondicionCelda.hide() }
											   
										   })
					
					
				}
				
			})
		}
		
		
		
		MA_BottomSheet(sheetState, onClose = {
			{ scope.launch { sheetState.hide() } }
		}, contenido = {
			Column {
				MA_BotonPrincipal("Cerrar") { scope.launch { sheetState.hide() } }
				
				
				MA_Panel(
						panelData = PanelData(
								panel = panelUI.toPanel(),
								panelConfiguracion = panelUI.configuracion,
											  valoresTabla = uiState.valoresTabla))
				
			}
			
		})
		
	})
	
}


