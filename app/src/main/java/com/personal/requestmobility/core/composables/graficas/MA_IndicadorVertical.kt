package com.personal.requestmobility.core.composables.graficas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import com.personal.requestmobility.core.composables.labels.MA_Titulo
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion

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
fun MA_IndicadorVertical(
    modifier: Modifier = Modifier,
    listaValores: List<Fila>,
    posicionX: Int = 0,
    posicionY: Int = 1,
    panelConfiguracion: PanelConfiguracion
) {
    val data = listaValores.map { fila ->

        val x = if (fila.celdas.size >= posicionX) fila.celdas[posicionX].valor else '-'
        var y: String = "0"
        try {
            y = if (fila.celdas.size >= posicionY) fila.celdas[posicionY].valor else "0"
        } catch (e: Exception) {
            e.printStackTrace()
        }


        /* BarData(
             xValue = x,
             yValue = y,
             barColor = (fila.color).asSolidChartColor()
         )*/
    }
    /* val data = listaValores.map {
         BarData(
             xValue = it.x,
             yValue = it.y,
             barColor = (it.color).asSolidChartColor()

         )
     }*/

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
            ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        items(items = listaValores) { fila ->
            val x = if (fila.celdas.size >= posicionX) fila.celdas[posicionX].valor else '-'
            var y: String = "0"
            try {
                y = if (fila.celdas.size >= posicionY) fila.celdas[posicionY].valor else "0"
            } catch (e: Exception) {
                e.printStackTrace()
            }


            MA_Indicador( texto = x.toString(), valor = y.toString(), color = fila.color)
            MA_Indicador(modifier = modifier, texto = x.toString(), valor = y.toString(), color = fila.color)
           /* Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .defaultMinSize(100.dp, 100.dp)
                    .padding(5.dp)
                    .background(color = Color.Black)
            ) {

                Text(
                    text = y.toString(), modifier = modifier
                       // .fillMaxWidth()
                        .padding(6.dp),
                    color = fila.color,
                    // style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight(800),
                    fontSize = TextUnit(40.0f, TextUnitType.Sp),


                    textAlign = TextAlign.Center
                )


                //   MA_Titulo(valor = y.toString(), color = Color.Green)
                MA_Titulo(valor = x.toString(), color = Color.White)

            }*/
        }
    }


}
