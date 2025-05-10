package com.personal.requestmobility.core.composables.graficas

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.circle.CircleChart
import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.common.asSolidChartColor
import com.personal.requestmobility.core.composables.componentes.Marco

@Preview
@Composable
fun TestGraficoAnillos() {
 //   GraficoAnillos("Test", dameValoresTest())
}

/*
@Composable
fun GraficoAnillos(titulo: String = "", listaValores: List<ValoresGrafico>) {

    val c: List<CircleData> = listaValores.subList(0,3).map {
        CircleData(
            value = it.y * 10, color = it.color.asSolidChartColor(), label = it.leyenda
        )
    }
    val data = remember { { c } }

    Marco(titulo = titulo) {
        CircleChart(
            data = data,
            modifier = Modifier.size(300.dp),
            onCircleClick = { circleData ->
                println("Clicked on circle with data: $circleData")
            }
        )
    }
}*/


fun generateMockCircleData(): List<CircleData> {

    return listOf(
        CircleData(
            value = 80F, color = Color(0XFFfb0f5a).asSolidChartColor(), label = "Label 1"
        ),
        CircleData(
            value = 80F, color = Color(0XFFafff01).asSolidChartColor(), label = "Label 2"
        ),
        CircleData(
            value = 50F, color = Color(0XFF00C4D4).asSolidChartColor(), label = "Label 3"
        ),
        CircleData(
            value = 70F, color = Color(0xFFFFECB3).asSolidChartColor(), label = "Label 3"
        ),
    )
}