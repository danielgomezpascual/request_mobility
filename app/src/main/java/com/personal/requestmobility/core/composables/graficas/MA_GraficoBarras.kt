package com.personal.requestmobility.core.composables.graficas

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.config.BarChartColorConfig
import com.himanshoe.charty.bar.config.BarChartConfig
import com.himanshoe.charty.bar.config.BarTooltip
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.LabelConfig
import com.himanshoe.charty.common.asSolidChartColor
import com.personal.requestmobility.core.composables.tabla.Fila

/*
@Preview
@Composable
fun TestGraficoBarras() {

    var listaValores: List<ElementoGrafica> = emptyList()
    (1..10).forEach {
        listaValores = listaValores.plus(
            ElementoGrafica(x = "Test ${it.toFloat()}",  y = it.toFloat(), leyenda = "Ley ${it}", color = Color.Red)
        )
    }


    GraficoBarras(Modifier,  listaValores)
}*/


@Composable
fun MA_GraficoBarras(
    modifier: Modifier = Modifier,
    listaValores: List<Fila>,
    posicionX: Int = 0,
    posicionY: Int = 1,
) {
    val data =  listaValores.map { fila ->

        val x =  if (fila.celdas.size >= posicionX) fila.celdas[posicionX].valor else '-'
        val y =  if (fila.celdas.size >= posicionY) fila.celdas[posicionY].valor.toFloat() else 0f

        BarData(
            xValue = x,
            yValue = y,
            barColor = (fila.color).asSolidChartColor()
        )
    }
   /* val data = listaValores.map {
        BarData(
            xValue = it.x,
            yValue = it.y,
            barColor = (it.color).asSolidChartColor()

        )
    }*/



    BarChart(
        modifier = Modifier
            .padding(10.dp)
            .width(500.dp)
            .height(200.dp),

        barTooltip = BarTooltip.GraphTop,
        labelConfig = LabelConfig.default().copy(
            labelTextStyle = TextStyle.Default.copy(fontSize = TextUnit(10f, TextUnitType.Sp)),
            showXLabel = true,
            xAxisCharCount = 10,
            showYLabel = true,
            textColor = Color(0xFFFF92C1).asSolidChartColor()
        ),
        barChartColorConfig = BarChartColorConfig.default().copy(
            fillBarColor = Color(0xFFFF92C1).asSolidChartColor(),
            negativeBarColors = Color(0xFF4D4D4D).asSolidChartColor()
        ),
        data = { data },
        barChartConfig = BarChartConfig.default().copy(
            minimumBarCount = 4,
            cornerRadius = CornerRadius(40F, 40F),
        ),
        onBarClick = { index, barData -> println("click in bar with $index index and data $barData") }
    )


}

fun dameValores(): List<BarData> = listOf<BarData>(
    BarData(yValue = 2f, xValue = "Test"),
    BarData(yValue = 105f, xValue = "Test"),
    BarData(yValue = 14f, xValue = "Test"),
    BarData(yValue = 100f, xValue = "Test"),
)
