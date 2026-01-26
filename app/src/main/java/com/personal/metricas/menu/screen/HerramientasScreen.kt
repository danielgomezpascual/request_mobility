package com.personal.metricas.menu.screen

import MA_IconBottom
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.App
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.labels.MA_Titulo2
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.menu.Features
import com.personal.metricas.start.composables.MA_PrimerosPasos
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScreenHerramientasInicial(
	navegacion: (EventosNavegacion) -> Unit,
) {
	HerramientasScreen(configuracionInicial = true, navegacion = navegacion)

}

@Composable
fun HerramientasScreen(
	configuracionInicial: Boolean = false,
	viewModel: HerramientasViewModel = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	MA_ScaffoldGenerico(
		mostrarBotonesSuperioresYBarraInferior = !configuracionInicial,
		tituloScreen = TituloScreen.Herramientas,
		navegacion = navegacion,
		accionesSuperiores = {

		},
		contenido = {
			Column(verticalArrangement = Arrangement.SpaceEvenly,
				   modifier = Modifier.fillMaxSize()) {

				if (!configuracionInicial) {
					MA_Titulo2("Planificador")
					Row() {
						MA_Card(
							modifier = Modifier
								.weight(1f)
								.fillMaxWidth()
								.clickable(
									enabled = true,
									onClick = {

										navegacion(EventosNavegacion.ListaOrganizaciones)
									})
						) {
							MA_IconBottom(icon = Features.Planificador().icono,
										  labelText = Features.Planificador().texto,
										  color = Features.Planificador().color) {
								navegacion(EventosNavegacion.ListaOrganizaciones)
							}


						}

						if (App.sharedPrerfences.get<Boolean>(
								Preferencias.ACCESO_SINCRONIZACION,
								true
							)
						) {
							MA_Card(
								modifier = Modifier
									.weight(1f)
									.fillMaxWidth()
									.clickable(
										enabled = true,
										onClick = {

											navegacion(EventosNavegacion.Sincronizacion)
										})
							) {
								MA_IconBottom(icon = Features.Sincronizar().icono,
											  labelText = Features.Sincronizar().texto,
											  color = Features.Sincronizar().color) {
									navegacion(EventosNavegacion.Sincronizacion)
								}


							}
							// Blue Theme for Sync
							/*ColoredNavItem(
								icon = Features.Sincronizar().icono,
								texto = Features.Sincronizar().texto,
								backgroundColor = Color(0xFFE3F2FD), // Light Blue
								iconColor = Color(0xFF1565C0), // Dark Blue
								onClick = { navegacion(EventosNavegacion.Sincronizacion) }
							)*/
						}
					}

					MA_Titulo2("Componentes")
					Row() {
						MA_Card(
							modifier = Modifier
								.weight(1f)
								.fillMaxWidth()
								.clickable(
									enabled = true,
									onClick = {

										navegacion(EventosNavegacion.MenuEndPoints)
									})
						) {
							MA_IconBottom(icon = Features.EndPoints().icono,
										  labelText = Features.EndPoints().texto,
										  color = Features.EndPoints().color) {
								navegacion(EventosNavegacion.MenuEndPoints)
							}


						}

						MA_Card(
							modifier = Modifier
								.weight(1f)
								.fillMaxWidth()
								.clickable(
									enabled = true,
									onClick = { navegacion(EventosNavegacion.MenuKpis) })
						) {
							MA_IconBottom(icon = Features.Kpi().icono,
										  labelText = Features.Kpi().texto,
										  color = Features.Kpi().color) {
								navegacion(EventosNavegacion.MenuKpis)
							}
						}
					}

					Row() {
						MA_Card(
							modifier = Modifier
								.weight(1f)
								.fillMaxWidth()
								.clickable(
									enabled = true,
									onClick = { navegacion(EventosNavegacion.MenuDashboard) })
						) {
							MA_IconBottom(icon = Features.Dashboard().icono,
										  labelText = Features.Dashboard().texto,
										  color = Features.Dashboard().color) {
								navegacion(EventosNavegacion.MenuDashboard)
							}
							/*Row(modifier = Modifier
							.fillMaxWidth()
							.padding(15.dp),
							horizontalArrangement = Arrangement.Start,
							verticalAlignment = Alignment.CenterVertically) {
							//MA_Avatar(Features.Dashboard().texto)
							MA_ImagenDrawable(TituloScreen.DashboardLista.icono)
							MA_LabelNormal(
								modifier = Modifier.padding(2.dp),
								valor = Features.Dashboard().texto
							)
						}*/
						}


						MA_Card(
							modifier = Modifier
								.weight(1f)
								.fillMaxWidth()
								.clickable(
									enabled = true,
									onClick = {

										navegacion(EventosNavegacion.MenuPaneles)
									})
						) {
							MA_IconBottom(icon = Features.Paneles().icono,
										  labelText = Features.Paneles().texto,
										  color = Features.Paneles().color) {
								navegacion(EventosNavegacion.MenuPaneles)
							}
							/*Row(modifier = Modifier
							.fillMaxWidth()
							.padding(15.dp),
							horizontalArrangement = Arrangement.Start,
							verticalAlignment = Alignment.CenterVertically) {

							MA_ImagenDrawable(TituloScreen.Paneles.icono)
							MA_LabelNormal(
								modifier = Modifier.padding(2.dp),
								valor = Features.Paneles().texto
							)
						}*/
						}

					}

					MA_Titulo2("Valores Predefinidos")
				}



				if (configuracionInicial) {
					Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
						.padding(50.dp)
						.fillMaxSize()) {
/*						MA_LabelNegrita("Primeros pasos")
						MA_Spacer()
						MA_Avatar(texto = "2", size = 70.dp, color = Color.Gray, fontSize = 40.sp,)
						MA_Spacer()
						MA_LabelNormal("Si quieres cargar unos paneles y dashboard por defecto con las consultas más habituales  pulsta el sigueinte botón", alineacion = TextAlign.Center)
						MA_Spacer()
*/

						MA_PrimerosPasos("Dashboard",
										 "2",
										 "Si quieres cargar unos paneles y dashboard por defecto con las consultas más habituales  pulsta el sigueinte botón")



						MA_Card {
							Column() {
								MA_IconBottom(icon = Features.InicializadorMetricas().icono,
											  labelText = Features.InicializadorMetricas().texto,
											  color = Features.InicializadorMetricas().color) {
									viewModel.onEvent(HerramientasViewModel.Eventos.InicializadorMetricas)
								}

							}
						}
						MA_Spacer()
						MA_BotonPrincipal("Continuar...") {
							navegacion(EventosNavegacion.ListaOrganizacionesStart)
						}
					}
				} else {
					MA_Card(
						modifier = Modifier
							//.weight(1f)
							.fillMaxWidth()
							.clickable(
								enabled = true,
								onClick = {
									viewModel.onEvent(HerramientasViewModel.Eventos.InicializadorMetricas)
								})
					) {

						Column {


							MA_IconBottom(icon = Features.InicializadorMetricas().icono,
										  labelText = Features.InicializadorMetricas().texto,
										  color = Features.InicializadorMetricas().color) {
								viewModel.onEvent(HerramientasViewModel.Eventos.InicializadorMetricas)
							}


						}


					}
				}


			}


		}
	)

}