package com.personal.requestmobility.core.composables.graficas

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.himanshoe.charty.common.asSolidChartColor
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.model.PieChartData
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.utils.Utils
import com.personal.requestmobility.core.utils.esNumerico
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion

/*
@Preview
@Composable
fun TestGraficoCircular() {
    var listaValores: List<ElementoGrafica> = emptyList()

    val colors: List<Color> = listOf(
        Color.White,
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Black,
        Color.Yellow
    )
    (0..4).forEach {
        listaValores = listaValores.plus(
            ElementoGrafica(it.toFloat(), it.toFloat(), leyenda = "V $it", colors[it])
        )
    }

    GraficoCircular(
        modifier = Modifier
            .width(400.dp)
            .height(500.dp),

        listaValores = listaValores  /*generateMockBarData(3, true, false)*/
    )
}*/


@Composable
fun MA_GraficoAnillo(
	modifier: Modifier = Modifier,
	listaValores: List<Fila>,
	posicionX: Int = 0,
	posivionY: Int = 1,
	rellenoCentro: Boolean = false,
	panelConfiguracion: PanelConfiguracion,


	) {
	MA_BaseGraficoCircular(modifier = modifier, listaValores = listaValores, posicionX = posicionX, posicionY = posivionY, rellenoCentro = rellenoCentro, panelConfiguracion = panelConfiguracion)
}

@Composable
fun MA_GraficoCircular(
	modifier: Modifier = Modifier,
	listaValores: List<Fila>,
	posicionX: Int = 0,
	posivionY: Int = 1,
	rellenoCentro: Boolean = true,
	panelConfiguracion: PanelConfiguracion,
) {

	MA_BaseGraficoCircular(modifier = modifier, listaValores = listaValores, posicionX = posicionX, posicionY = posivionY, rellenoCentro = rellenoCentro, panelConfiguracion = panelConfiguracion)
}


@Composable
private fun MA_BaseGraficoCircular(
	modifier: Modifier = Modifier,
	listaValores: List<Fila>,
	posicionX: Int = 0,
	posicionY: Int = 1,
	rellenoCentro: Boolean = true,
	panelConfiguracion: PanelConfiguracion,
) {

	/*val data = listaValores.map {
		PieChartData(
			value = (it.y) as Float,
			color = (it.color).asSolidChartColor(),
			label = it.leyenda,
			labelColor = Color.Black.asSolidChartColor()
		)

	}*/
	val data = listaValores.map { fila ->


		PieChartData(value = if3(fila.celdas[posicionY].valor.toString().esNumerico(), fila.celdas[posicionY].valor.toFloat(), 0f),
					 color = (fila.color).asSolidChartColor(),
					 label = if3(panelConfiguracion.mostrarEtiquetas, fila.celdas[posicionX].valor, ""),
					 labelColor = Color.Black.asSolidChartColor()
		)
	}

	PieChart(
		onPieChartSliceClick = {
			println("Clicked on slice with data: $it")
		},
		isDonutChart = !rellenoCentro,
		data = { data },
		modifier = Modifier
			.fillMaxSize()
	)
}