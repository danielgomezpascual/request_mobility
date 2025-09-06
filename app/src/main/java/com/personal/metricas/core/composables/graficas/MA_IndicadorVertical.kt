package com.personal.metricas.core.composables.graficas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.personal.metricas.core.composables.tabla.Fila
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion

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

    }



    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        LazyColumn(
            modifier = Modifier.fillMaxHeight()            ,
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


                MA_Indicador(texto = x.toString(), valor = y.toString(), colorIndicador = fila.color,
                             colorFondo =  Color(panelConfiguracion.colorFondoIndicador))


            }
        }
    }



}
