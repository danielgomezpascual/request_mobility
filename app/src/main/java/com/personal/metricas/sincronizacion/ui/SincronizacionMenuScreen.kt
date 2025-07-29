package com.personal.metricas.sincronizacion.ui

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
import com.personal.metricas.menu.screen.HerramientasViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SincronizacionMenuScreen(
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


				MA_Card(
					modifier = Modifier
						.weight(1f)
						.fillMaxSize()
						.clickable(
							enabled = true,
							onClick = { navegacion(EventosNavegacion.Sincronizacion) })
				) {
					Row(modifier = Modifier
						.fillMaxWidth()
						.padding(15.dp),
						horizontalArrangement = Arrangement.Start,
						verticalAlignment = Alignment.CenterVertically) {
						//MA_Avatar(Features.Dashboard().texto)
						MA_ImagenDrawable(TituloScreen.Sincronizar.icono)
						MA_LabelNormal(
							modifier = Modifier.padding(2.dp),
							valor = Features.Sincronizar().texto
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

								navegacion(EventosNavegacion.MenuEndPoints)
							})
				) {
					Row(modifier = Modifier
						.fillMaxWidth()
						.padding(15.dp),
						horizontalArrangement = Arrangement.Start,
						verticalAlignment = Alignment.CenterVertically) {

						MA_ImagenDrawable(TituloScreen.EndPoints.icono)
						MA_LabelNormal(
							modifier = Modifier.padding(2.dp),
							valor = Features.EndPoints().texto
						)
					}
				}




			}


		}
	)

}