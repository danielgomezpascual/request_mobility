package com.personal.requestmobility.core.composables.graficas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.bar.LineBarChart
import com.himanshoe.charty.bar.config.BarChartColorConfig
import com.himanshoe.charty.bar.config.BarChartConfig
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.LabelConfig
import com.himanshoe.charty.common.asSolidChartColor
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.componentes.GraTab.GraTabConfiguracion
import com.personal.requestmobility.core.composables.componentes.Marco

@Preview
@Composable
fun TestGraficoBarrasVerticales() {
    var listaValores: List<ElementoGrafica> = emptyList()

    val colors = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.DarkGray,
        Color.Magenta,
        Color.Cyan
    )
    (0..6).forEach {
        listaValores = listaValores.plus(
            ElementoGrafica(it.toString(), it.toFloat(), leyenda = "V $it", colors[it])
        )
    }

    GraficoBarrasVerticales(Modifier, listaValores  /*generateMockBarData(3, true, false)*/)
}


@Composable
fun GraficoBarrasVerticalesConMarco(
    modifier: Modifier = Modifier,
    titulo: String = "",
    listaValores: List<ElementoGrafica>,
    target: Float = 1f) {

    Marco(modifier = modifier, titulo = titulo) {
        GraficoBarrasVerticalesConMarco(modifier, titulo, listaValores, target)
    }

}


@Composable
fun GraficoBarrasVerticales(
    modifier: Modifier = Modifier,

    listaValores: List<ElementoGrafica>,
  ) {


    val data = listaValores.map {
        BarData(
            xValue = "${it.y}",
            yValue = it.y,
            barColor = (it.color).asSolidChartColor()

            //, label = it.leyenda
        )
    }


    LineBarChart(
        modifier = Modifier
            .padding(10.dp)
            .width(500.dp)
            .height(200.dp)
          ,
        data = { data },
        barChartColorConfig = BarChartColorConfig.default().copy(
            fillBarColor = Color(0xFFFF92C1).asSolidChartColor(),
            negativeBarColors = Color(0xFF4D4D4D).asSolidChartColor()
        ),
        barChartConfig = BarChartConfig.default().copy(
            showAxisLines = true,
            showGridLines = true,
            drawNegativeValueChart = true,
            showCurvedBar = true,
            minimumBarCount = data.size,
            cornerRadius = null
        ),
        labelConfig = LabelConfig.default().copy(
            textColor = Color.Black.asSolidChartColor(),
            showXLabel = true,
            xAxisCharCount = null,
            showYLabel = true
        ),


        onBarClick = { _, barData: BarData -> })


}
