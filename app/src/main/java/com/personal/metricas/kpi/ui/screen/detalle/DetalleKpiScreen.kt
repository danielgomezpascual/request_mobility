package com.personal.metricas.kpi.ui.screen.detalle


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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.PushPin
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.checks.MA_CheckBoxNormal
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextoEditable
import com.personal.metricas.core.composables.edittext.MA_TextoNormal
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelLeyenda
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.composables.labels.MA_Titulo2
import com.personal.metricas.core.composables.modales.MA_BottomSheet
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.composables.tabla.MA_Tabla
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.core.utils.if3
import com.personal.metricas.kpi.ui.screen.detalle.DetalleKpiVM.UIState
import com.personal.metricas.menu.Features
import com.personal.metricas.transacciones.domain.entidades.ResultadoSQL
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun DetalleKpiScreen(
	identificador: Int,
	viewModel: DetalleKpiVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.onEvent(DetalleKpiVM.Eventos.Cargar(identificador))
	}


	when (uiState) {
		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).mensaje)
		UIState.Loading    -> LoadingScreen()
		is UIState.Success -> SuccessScreenDetalleKpi(
			viewModel,
			(uiState as UIState.Success),
			navegacion
		)

	}


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreenDetalleKpi(
	viewModel: DetalleKpiVM,
	uiState: UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {

	val kpiUI = uiState.kpiUI


	val sheetParametros = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	val scope = rememberCoroutineScope() // Se mantiene dentro del componente



	MA_ScaffoldGenerico(
		titulo = "",
		tituloScreen = TituloScreen.Kpi,
		navegacion = { },
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
						onClick = {
							navegacion(EventosNavegacion.MenuKpis)
						}
					)

					MA_IconBottom(
						modifier = Modifier.weight(1f),
						icon = Features.Paneles().icono,
						labelText = "Crear Panel",

						onClick = {
							viewModel.onEvent(DetalleKpiVM.Eventos.CrearPanel(navegacion))
							//navegacion(EventosNavegacion.MenuApp)
						}
					)

					MA_IconBottom(
						modifier = Modifier.weight(1f),
						icon = Features.Duplicar().icono,
						labelText = Features.Duplicar().texto,
						onClick = {
							viewModel.onEvent(DetalleKpiVM.Eventos.DuplicarKpi(navegacion))
							//navegacion(EventosNavegacion.MenuApp)
						}
					)


					Spacer(
						modifier = Modifier
							.fillMaxWidth()
							.weight(1f)
					)

					MA_IconBottom(
						modifier = Modifier.weight(1f),
						icon = Features.Eliminar().icono,
						labelText = Features.Eliminar().texto,
						color = Features.Eliminar().color,
						onClick = {
							viewModel.onEvent(DetalleKpiVM.Eventos.Eliminar(navegacion))
							// navegacion(EventosNavegacion.MenuKpis)
						}
					)

					MA_IconBottom(
						modifier = Modifier.weight(1f),
						icon = Features.Guardar().icono,
						labelText = Features.Guardar().texto,
						color = Features.Guardar().color,
						onClick = {
							viewModel.onEvent(DetalleKpiVM.Eventos.Guardar(navegacion))
							//  navegacion(EventosNavegacion.MenuKpis)
						}
					)


				}


			}


		},
		contenido = {
			val scrollState = rememberScrollState() // 1. Recuerda el estado del scroll
			Column(modifier = Modifier.verticalScroll(scrollState)) {


				Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
					MA_Avatar(kpiUI.titulo)
					MA_Titulo(kpiUI.titulo)
				}

				val visible: Boolean = false

				if (visible) {
					MA_TextoNormal(valor = kpiUI.id.toString(), titulo = "ID", onValueChange = { valor -> null })
				}

				MA_Titulo2("Definicion")
				MA_Card {

					Column() {


						MA_TextoNormal(valor = kpiUI.titulo, titulo = "Nombre", onValueChange = { valor ->
							viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeTitulo(valor))
						})

						MA_TextoNormal(valor = kpiUI.descripcion, titulo = "Descripcion", onValueChange = { valor ->
							viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeDescripcion(valor))
						})


						Box(modifier = Modifier.fillMaxWidth()){
							Column {
								MA_TextoNormal(valor = kpiUI.sql, titulo = "SQL", onValueChange = { valor ->
									viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeSQL(valor))
								})


								MA_BotonSecundario(texto = "RUN  SQL") {
									viewModel.onEvent(DetalleKpiVM.Eventos.RunSQL)
								}


								if (!uiState.infoSQL.isEmpty()){

									val color = if (uiState.estadoErrorSQL) Color.Red else Color.Black
									MA_LabelLeyenda(
										modifier = Modifier.fillMaxWidth(),
										alineacion = TextAlign.Center,
										valor = uiState.infoSQL,
										color = color)
								}
							}

						}


					}
				}

				MA_Titulo2("Parámetros")
				MA_IconBottom(icon = Icons.Default.PlusOne) {
					viewModel.onEvent(DetalleKpiVM.Eventos.NuevoParametro)
					scope.launch { sheetParametros.show() }
				}

				MA_Card {
					Column {
						kpiUI.parametros.ps.forEach { parametro ->
							Row(modifier = Modifier.clickable(enabled = true, onClick = {
								viewModel.onEvent(DetalleKpiVM.Eventos.SeleccionarParametro(parametro))
								scope.launch { sheetParametros.show() }

							})) {
								Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {


									MA_LabelNegrita(parametro.key)
									MA_Spacer()
									MA_LabelNormal(parametro.defecto)

									if (parametro.fijo) {
										Spacer(modifier = Modifier.padding(5.dp))
										MA_Icono(Icons.Default.PushPin, modifier = Modifier.size(15.dp))
									}

									Box(
										modifier = Modifier.clickable(onClick = {
											viewModel.onEvent(DetalleKpiVM.Eventos.EliminarParametro(parametro))

										})
									) {
										MA_Icono(Icons.Default.Delete, color = Color.Red)
									}


								}


							}

						}
					}
				}


				val filas = ResultadoSQL.fromSqlToTabla(kpiUI.sql, kpiUI.parametros).filas.take(10)
				MA_Titulo2("Resultado")
				MA_Card(Modifier.height(400.dp)) {
					Column() {
						MA_Tabla(filas = filas) { }
					}
				}
			}


			MA_BottomSheet(sheetParametros, onClose = {
				{ scope.launch { sheetParametros.hide() } }
			}, contenido = {
				Column {
					//	MA_BotonPrincipal("Cerrar") { scope.launch { sheetParametros.hide() } }
					MA_Titulo2(valor = "Definición de parámetros en el KPI")

					Column {

						MA_TextoEditable(valor = uiState.paramtrosSeleccionado.key,
										 titulo = "Key",
										 onValueChange = { viewModel.onEvent(DetalleKpiVM.Eventos.ModificarClaveParametrosSeleccionado(it)) }


						)

						/*MA_TextoEditable(valor = uiState.paramtrosSeleccionado.valor, titulo = "Valor") {
							viewModel.onEvent(DetalleKpiVM.Eventos.ModificarValorParametrosSeleccionado(it))
						}*/
						MA_TextoEditable(valor = uiState.paramtrosSeleccionado.defecto, titulo = "Valor Por Defecto") {
							viewModel.onEvent(DetalleKpiVM.Eventos.ModificarValorPorDefectoParametrosSeleccionado(it))
						}
						MA_CheckBoxNormal(valor = uiState.paramtrosSeleccionado.fijo, titulo = "Valor Fijo") {
							viewModel.onEvent(DetalleKpiVM.Eventos.ModificarValorFijoeParametrosSeleccionado(it))
						}

					}


					Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
						MA_BotonSecundario("Cancelar") {
							scope.launch { sheetParametros.hide() }
						}

						MA_BotonPrincipal("Guardar") {
							viewModel.onEvent(DetalleKpiVM.Eventos.GuardarParametrosSelecciconado)
							scope.launch { sheetParametros.hide() }
						}

					}


				}

			})

		}
	)


}
