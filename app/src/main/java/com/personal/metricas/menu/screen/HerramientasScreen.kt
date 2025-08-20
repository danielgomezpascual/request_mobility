package com.personal.metricas.menu.screen

import MA_IconBottom
import androidx.compose.animation.core.FastOutSlowInEasing
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
import com.personal.metricas.core.composables.labels.MA_Titulo2
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.menu.Features
import org.koin.androidx.compose.koinViewModel

@Composable
fun HerramientasScreen(
	viewModel: HerramientasViewModel = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	MA_ScaffoldGenerico(

		tituloScreen = TituloScreen.Herramientas,
		navegacion = navegacion,
		accionesSuperiores = {

		},
		contenido = {
			Column(verticalArrangement = Arrangement.SpaceEvenly,
				   modifier = Modifier.fillMaxSize()) {



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

					MA_IconBottom(icon = Features.InicializadorMetricas().icono,
								  labelText = Features.InicializadorMetricas().texto,
								  color = Features.InicializadorMetricas().color) {
						viewModel.onEvent(HerramientasViewModel.Eventos.InicializadorMetricas)
					}

				}
			}


		}
	)

}