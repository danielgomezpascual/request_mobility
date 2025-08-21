package com.personal.metricas.core.composables.graficas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.bar.SignalProgressBarChart
import com.himanshoe.charty.common.ChartColor
import com.himanshoe.charty.common.asSolidChartColor
import com.personal.metricas.App
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.tabla.Fila
import com.personal.metricas.core.utils.esNumerico
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion

@Composable
fun MA_SignalVertical(
	modifier: Modifier = Modifier,
	listaValores: List<Fila>,
	posicionX: Int = 0,
	posicionY: Int = 1,
	panelConfiguracion: PanelConfiguracion,
) {


	/*var maximo = -1f
	listaValores.forEach { f ->
		val v = f.celdas.get(posicionY).valor
		App.log.d("Valor $v")
		if (v.esNumerico()) {
			if (v.toFloat() > maximo) {
				maximo = v.toFloat()
			}
		}
	}*/
	var maximo = -1f
	if (panelConfiguracion.valorMaximo.equals("0")) {
		listaValores.forEach { f ->
			val v = f.celdas.get(posicionY).valor
			App.log.d("Valor $v")
			if (v.esNumerico()) {
				if (v.toFloat() > maximo) {
					maximo = v.toFloat()
				}
			}
		}
	}else{
		maximo = panelConfiguracion.valorMaximo.toFloat()
	}



	App.log.c("Maximo: $maximo")

	Column() {


		listaValores.map { fila ->

			val x = if (fila.celdas.size >= posicionX) fila.celdas[posicionX].valor else '-'
			var y: String = "0"
			try {
				y = if (fila.celdas.size >= posicionY) fila.celdas[posicionY].valor else "0"
			}
			catch (e: Exception) {
				e.printStackTrace()
			}







			Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
				LazyRow (
					modifier = Modifier.fillMaxHeight(),
					/*horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Center*/

				) {
					items(items = listaValores) { fila ->
						val x = if (fila.celdas.size >= posicionX) fila.celdas[posicionX].valor else '-'
						var y: String = "0"
						try {
							y = if (fila.celdas.size >= posicionY) fila.celdas[posicionY].valor else "0"
						}
						catch (e: Exception) {
							e.printStackTrace()
						}

						val texto = x.toString()
						val valor = y.toString()
						val color = fila.color

						Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
							.width(100.dp)
							.height(350.dp)

							.padding(10.dp)
						) {

							//	val valor = 800f
							//	val maximo = 1000f
							//	val texto = "Pruebnas"
							//val color = Color.Red

							Text(
								text = valor.toInt().toString(),
								modifier = Modifier.padding(6.dp),
								color = color,
								fontWeight = FontWeight(800),
								fontSize = TextUnit(25.0f, TextUnitType.Sp),
								textAlign = TextAlign.Center
							)


							SignalProgressBarChart(
								modifier = Modifier
									.weight(1f)
									.fillMaxSize(),
								progress = { valor.toFloat() },
								//progress = { 500f},
								maxProgress = maximo,
								totalBlocks = 10,
								progressColor = ChartColor.Gradient(
									listOf(
										//			Color(0xFFE8C900), Color(0xFFBA1515)
										color, Color.Black
									)
								),
								trackColor = Color.Gray.asSolidChartColor(),
								gapRatio = 0.15f
							)

							Text(
								text = texto,
								modifier = Modifier.padding(6.dp),
								color = color,
								fontWeight = FontWeight(800),
								fontSize = TextUnit(14.0f, TextUnitType.Sp),
								textAlign = TextAlign.Center
							)


						}


					}
				}
			}

		}
	}
}
