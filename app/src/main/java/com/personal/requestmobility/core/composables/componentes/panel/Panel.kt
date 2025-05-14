package com.personal.requestmobility.core.composables.componentes.panel

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
import com.personal.requestmobility.core.composables.componentes.Marco
import com.personal.requestmobility.core.composables.graficas.GraficoAnillo
import com.personal.requestmobility.core.composables.graficas.GraficoBarras
import com.personal.requestmobility.core.composables.graficas.GraficoBarrasVerticales
import com.personal.requestmobility.core.composables.graficas.GraficoCircular
import com.personal.requestmobility.core.composables.graficas.GraficoLineas
import com.personal.requestmobility.core.composables.tabla.Celda
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.Tabla

import kotlin.collections.filter
import kotlin.collections.map


@Composable
fun GraTab(modifier: Modifier = Modifier,
           panelData: PanelData

) {
    //val graTabData by remember { mutableStateOf<GraTabData>(gr) }


    val configGrafica = panelData.panelConfiguracion

    panelData.setupValores()

    //variable para controlar el estado de las filas que se estan presentado en la tabla
    var filas by remember { mutableStateOf<List<Fila>>(panelData.valoresTabla.filas) }

    //varaible para controlar el estadp de  las celdas y los atributos que se seleccionan
    var celdasFiltro by remember { mutableStateOf<List<Celda>>(emptyList()) }


//--------------------------------------------------
    val filasPintar = filas.filter { it.visible == true } //solo pintamos las filas que estas visibles, el resto no.

    var tablaComposable: @Composable () -> Unit = {}
    var graficaComposable: @Composable () -> Unit = {}



        graficaComposable = dameTipoGrafica(
            panelConfiguracion = configGrafica,
            modifier = modifier,
            filas = filasPintar
        )



        tablaComposable = dameTipoTabla(
            panelConfiguracion = configGrafica,
            modifier = modifier,

            filas = filasPintar,
            celdasFiltro = celdasFiltro,

            onClickSeleccionarFila = { fila ->
                filas = filas.map { f -> f.copy(seleccionada = (f == fila)) }
                celdasFiltro = fila.celdas
            },
            onClickInvertir = {cfi->
                celdasFiltro = celdasFiltro.map { c ->
                    if (c.titulo.equals(cfi.titulo)) {
                        cfi.copy(filtroInvertido = !cfi.filtroInvertido)
                    } else {
                        c
                    }
                }

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


    when (configGrafica.orientacion) {
        PanelOrientacion.VERTICAL -> {
            GraficaConTablaVertical(
                modifier = modifier,
                panelConfiguracion = configGrafica,
                grafica = { graficaComposable() },
                tabla = { tablaComposable() }
            )
        }

        PanelOrientacion.HORIZONTAL -> {
            GraficaConTablaHorizontal(
                modifier = modifier,
                panelConfiguracion = configGrafica,
                grafica = { graficaComposable() },
                tabla = { tablaComposable() }
            )
        }
    }

}

@Composable
fun GraficaConTablaHorizontal(modifier: Modifier = Modifier,
                              panelConfiguracion: PanelConfiguracion = PanelConfiguracion(),
                              grafica: @Composable () -> Unit,
                              tabla: @Composable () -> Unit) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val m = Modifier
            .width(panelConfiguracion.width)
            .height(panelConfiguracion.height)

        Marco(titulo = panelConfiguracion.titulo, modifier = m, componente = {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                if (panelConfiguracion.mostrarGrafica) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(panelConfiguracion.espacioGrafica)
                    ) {
                        grafica()
                    }
                }
                if (panelConfiguracion.mostrarTabla) {
                    Box(
                        modifier = Modifier
                            .padding(panelConfiguracion.paddingTablaHorizontal)
                            .fillMaxWidth(panelConfiguracion.espacioTabla)
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
                            panelConfiguracion: PanelConfiguracion = PanelConfiguracion(),
                            grafica: @Composable () -> Unit,
                            tabla: @Composable () -> Unit) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val m = Modifier
            .width(panelConfiguracion.width)
            .height(panelConfiguracion.height)

        Marco(titulo = panelConfiguracion.titulo, modifier = m, componente = {
            Column() {
                if (panelConfiguracion.mostrarGrafica) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(panelConfiguracion.espacioGrafica)
                    ) {
                        grafica()
                    }
                }
                if (panelConfiguracion.mostrarTabla) {
                    Box(
                        modifier = Modifier
                            .padding(panelConfiguracion.paddingTablaVertical)
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
fun dameTipoGrafica(panelConfiguracion: PanelConfiguracion,
                    modifier: Modifier,
                    filas: List<Fila>,
                    posicionX: Int = 0,
                    posivionY: Int = 1): @Composable () -> Unit {
    if (!panelConfiguracion.mostrarGrafica) {
        return {}
    }
    var datosPintar = filas


    /*if (!graTabConfiguracion.mostrarEtiquetas) {
        datosPintar = filas.mapIndexed {index,


        }
    }*/

    return {
        when (panelConfiguracion.tipo) {
            PanelTipoGrafica.BARRAS_ANCHAS_VERTICALES -> {

                GraficoBarras(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX =posicionX,
                    posivionY = posivionY
                )

            }

            PanelTipoGrafica.BARRAS_FINAS_VERTICALES -> {
                GraficoBarrasVerticales(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX =posicionX,
                    posivionY = posivionY
                )
            }

            PanelTipoGrafica.CIRCULAR -> {
                GraficoCircular(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX =posicionX,
                    posivionY = posivionY
                )
            }

            PanelTipoGrafica.ANILLO -> {
                GraficoAnillo(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX =posicionX,
                    posivionY = posivionY
                )
            }

            PanelTipoGrafica.LINEAS -> {
                GraficoLineas(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX =posicionX,
                    posivionY = posivionY
                )
            }
        }
    }


}

@Composable
fun dameTipoTabla(panelConfiguracion: PanelConfiguracion,
                  modifier: Modifier,
                  filas: List<Fila>,
                  celdasFiltro: List<Celda>,
                  onClickSeleccionarFiltro: (Celda) -> Unit,
                  onClickInvertir: (Celda) -> Unit,
                  onClickSeleccionarFila: (Fila) -> Unit): @Composable () -> Unit {

    if (panelConfiguracion.mostrarTabla) {
        return {
            Tabla(
                modifier = Modifier.fillMaxSize(),
                panelConfiguracion = panelConfiguracion,
                //tabla = valoresTabla,
                filas = filas,
                celdasFiltro = celdasFiltro,
                mostrarTitulos = panelConfiguracion.mostrarTituloTabla,
                onClickSeleccionarFiltro = onClickSeleccionarFiltro,
                onClickInvertir = onClickInvertir,
                onClickSeleccionarFila = onClickSeleccionarFila
            )
        }
    } else {
        return {}
    }
}
