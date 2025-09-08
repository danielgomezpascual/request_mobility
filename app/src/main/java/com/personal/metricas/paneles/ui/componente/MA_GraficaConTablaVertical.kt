package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.componentes.MA_Marco
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.paneles.domain.entidades.Alarmas
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion

@Composable
fun MA_GraficaConTablaVertical(
	modifier: Modifier = Modifier.Companion,
	panelConfiguracion: PanelConfiguracion = PanelConfiguracion(),
	grafica: @Composable () -> Unit,
	tabla: @Composable () -> Unit,
	alarmas: List<Alarmas> = emptyList<Alarmas>()
) {
	Column(
		modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.Companion.CenterHorizontally
	) {




		val m = Modifier.Companion
			//.width(Dp(panelConfiguracion.width.toFloat()))
			.fillMaxWidth()
			.height(Dp(panelConfiguracion.height.toFloat()))

		MA_Marco(titulo = panelConfiguracion.titulo, modifier = m, componente = {

			Column(
				modifier = Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally,
				   verticalArrangement = Arrangement.Center) {

				MA_LabelMini(panelConfiguracion.descripcion)


				Column {
					alarmas.forEach { alarma ->
						val colorAlarma = EsquemaColores().dameEsquemaCondiciones().colores.get(alarma.color)
						Row(modifier = Modifier.fillMaxWidth().padding(2.dp), horizontalArrangement = Arrangement.Center){
							MA_Icono(icono = Icons.Default.Notifications, color = colorAlarma)
							MA_LabelNormal(modifier = Modifier.padding(3.dp) ,valor = alarma.texto, color = colorAlarma)
						}

					}
				}

				if (panelConfiguracion.mostrarGrafica) {
					Box(
						modifier = Modifier.Companion
							.fillMaxWidth()
							.fillMaxHeight(panelConfiguracion.espacioGrafica.toFloat() / 100)
					) {
						grafica()
					}
				}
				if (panelConfiguracion.mostrarTabla) {

					var modifier: Modifier = Modifier.Companion
					modifier = if (panelConfiguracion.ocuparTodoEspacio) {
						modifier.padding(PaddingValues(0.dp, 0.dp))
					} else {
						modifier.padding(PaddingValues(60.dp, 15.dp))
					}

					Box(

						modifier = modifier
							.fillMaxSize()

					) {
						tabla()
					}
				}
			}

		})
	}
}