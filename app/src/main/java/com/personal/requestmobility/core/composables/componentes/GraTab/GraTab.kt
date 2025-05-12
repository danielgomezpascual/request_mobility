package com.personal.requestmobility.core.composables.componentes.GraTab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.componentes.Marco
import com.personal.requestmobility.core.composables.graficas.GraficoAnillo
import com.personal.requestmobility.core.composables.graficas.GraficoBarras
import com.personal.requestmobility.core.composables.graficas.GraficoBarrasVerticales
import com.personal.requestmobility.core.composables.graficas.GraficoCircular
import com.personal.requestmobility.core.composables.graficas.GraficoLineas
import com.personal.requestmobility.core.composables.graficas.ElementoGrafica
import com.personal.requestmobility.core.composables.graficas.ValoresGrafica
import com.personal.requestmobility.core.composables.tabla.Celda
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.Tabla

import com.personal.requestmobility.core.composables.tabla.ValoresTabla
import com.personal.requestmobility.core.composables.tabla.toValoresGrafica
import kotlin.collections.filter
import kotlin.collections.map


@Composable
fun GraTab(modifier: Modifier = Modifier,
           graTabData: GraTabData

) {
    //val graTabData by remember { mutableStateOf<GraTabData>(gr) }


    val configGrafica = graTabData.graTabConfiguracion

    graTabData.setupValores()

    //variable para controlar el estado de las filas que se estan presentado en la tabla
    var filas by remember { mutableStateOf<List<Fila>>(graTabData.valoresTabla.filas) }

    //varaible para controlar el estadp de  las celdas y los atributos que se seleccionan
    var celdasFiltro by remember { mutableStateOf<List<Celda>>(emptyList()) }


//--------------------------------------------------
    val filasPintar = filas.filter { it.visible == true } //solo pintamos las filas que estas visibles, el resto no.

    var tablaComposable: @Composable () -> Unit = {}
    var graficaComposable: @Composable () -> Unit = {}

    if (graTabData.graTabConfiguracion.mostrarGrafica) {
        val listaElementosMostrarGrafica: List<ElementoGrafica> = ValoresGrafica.fromValoresTabla(filas, graTabData.graTabConfiguracion.campoAgrupacionTabla, graTabData.graTabConfiguracion.campoSumaValorTabla).elementos
        graficaComposable = dameTipoGrafica(
            graTabConfiguracion = configGrafica,
            modifier = modifier,
            datos = listaElementosMostrarGrafica,
            filas = filasPintar
        )
    }

    if (graTabData.graTabConfiguracion.mostrarTabla) {
        tablaComposable = dameTipoTabla(
            graTabConfiguracion = configGrafica,
            modifier = modifier,
           // valoresTabla = valoresTabla,
            filas = filasPintar,
            celdasFiltro = celdasFiltro,

            onClickSeleccionarFila = { fila ->
                filas = filas.map { f -> f.copy(seleccionada = (f == fila)) }
                celdasFiltro = fila.celdas
            },
            onClickSeleccionarFiltro = { cf ->
                celdasFiltro = celdasFiltro.map { c ->
                    if (c.titulo.equals(cf.titulo)) {
                        cf.copy(seleccionada = !cf.seleccionada)
                    } else {
                        c
                    }
                }

                filas = filas.map { fila ->
                    var cumpleFiltro: Boolean = true
                    fila.celdas.forEach { celdaFila ->
                        celdasFiltro.filter { it.seleccionada }.forEach { celdaFiltro ->
                            if ((celdaFila.titulo.equals(celdaFiltro.titulo)) && !(celdaFila.valor.equals(celdaFiltro.valor))) {
                                cumpleFiltro = false
                            }
                        }
                    }
                    fila.copy(visible = cumpleFiltro)
                }


            }
        )
    }

    when (configGrafica.orientacion) {
        GraTabOrientacion.VERTICAL -> {
            GraficaConTablaVertical(
                modifier = modifier,
                graTabConfiguracion = configGrafica,
                grafica = { graficaComposable() },
                tabla = { tablaComposable() }
            )
        }

        GraTabOrientacion.HORIZONTAL -> {
            GraficaConTablaHorizontal(
                modifier = modifier,
                graTabConfiguracion = configGrafica,
                grafica = { graficaComposable() },
                tabla = { tablaComposable() }
            )
        }
    }

}

