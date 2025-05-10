package com.personal.requestmobility.core.composables.graficas

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.common.ChartColor
import com.himanshoe.charty.common.LabelConfig
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.InteractionTooltipConfig
import com.himanshoe.charty.line.config.LineChartColorConfig
import com.himanshoe.charty.line.config.LineChartConfig
import com.himanshoe.charty.line.config.LineConfig
import com.himanshoe.charty.line.model.LineData

@Preview
@Composable
fun TestGraficoLineas() {
    GraficoLineas(Modifier, dameValoresTest(), 8f /*generateMockBarData(3, true, false)*/)
}


@Composable
fun GraficoLineas(modifier: Modifier = Modifier,
                  listaValores: List<ElementoGrafica>,
                  target: Float = 0f) {

    val data = listaValores.map {
        LineData(
            xValue = it.leyenda,
            yValue = it.y,

            )
    }


    LineChart(
        modifier = modifier
            .fillMaxSize()
            .padding(50.dp, 5.dp),

        target = target,
        colorConfig = LineChartColorConfig.default().copy(
            lineColor = ChartColor.Solid(Color(0xFF8D79F6)),
            lineFillColor = ChartColor.Gradient(
                listOf(
                    Color(0x4DB09FFF), Color(0xFFFFFFFF)
                )
            )
        ),
        data = {
            data
        },
        chartConfig = LineChartConfig(
            lineConfig = LineConfig(showValueOnLine = true, drawPointerCircle = true, lineCap = StrokeCap.Round),
            interactionTooltipConfig = InteractionTooltipConfig(
                isLongPressDragEnabled = true,
            ),

            ),
        onValueChange = {
            println("Value changed to $it")
        },
        labelConfig = LabelConfig.default().copy(
            showYLabel = true,
            showXLabel = true
        ),

        showFilledArea = true,

        )

}
