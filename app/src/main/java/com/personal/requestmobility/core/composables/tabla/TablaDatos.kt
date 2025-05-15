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
    //tabla: ValoresTabla,
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


    //variable para controlar el estado de las filas que se estan presentado en la tabla
    /*   var filas by remember { mutableStateOf<List<Fila>>(tabla.filas) }
       //var filas = tabla.filas

       //variable para controlar la fila que se selecciono
       var filaSeleccionada by remember { mutableStateOf<Fila>(filas.first()) }

       //varaible para controlar el estadp de  las celdas y los atributos que se seleccionan
       var celdasFiltro by remember { mutableStateOf<List<Celda>>(emptyList()) }*/


    Column(modifierColumn) {
        //Text(text = filaSeleccionada.toString().substring(0, 150) + "...", fontSize = 10.sp, modifier = Modifier.background(Color.Yellow))

        ModalInferiorFiltros() {
            Column {
                Titulo("Filtro")
                Lista(celdasFiltro) { celdaFiltro ->
                    CeldaFiltro(celda = celdaFiltro,
                        onClickSeleccion = { cf -> onClickSeleccionarFiltro(cf) },
                        onClickInvertir = { cf -> onClickInvertir(cf) })
                }

                /*TextBuscador(searchText = viewModel.textoBuscar) { str ->
                    viewModel.onEvent(DockTransaccionesVM.Eventos.ModificarTextoBuscar(str))
                }*/

                /* Lista(data = uiState.filtrosTransaccion.filtros) { filtro ->
                     if (filtro is Filtro.Seleccion) {

                         ItemFiltroTransaccion(
                             filtro,
                             onClick = {
                                 viewModel.onEvent(DockTransaccionesVM.Eventos.ModicarSeleccionFiltro(filtro))
                             },
                             onLongClick = {
                                 viewModel.onEvent(DockTransaccionesVM.Eventos.InvertirSeleccionFiltro(filtro))
                             })
                     }
                 }*/

            }


        }


        /* Box() {
             Text(
                 modifier = Modifier.padding(start = 16.dp),
                 text = "TOTLA DE LA TABLA QUe sea configurable y aparezca abajo utlizando una celda 25 transacciones encontradas",
                 style = MaterialTheme.typography.bodySmall,
                 color = Color.Red,
                 textAlign = TextAlign.Start
             )
         }*/


        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {


            if (mostrarTitulos) {
        //todo: si no hay filas se jode el sistema, poner una pantalla oalgo de sin informacion
                filas.first().celdas.forEachIndexed { int, celda ->

                    var modifierBox: Modifier = Modifier
                    if (ajustarContenidoAncho) {
                        modifierBox = modifierBox
                            .fillMaxWidth()
                            .weight(1f)
                    } else {
                        modifierBox = modifierBox.width(celda.size)
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
            FilaTablaDatos(fila, indicadorColor, filasColor, ajustarContenidoAncho) { fila ->
                /*  filaSeleccionada = fila
                  celdasFiltro = fila.celdas*/
                onClickSeleccionarFila(fila)
            }
        }


    }


}

@Composable
fun FilaTablaDatos(fila: Fila, indicadorColor: Boolean, filasColor: Boolean, ajustarContenidoAncho: Boolean, onClick: (Fila) -> Unit) {
    val modifier = Modifier

    val color: Color = (fila.color).copy(alpha = 0.2f)

    Row(
        modifier = modifier
            .background(if3(fila.seleccionada, color, Color.Transparent))
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable {
                onClick(fila)
            },
        // .padding(4.dp),
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

