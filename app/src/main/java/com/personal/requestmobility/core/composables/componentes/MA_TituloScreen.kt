package com.personal.requestmobility.core.composables.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.labels.MA_LabelEtiqueta
import com.personal.requestmobility.core.composables.labels.MA_Titulo

sealed class TituloScreen(val titulo: String, val descripcion: String, val icono: Int ){
	object Home: TituloScreen(titulo = "Home", descripcion = "Paneles inciales", icono = R.drawable.panel_top2)
	object Sincronizar: TituloScreen(titulo = "Sincronziacion", descripcion = "Obten los datos y mide sus resultados", icono = R.drawable.sincro_top)
	object Cuadriculas: TituloScreen(titulo = "Cuadriculas", descripcion = "Consulta los dashboards", icono = R.drawable.cuadricula)
	object DashboardDetalle: TituloScreen(titulo = "Dashboard", descripcion = "Define y establece paneles a consultar", icono = R.drawable.dashboard)
	object DashboardLista: TituloScreen(titulo = "Dashboard", descripcion = "Define y establece paneles a consultar", icono = R.drawable.dashboard)
	object Herramientas: TituloScreen(titulo = "Herramientas", descripcion = "Define componentes", icono = R.drawable.herramientas)
	object Paneles: TituloScreen(titulo = "Paneles", descripcion = "Define paneles", icono = R.drawable.paneles)
	object Kpi: TituloScreen(titulo = "Paneles", descripcion = "Define paneles", icono = R.drawable.kpi)
}

@Composable
fun Cabecera(cabecera: TituloScreen) {
	MA_Card(elevacion = 0.dp, modifier = Modifier.padding(0.dp)) {
		Column {
			Row(modifier = Modifier
				.padding(15.dp)
				.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {


				Image(modifier = Modifier.height(80.dp),                            //modifier = Modifier.width(s),
					  painter = painterResource(id = cabecera.icono),
					  contentDescription = "Descripción de tu imagen", contentScale = ContentScale.Crop

				)
				Column {
					MA_Titulo(alineacion = TextAlign.Start, valor = cabecera.titulo)
					MA_LabelEtiqueta(cabecera.descripcion, color = Color.Gray, modifier = Modifier.padding(horizontal = 7.dp))
				}

			}

			HorizontalDivider(thickness = 1.dp)
		}


	}

}