package com.personal.metricas.organizaciones.ui.detalle

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HorizontalRule
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
import com.personal.metricas.App
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.checks.MA_CheckBoxNormal
import com.personal.metricas.core.composables.combo.MA_Combo
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.composables.labels.MA_Titulo2
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils.if3
import com.personal.metricas.inicializador.domain.ACTUA_SOBRE
import com.personal.metricas.inicializador.domain.PanelesGenericos
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.menu.Features
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.organizaciones.ui.composables.ParseTimesToSet
import com.personal.metricas.organizaciones.ui.composables.TimeSelector


import com.personal.metricas.organizaciones.ui.entidades.FORMA_SINCRONIZAR
import com.personal.metricas.organizaciones.ui.entidades.OrganizacionUI
import com.personal.metricas.paneles.domain.entidades.Alarmas
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.paneles.domain.entidades.PanelData
import com.personal.metricas.paneles.domain.entidades.PanelTipoGrafica
import com.personal.metricas.paneles.ui.componente.MA_Panel
import com.personal.metricas.paneles.ui.entidades.PanelUI
import org.koin.androidx.compose.koinViewModel
import java.time.LocalTime


@Composable
fun ScreenDetalleOrganizacion(
	identificador: String,
	viewModel: OrganizacionesDetalleVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	LaunchedEffect(Unit) {
		viewModel.onEvent(OrganizacionesDetalleVM.Eventos.Cargar(identificador))
	}
	val uiState by viewModel.uiState.collectAsState()

	when (uiState) {
		is OrganizacionesDetalleVM.UIState.Error   -> ErrorScreen((uiState as OrganizacionesDetalleVM.UIState.Error).mensaje)
		OrganizacionesDetalleVM.UIState.Loading    -> LoadingScreen()
		is OrganizacionesDetalleVM.UIState.Success -> {
			ScreenDetalleOrganizacionSincronizacionSuccess(viewModel, (uiState as OrganizacionesDetalleVM.UIState.Success), navegacion)
		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenDetalleOrganizacionSincronizacionSuccess(
	viewModel: OrganizacionesDetalleVM,
	uiState: OrganizacionesDetalleVM.UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {

	val organizacionUI: OrganizacionUI = uiState.organizacionUI


	MA_ScaffoldGenerico(
		tituloScreen = TituloScreen.Kpi,
		navegacion = navegacion,
		accionesSuperiores = {
			Row(modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.Top) {

				MA_IconBottom(icon = Features.Eliminar().icono, color = Features.Eliminar().color) { viewModel.onEvent(OrganizacionesDetalleVM.Eventos.Eliminar(navegacion)) }
				MA_IconBottom(icon = Features.Guardar().icono, color = Features.Guardar().color) { viewModel.onEvent(OrganizacionesDetalleVM.Eventos.Guardar(navegacion)) }
			}
		},
		contenido = {
			val scrollState = rememberScrollState() // 1. Recuerda el estado del scroll
			Column(modifier = Modifier.verticalScroll(scrollState)) {

				MA_Card {
					Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
						MA_Avatar(organizacionUI.organizationCode, color = Color(organizacionUI.color))
						MA_Titulo(organizacionUI.organizationName)
					}
				}

				MA_Titulo2("Forma sincronización")

				MA_Card {


					Column() {


						MA_CheckBoxNormal(valor = organizacionUI.activo, titulo = "Activar sincronizacióm") {
							viewModel.onEvent(OrganizacionesDetalleVM.Eventos.ActivarSincronizacion(it))
						}

						MA_Combo(icono = Icons.Filled.HorizontalRule,                    //modifier = Modifier.weight(1f),
								 titulo = "Forma Sincronizar",
								 descripcion = "Forma de establecer la sincronización de los elementos",
								 valorInicial = organizacionUI.formaSincronizar.toString(),
								 elementosSeleccionables = listOf<String>(FORMA_SINCRONIZAR.AUTO.toString(), FORMA_SINCRONIZAR.MANUAL.toString()),
								 onClickSeleccion = { str, indice ->
									 viewModel.onEvent(OrganizacionesDetalleVM.Eventos.OnChangeFormaSincronizar(str))

								 })
					}
				}


				MA_Titulo2("Horas Sincronziacion")
				val initialSelectionString = "00:30; 02:00; 09:00;10:30;18:00;19:30"

				var mySelectedHours by remember { mutableStateOf<Set<LocalTime>>(ParseTimesToSet(initialSelectionString)) }
				MA_Card {
					Column() {
						TimeSelector(
							// Le pasamos el Set actual para que se muestre correctamente
							initialSelection = mySelectedHours,
							// Recibimos el Set actualizado cada vez que el usuario hace click
							onSelectionChanged = { updatedHours ->
								mySelectedHours = updatedHours
								val horas = updatedHours.joinToString(separator = ";")
								App.log.d("Horas -> $horas")
							}
						)
					}
				}

				MA_Titulo2("Carga del servidor")


			/*	val kpiHorasTransacciones = KpiUI(
					titulo = "Transacciones por horas",
					descripcion = "Estimación de procesamiento de transacciones por horas (Trabajo de TRX real).",
					origen = "",
					sql = """
						SELECT
							STRFTIME('%H', CREATION_DATE) AS Hora,
							COUNT(MOB_REQUEST_ID) AS 'Trx'
						FROM
							TRANSACCIONES
						
						GROUP BY
							Hora
						ORDER BY
							1 ASC;
					""".trimIndent(),
					dinamico = true,
					parametros = Parametros()
				)

				val configuracion = PanelConfiguracion().copy(
					ajustarContenidoAncho = true,
					tipo = PanelTipoGrafica.BarrasFinasVerticales(),
					mostrarTabla = false,
					mostrarEtiquetas = true)

				//val kpi = if3(crearKPI, guardarKpi(kpiUI), kpiUI)
				val panel = PanelUI.Companion.crearPanelUI(kpiHorasTransacciones, configuracion)*/

				MA_Panel(panelData = PanelData.fromPanelUI(PanelesGenericos.PanelHoras(ACTUA_SOBRE.GENERAL), NotasManager(), Parametros()))
				MA_Panel(panelData = PanelData.fromPanelUI(PanelesGenericos.PanelHoras(ACTUA_SOBRE.ORGANIZACION, organizacionUI.organizationCode), NotasManager(), Parametros()))




				MA_Titulo2("Impacto de tranacciones")


				val kpiPorcentajeTrx = KpiUI(
					titulo = "Transacciones",
					descripcion = "Transacciones cargadas por la organizacion",
					origen = "",
					sql = """
						SELECT
  ORGANIZATION_CODE,
  COUNT(*) AS recuento,
  ROUND((COUNT(*) * 100.0 / SUM(COUNT(*)) OVER ()),2) AS porcentaje
FROM
  TRANSACCIONES
GROUP BY
  ORGANIZATION_CODE;;
					""".trimIndent(),
					dinamico = true,
					parametros = Parametros()
				)
				val condicionOrganizacion: Condiciones = Condiciones(1, Columnas("ORGANIZATION_CODE",
																				 posicion = 0,
																				 valores = emptyList()),
																	 condicionCelda = 0,
																	 color = 3,
																	 predicado = "== '${organizacionUI.organizationCode}'",
																	 descripion = "",
																	 alarma = Alarmas())



				val listaCondicionesErr = listOf<Condiciones>(condicionOrganizacion)
				val configuracionPorcentajeTrx = PanelConfiguracion().copy(
					ajustarContenidoAncho = true,
					tipo = PanelTipoGrafica.Circular(),
					colores = 3,
					mostrarEtiquetas = false, condiciones = listaCondicionesErr)





				//val kpi = if3(crearKPI, guardarKpi(kpiUI), kpiUI)
				val panelPorcentajeTrx = PanelUI.Companion.crearPanelUI(kpiPorcentajeTrx, configuracionPorcentajeTrx)

				MA_Panel(panelData = PanelData.fromPanelUI(panelPorcentajeTrx, NotasManager(), Parametros()))




				//Mostramos las horas que trabaja cada organizacin


			}


		}
	)
}



