package com.personal.requestmobility.core.composables.tabla

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.componentes.panel.PanelConfiguracion
import com.personal.requestmobility.core.composables.labels.Titulo
import com.personal.requestmobility.core.composables.listas.Lista
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.transacciones.ui.screens.composables.ModalInferiorFiltros


@Preview
@Composable
fun TestTablaDatos() {


    // TablaDatos(Modifier, "Test", dameValoresTestTabla())
}

/*fun dameValoresTestTabla(): ValoresTabla {
    val colors = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.DarkGray,
        Color.Magenta,
        Color.Cyan
    )


    var titulos: List<Header> = emptyList<Header>()
    val _titulos: List<String> = listOf("Campo 1 ", "Campo 2", "Campo 3")
    _titulos.forEach { titulos = titulos.plus(Header(it)) }

    var filas: List<Fila> = emptyList()
    (0..6).forEach { fila ->

        var vs = emptyList<String>()
        (1..5).forEach { columna ->
            when (columna) {
                2 -> vs = vs.plus("$columna")
                else -> vs = vs.plus("Data $fila.$columna")
            }

        }


        var fila = Fila(celdas = vs, color = colors[fila])
        //ValoresGrafico(it.toFloat(), it.toFloat(), leyenda = "V $it", colors[it])
        filas = filas.plus(fila)
    }

    return ValoresTabla(titulos, filas, indicadorColor = true, filasColor = false)
}*/

@Composable
fun TablaDatos(modifier: Modifier = Modifier,
               titulo: String = "",
               tabla: ValoresTabla) {
    /*
        Marco(modifier = modifier, titulo = titulo) {
            Tabla(modifier, tabla)
        }*/
}


@Composable
fun Tabla(modifier: Modifier = Modifier,
          panelConfiguracion: PanelConfiguracion,
          filas: List<Fila>,
          celdasFiltro: List<Celda>,
          mostrarTitulos: Boolean = true,
          onClickSeleccionarFiltro: (Celda) -> Unit,
          onClickInvertir: (Celda) -> Unit,
          onClickSeleccionarFila: (Fila) -> Unit) {

    val estadoScroll = rememberScrollState()
    val indicadorColor = panelConfiguracion.indicadorColor
    val filasColor = panelConfiguracion.filasColor
    val ajustarContenidoAncho = panelConfiguracion.ajustarContenidoAncho
    var modifierColumn = modifier
    if (!ajustarContenidoAncho) {
        modifierColumn = modifierColumn.horizontalScroll(estadoScroll)
    }




    Column(modifierColumn) {
        ModalInferiorFiltros() {
            Column {
                Titulo("Filtro")
                Lista(celdasFiltro) { celdaFiltro ->
                    CeldaFiltro(
                        celda = celdaFiltro,
                        onClickSeleccion = { cf -> onClickSeleccionarFiltro(cf) },
                        onClickInvertir = { cf -> onClickInvertir(cf) })
                }
            }


        }
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {


            if (mostrarTitulos && !filas.isEmpty()) {
                //todo: si no hay filas se jode el sistema, poner una pantalla oalgo de sin informacion
                filas.first().celdas.forEachIndexed { int, celda ->

                    var modifierBox: Modifier = Modifier
                    modifierBox = if (ajustarContenidoAncho) {
                        modifierBox
                            .fillMaxWidth()
                            .weight(1f)
                    } else {
                        modifierBox.width(celda.size)
                    }
                    Box(
                        modifier = modifierBox.background(Color.Gray)

                    ) {
                        celda.celdaTitulo(modifier)
                    }

                }
            }
        }


        Lista(filas) { fila ->
            FilaTablaDatos(fila, panelConfiguracion) { fila ->
                onClickSeleccionarFila(fila)
            }
        }


    }


}

@Composable
fun FilaTablaDatos(fila: Fila, configuracion: PanelConfiguracion, onClick: (Fila) -> Unit) {
    val modifier = Modifier
    val filasColor = configuracion.filasColor
    val ajustarContenidoAncho = configuracion.ajustarContenidoAncho
    val indicadorColor = configuracion.indicadorColor

    val color: Color = (fila.color).copy(alpha = 0.3f)
    val colorFondo = if3(filasColor, (fila.color).copy(alpha = 0.1f), Color.Transparent)


    Row(
        modifier = modifier
            .background(if3(fila.seleccionada, color, colorFondo))
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable {
                onClick(fila)
            },
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {


        fila.celdas.forEachIndexed { indice, celda ->

            var modifierFila: Modifier = Modifier

            if (ajustarContenidoAncho) {
                modifierFila = modifierFila
                    .fillMaxWidth()
                    .weight(1f)
            } else {
                modifierFila = modifierFila.width(fila.size)
            }

            if (indicadorColor && indice == 0) {
                Indicador(modifierFila, fila.color) {
                    celda.contenido(modifierFila)
                }
            } else {
                celda.contenido(modifierFila)
            }

        }

    }
    HorizontalDivider()

}

