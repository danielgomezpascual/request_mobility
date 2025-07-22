package com.personal.metricas.core.composables.graficas

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.common.ChartColor
import com.himanshoe.charty.common.LabelConfig
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.InteractionTooltipConfig
import com.himanshoe.charty.line.config.LineChartColorConfig
import com.himanshoe.charty.line.config.LineChartConfig
import com.himanshoe.charty.line.config.LineChartGridConfig
import com.himanshoe.charty.line.config.LineConfig
import com.himanshoe.charty.line.model.LineData
import com.personal.metricas.core.composables.tabla.Fila
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion

/*
@Preview
@Composable
fun TestGraficoLineas() {
    GraficoLineas(Modifier, dameValoresTest(), 8f /*generateMockBarData(3, true, false)*/)
}*/


@Composable
fun MA_GraficoLineas(
	modifier: Modifier = Modifier,
	listaValores: List<Fila>,
	posicionX: Int = 0,
	posivionY: Int = 1,
	panelConfiguracion: PanelConfiguracion
) {


	val data = listaValores.map { fila ->
		LineData(
			xValue = fila.celdas[posicionX].valor,
			yValue = fila.celdas[posivionY].valor.toFloat(),
		)
	}
	LineChart(

		modifier = modifier
			.fillMaxSize()
			.padding(50.dp, 5.dp),

		target = null,
		colorConfig = LineChartColorConfig.default().copy(
			lineColor = ChartColor.Solid(Color(0xFF084CF8)),
			/*lineFillColor = ChartColor.Gradient(
				listOf(
					Color(0x4DFC0013), Color(0xFFFFFFFF)
				)
			)*/
		),
		data = {
			data
		},
		chartConfig = LineChartConfig(
			lineConfig = LineConfig(showValueOnLine = panelConfiguracion.mostrarEtiquetas,
									drawPointerCircle = true,
									lineCap = StrokeCap.Round),
			gridConfig = LineChartGridConfig().copy(),
			interactionTooltipConfig = InteractionTooltipConfig(
				isLongPressDragEnabled = true,
			),

			),
		onValueChange = {
			println("Value changed to $it")
		},
		labelConfig = LabelConfig.default().copy(
			showYLabel = true,
			showXLabel = true,
			xAxisCharCount = 15

		),

		showFilledArea = true,

		)

}