@Composable
fun GraficaConTablaHorizontal(modifier: Modifier = Modifier,
                              graTabConfiguracion: GraTabConfiguracion = GraTabConfiguracion(),
                              grafica: @Composable () -> Unit,
                              tabla: @Composable () -> Unit) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val m = Modifier
            .width(graTabConfiguracion.width)
            .height(graTabConfiguracion.height)

        Marco(titulo = graTabConfiguracion.titulo, modifier = m, componente = {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                if (graTabConfiguracion.mostrarGrafica) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(graTabConfiguracion.espacioGrafica)
                    ) {
                        grafica()
                    }
                }
                if (graTabConfiguracion.mostrarTabla) {
                    Box(
                        modifier = Modifier
                            .padding(graTabConfiguracion.paddingTablaHorizontal)
                            .fillMaxWidth(graTabConfiguracion.espacioTabla)
                    ) {
                        tabla()
                    }
                }
            }

        })
    }
}


@Composable
fun GraficaConTablaVertical(modifier: Modifier = Modifier,
                            graTabConfiguracion: GraTabConfiguracion = GraTabConfiguracion(),
                            grafica: @Composable () -> Unit,
                            tabla: @Composable () -> Unit) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val m = Modifier
            .width(graTabConfiguracion.width)
            .height(graTabConfiguracion.height)

        Marco(titulo = graTabConfiguracion.titulo, modifier = m, componente = {
            Column() {
                if (graTabConfiguracion.mostrarGrafica) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(graTabConfiguracion.espacioGrafica)
                    ) {
                        grafica()
                    }
                }
                if (graTabConfiguracion.mostrarTabla) {
                    Box(
                        modifier = Modifier
                            .padding(graTabConfiguracion.paddingTablaVertical)
                            .fillMaxSize()

                    ) {
                        tabla()
                    }
                }
            }

        })
    }
}

@Composable
fun dameTipoGrafica(graTabConfiguracion: GraTabConfiguracion,
                    modifier: Modifier,
                    datos: List<ElementoGrafica>,
                    filas: List<Fila>,
                    posicionX: Int = 0,
                    posivionY: Int = 1): @Composable () -> Unit {
    if (!graTabConfiguracion.mostrarGrafica) {
        return {}
    }
    var datosPintar = filas


    /*if (!graTabConfiguracion.mostrarEtiquetas) {
        datosPintar = filas.mapIndexed {index,


        }
    }*/

    return {
        when (graTabConfiguracion.tipo) {
            GraTabTipoGrafica.BARRAS_ANCHAS_VERTICALES -> {

                /*Box(Modifier
                    .width(500.dp)
                    .height(300.dp)) {*/
                GraficoBarras(
                    modifier = modifier,
                    //listaValores = datosPintar
                    listaValores =  datosPintar
                )
                //}
            }

            GraTabTipoGrafica.BARRAS_FINAS_VERTICALES -> {
                GraficoBarrasVerticales(
                    modifier = modifier,
                    //listaValores = datosPintar
                    listaValores =  datosPintar
                )
            }

            GraTabTipoGrafica.CIRCULAR -> {
                GraficoCircular(
                    modifier = modifier,
                    //listaValores = datosPintar
                    listaValores =  datosPintar
                )

            }

            GraTabTipoGrafica.ANILLO -> {

                GraficoAnillo(
                    modifier = modifier,
                    //listaValores = datosPintar
                    listaValores =  datosPintar
                )

            }

            GraTabTipoGrafica.LINEAS -> {
                GraficoLineas(
                    modifier = modifier,
                    //listaValores = datosPintar
                    listaValores =  datosPintar
                )
            }
        }
    }


}

@Composable
fun dameTipoTabla(graTabConfiguracion: GraTabConfiguracion,
                  modifier: Modifier,
                  //valoresTabla: ValoresTabla,
                  filas: List<Fila>,
                  celdasFiltro: List<Celda>,
                  onClickSeleccionarFiltro: (Celda) -> Unit,
                  onClickSeleccionarFila: (Fila) -> Unit): @Composable () -> Unit {

    if (graTabConfiguracion.mostrarTabla) {
        return {
            Tabla(
                modifier = Modifier.fillMaxSize(),
                graTabConfiguracion = graTabConfiguracion,
                //tabla = valoresTabla,
                filas = filas,
                celdasFiltro = celdasFiltro,
                mostrarTitulos = graTabConfiguracion.mostrarTituloTabla,
                onClickSeleccionarFiltro = onClickSeleccionarFiltro,
                onClickSeleccionarFila = onClickSeleccionarFila
            )
        }
    } else {
        return {}
    }
}
