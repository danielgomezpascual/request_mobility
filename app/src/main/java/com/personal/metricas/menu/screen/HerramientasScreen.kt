package com.personal.metricas.menu.screen

import MA_IconBottom
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.R
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.imagenes.MA_ImagenDrawable
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.menu.Features
import org.koin.androidx.compose.koinViewModel

@Composable
fun HerramientasScreen(
	viewModel: HerramientasViewModel = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit) {

	MA_ScaffoldGenerico(
		titulo = "",
		tituloScreen = TituloScreen.Herramientas,
		volver = false,
		navegacion = { },
		contenidoBottomBar = {

			BottomAppBar() {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.Bottom

				) {

					MA_IconBottom(
						//   modifier = Modifier.weight(1f),
						icon = Features.Menu().icono,
						labelText = Features.Menu().texto,
						seleccionado = false,
						destacado = false,
						onClick = { navegacion(EventosNavegacion.CuadriculaDashboard) }
					)

					MA_IconBottom(
						//   modifier = Modifier.weight(1f),
						icon = Features.Menu().icono,
						labelText = Features.Menu().texto,
						seleccionado = false,
						destacado = false,
						onClick = { navegacion(EventosNavegacion.MenuApp) }
					)


				}


			}
		},
		contenido = {

			Column(verticalArrangement = Arrangement.SpaceEvenly,
				   modifier = Modifier.fillMaxSize()) {



				MA_Card(
					modifier = Modifier
						.weight(1f)
						.fillMaxSize()
						.clickable(
							enabled = true,
							onClick = {

								viewModel.onEvent(HerramientasViewModel.Eventos.InicializadorMetricas)
							})
				) {
					Row(modifier = Modifier
						.fillMaxWidth()
						.padding(15.dp),
						horizontalArrangement = Arrangement.Start,
						verticalAlignment = Alignment.CenterVertically) {

						MA_ImagenDrawable(R.drawable.caja4)
						MA_LabelNormal(
							modifier = Modifier.padding(2.dp),
							valor = Features.InicializadorMetricas().texto
						)
					}
				}




				MA_Card(
					modifier = Modifier
						.weight(1f)
                        .fillMaxSize()
                        .clickable(
                            enabled = true,
                            onClick = { navegacion(EventosNavegacion.MenuDashboard) })
				) {
					Row(modifier = Modifier
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
					}
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
					Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
						horizontalArrangement = Arrangement.Start,
						verticalAlignment = Alignment.CenterVertically) {

						MA_ImagenDrawable(TituloScreen.Paneles.icono)
						MA_LabelNormal(
							modifier = Modifier.padding(2.dp),
							valor = Features.Paneles().texto
						)
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
					Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
						horizontalArrangement = Arrangement.Start,
						verticalAlignment = Alignment.CenterVertically) {

						MA_ImagenDrawable(TituloScreen.Kpi.icono)
						MA_LabelNormal(
							modifier = Modifier.padding(2.dp),
							valor = Features.Kpi().texto
						)
					}
				}




			}


		}
	)

}