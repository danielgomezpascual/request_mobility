package com.personal.requestmobility.core.composables.componentes.panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.componentes.MA_Marco
import com.personal.requestmobility.core.composables.graficas.MA_GraficoAnillo
import com.personal.requestmobility.core.composables.graficas.MA_GraficoBarras
import com.personal.requestmobility.core.composables.graficas.MA_GraficoBarrasVerticales
import com.personal.requestmobility.core.composables.graficas.MA_GraficoCircular
import com.personal.requestmobility.core.composables.graficas.MA_GraficoLineas
import com.personal.requestmobility.core.composables.labels.MA_LabelMini
import com.personal.requestmobility.core.composables.tabla.Celda
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.MA_Tabla

import kotlin.collections.filter
import kotlin.collections.map


@Composable
fun MA_Panel(modifier: Modifier = Modifier,
             panelData: PanelData

) {
    //val graTabData by remember { mutableStateOf<GraTabData>(gr) }




    val configuracion = panelData.panelConfiguracion

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
        panelConfiguracion = configuracion,
        modifier = modifier,
        filas = filasPintar,
        posicionX = panelData.panelConfiguracion.campoAgrupacionTabla,
        posivionY = panelData.panelConfiguracion.campoSumaValorTabla

    )



    tablaComposable = dameTipoTabla(
        panelConfiguracion = configuracion,
        modifier = modifier,

        filas = filasPintar,
        celdasFiltro = celdasFiltro,

        onClickSeleccionarFila = { fila ->
            filas = filas.map { f ->
                if (fila.seleccionada) {
                    val colorAlpha = f.color.copy(alpha = 1.0f)
                    f.copy(seleccionada = false, color = colorAlpha)

                } else {

                    if (f.equals(fila)) {
                        val colorAlpha = f.color.copy(alpha = 1.0f)
                        f.copy(seleccionada = true, color = colorAlpha)
                    } else {
                        val colorAlpha = f.color.copy(alpha = 0.2f)
                        f.copy(seleccionada = false, color = colorAlpha)
                    }
                }

            }
            celdasFiltro = fila.celdas
        },
        onClickInvertir = { cfi ->
            celdasFiltro = celdasFiltro.map { c ->
                if (c.titulo.equals(cfi.titulo)) {
                    if (!cfi.filtroInvertido) {
                        c.copy(filtroInvertido = true, seleccionada = true)
                    } else {
                        c.copy(filtroInvertido = false)
                    }
                } else {
                    c
                }
            }
            filas = cumplenFiltro(filas, celdasFiltro)

        },
        onClickSeleccionarFiltro = { cf ->
            celdasFiltro = celdasFiltro.map { c ->
                if (c.titulo.equals(cf.titulo)) {
                    cf.copy(seleccionada = !cf.seleccionada)
                } else {
                    c
                }
            }

            /*filas = filas.map { fila ->
                var cumpleFiltro: Boolean = true
                fila.celdas.forEach { celdaFila ->
                    celdasFiltro.filter { it.seleccionada }.forEach { celdaFiltro ->
                        if ((celdaFila.titulo.equals(celdaFiltro.titulo))
                            && !(celdaFila.valor.equals(celdaFiltro.valor))) {
                            cumpleFiltro = false
                        }
                    }
                }
                fila.copy(visible = cumpleFiltro)
            }*/

            filas = cumplenFiltro(filas, celdasFiltro)

        }
    )


    when (configuracion.orientacion) {
        PanelOrientacion.VERTICAL -> {
            GraficaConTablaVertical(
                modifier = modifier,
                panelConfiguracion = configuracion,
                grafica = { graficaComposable() },
                tabla = { tablaComposable() }
            )
        }

        PanelOrientacion.HORIZONTAL -> {
            GraficaConTablaHorizontal(
                modifier = modifier,
                panelConfiguracion = configuracion,
                grafica = { graficaComposable() },
                tabla = { tablaComposable() }
            )
        }
    }

}

fun cumplenFiltro(filas: List<Fila>, celdasFiltro: List<Celda>): List<Fila> = filas.map { fila ->
    var cumpleFiltro: Boolean = true
    fila.celdas.forEach { celdaFila ->
        celdasFiltro.filter { it.seleccionada }.forEach { celdaFiltro ->

            if ((celdaFila.titulo.equals(celdaFiltro.titulo))
                &&
                (!celdaFiltro.filtroInvertido && !(celdaFila.valor.equals(celdaFiltro.valor)))
                ||
                (celdaFiltro.filtroInvertido && (celdaFila.valor.equals(celdaFiltro.valor)))
            ) {
                cumpleFiltro = false
            }
        }
    }
    fila.copy(visible = cumpleFiltro)
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

        MA_Marco(titulo = panelConfiguracion.titulo, modifier = m, componente = {

          MA_LabelMini(panelConfiguracion.descripcion)

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

                    var modifier :  Modifier = Modifier
                    modifier = if (panelConfiguracion.ocuparTodoEspacio){
                        modifier.padding( PaddingValues(0.dp, 0.dp))
                    }else{
                        modifier.padding( PaddingValues(60.dp, 15.dp))
                    }


                    Box(
                        modifier = modifier

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

        MA_Marco(titulo = panelConfiguracion.titulo, modifier = m, componente = {
            Column() {
                MA_LabelMini(panelConfiguracion.descripcion)



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

                    var modifier :  Modifier = Modifier
                    modifier = if (panelConfiguracion.ocuparTodoEspacio){
                        modifier.padding( PaddingValues(0.dp, 0.dp))
                    }else{
                        modifier.padding( PaddingValues(60.dp, 15.dp))
                    }

                    Box(

                        modifier = modifier
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

                MA_GraficoBarras(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX = posicionX,
                    posivionY = posivionY
                )

            }

            PanelTipoGrafica.BARRAS_FINAS_VERTICALES -> {
                MA_GraficoBarrasVerticales(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX = posicionX,
                    posivionY = posivionY
                )
            }

            PanelTipoGrafica.CIRCULAR -> {
                MA_GraficoCircular(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX = posicionX,
                    posivionY = posivionY
                )
            }

            PanelTipoGrafica.ANILLO -> {
                MA_GraficoAnillo(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX = posicionX,
                    posivionY = posivionY
                )
            }

            PanelTipoGrafica.LINEAS -> {
                MA_GraficoLineas(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX = posicionX,
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
            MA_Tabla(
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
