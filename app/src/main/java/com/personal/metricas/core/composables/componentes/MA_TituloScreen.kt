package com.personal.metricas.core.composables.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.labels.MA_Titulo

@Composable
fun Cabecera(cabecera: TituloScreen, acciones: @Composable () -> Unit = {}) {
	MA_Card(elevacion = 0.dp, modifier = Modifier.padding(0.dp)) {
		Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
			/*Row(modifier = Modifier
				.padding(15.dp)
				.fillMaxWidth(), horizontalArrangement = Arrangement.Start,
				verticalAlignment = Alignment.CenterVertically) {*/


			/*Image(modifier = Modifier.height(80.dp),                            //modifier = Modifier.width(s),
				  painter = painterResource(id = cabecera.icono),
				  contentDescription = "Descripción de tu imagen", contentScale = ContentScale.Crop

			)*/
			Column (modifier = Modifier.padding(start = 5.dp, top = 45.dp)){
				Box() {
					MA_Titulo(
						alineacion = TextAlign.Start,
						valor = cabecera.titulo)
					//	MA_LabelEtiqueta(cabecera.descripcion, color = Color.Gray, modifier = Modifier.padding(horizontal = 7.dp))
					Box() {
						acciones()
					}

				}

			}

			//}

			HorizontalDivider(thickness = 1.dp)
		}


	}

}