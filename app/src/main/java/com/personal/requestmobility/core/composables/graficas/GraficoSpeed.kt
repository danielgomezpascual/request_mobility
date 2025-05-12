package com.personal.requestmobility.core.composables.graficas

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshoe.charty.circle.SpeedometerProgressBar
import com.himanshoe.charty.circle.config.DotConfig
import com.himanshoe.charty.common.ChartColor
import com.himanshoe.charty.common.TextConfig
import com.himanshoe.charty.common.asSolidChartColor
import com.personal.requestmobility.core.composables.componentes.Marco
import com.personal.requestmobility.core.composables.tabla.Fila

/*
@Preview
@Composable
fun TestGraficoSpeed() {
    GraficoSpeed("Test", dameValoresTest())
}*/


@Composable
fun GraficoSpeed(titulo: String = "",
                 listaValores: List<Fila>,
                 posicionX: Int = 0 ,
                 posivionY: Int = 1,
) {
    Marco(titulo = titulo) {
        val v = listaValores.first()
        SpeedometerProgressBar(
            progress = { 0.50f },
            title = "SIN DESARROLLAR",
            modifier = Modifier.size(300.dp),
            titleTextConfig = TextConfig.default().copy(
                fontSize = 20.sp,
            ),
            subTitleTextConfig = TextConfig.default().copy(
                fontSize = 30.sp, textColor = ChartColor.Solid(Color.Black)
            ),
            trackColor = Color.Gray.copy(alpha = 0.2F).asSolidChartColor(),
            color = ChartColor.Gradient(
                listOf(
                    Color(0xFF7AD3FF), Color(0xFF4FBAF0)
                )
            ),
            dotConfig = DotConfig(
                fillDotColor = ChartColor.Gradient(
                    listOf(
                        Color(0xFF7AD3FF), Color(0xFF4FBAF0)
                    )
                ),
                trackDotColor = Color.Gray.copy(alpha = 0.2F).asSolidChartColor(),
            ),
            progressIndicatorColor = ChartColor.Solid(Color.White)
        )
    }
}
