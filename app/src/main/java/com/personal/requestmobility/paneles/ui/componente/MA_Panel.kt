package com.personal.requestmobility.paneles.ui.componente

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.personal.requestmobility.core.composables.graficas.MA_GraficoAnillo
import com.personal.requestmobility.core.composables.graficas.MA_GraficoBarras
import com.personal.requestmobility.core.composables.graficas.MA_GraficoBarrasVerticales
import com.personal.requestmobility.core.composables.graficas.MA_GraficoCircular
import com.personal.requestmobility.core.composables.graficas.MA_GraficoLineas
import com.personal.requestmobility.core.composables.graficas.MA_IndicadorHorizontal
import com.personal.requestmobility.core.composables.graficas.MA_IndicadorVertical
import com.personal.requestmobility.core.composables.tabla.Celda
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.MA_Tabla
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion
import com.personal.requestmobility.paneles.domain.entidades.PanelData
import com.personal.requestmobility.paneles.domain.entidades.PanelOrientacion
import com.personal.requestmobility.paneles.domain.entidades.PanelTipoGrafica

import kotlin.collections.filter
import kotlin.collections.map


@Composable
fun MA_Panel(modifier: Modifier = Modifier,
             panelData: PanelData

) {

    val configuracion = panelData.panelConfiguracion

    //  panelData.setupValores()

    //variable para controlar el estado de las filas que se estan presentado en la tabla
    var filas by remember { mutableStateOf<List<Fila>>(panelData.valoresTabla.filas) }

    //varaible para controlar el estadp de  las celdas y los atributos que se seleccionan
    var celdasFiltro by remember { mutableStateOf<List<Celda>>(emptyList()) }





    if (configuracion.limiteElementos > 0) {
        filas = panelData.limiteElementos()
        panelData.valoresTabla.filas = filas
    }

    if (configuracion.ordenado) {
        filas = panelData.ordenarElementos()

    }

    filas = panelData.establecerColorFilas()

//--------------------------------------------------
    val filasPintar = filas.filter { it.visible == true } //solo pintamos las filas que estas visibles, el resto no.

    var tablaComposable: @Composable () -> Unit = {}
    var graficaComposable: @Composable () -> Unit = {}




    //establecemos los colores
    val hayFilaSeleccionada: Boolean = !filasPintar.none { it.seleccionada }
    val fs: List<Fila> = filasPintar.map { fila ->
        var colorAlpha = fila.color.copy(alpha = 1.0f)
        if (hayFilaSeleccionada) {
            colorAlpha = fila.color.copy(alpha = 0.20f)
            if (fila.seleccionada) {
                colorAlpha = fila.color.copy(alpha = 1.0f)
            }
        }
        fila.copy(color = colorAlpha)
    }


    graficaComposable = dameTipoGrafica(
        panelConfiguracion = configuracion,
        modifier = modifier,
        filas = fs,
        posicionX = panelData.panelConfiguracion.columnaX,
        posivionY = panelData.panelConfiguracion.columnaY

    )



    tablaComposable = dameTipoTabla(
        panelConfiguracion = configuracion,
        modifier = modifier,
        filas = filasPintar,
        celdasFiltro = celdasFiltro,

        onClickSeleccionarFila = { fila ->
            filas = filas.map { f ->
                if (fila.seleccionada) {
                    f.copy(seleccionada = false)

                } else {

                    if (f.equals(fila)) {

                        f.copy(seleccionada = true)
                    } else {

                        f.copy(seleccionada = false)
                    }
                }

            }
            celdasFiltro = fila.celdas
            panelData.valoresTabla.filas = filas
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
            panelData.valoresTabla.filas = filas
        },
        onClickSeleccionarFiltro = { cf ->
            celdasFiltro = celdasFiltro.map { c ->
                if (c.titulo.equals(cf.titulo)) {
                    cf.copy(seleccionada = !cf.seleccionada)
                } else {
                    c
                }
            }

            filas = cumplenFiltro(filas, celdasFiltro)
            panelData.valoresTabla.filas = filas


        }
    )


    when (configuracion.orientacion) {
        PanelOrientacion.VERTICAL -> {
            MA_GraficaConTablaVertical(
                modifier = modifier,
                panelConfiguracion = configuracion,
                grafica = { graficaComposable() },
                tabla = { tablaComposable() }
            )
        }

        PanelOrientacion.HORIZONTAL -> {
            MA_GraficaConTablaHorizontal(
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

            PanelTipoGrafica.INDICADOR_VERTICAL ->{
                MA_IndicadorVertical(  modifier = modifier,
                    listaValores = datosPintar,
                    posicionX = posicionX,
                    posicionY = posivionY)
            }

            PanelTipoGrafica.INDICADOR_HORIZONTAL ->{
                MA_IndicadorHorizontal(  modifier = modifier,
                    listaValores = datosPintar,
                    posicionX = posicionX,
                    posicionY = posivionY)
            }

            PanelTipoGrafica.BARRAS_ANCHAS_VERTICALES -> {

                MA_GraficoBarras(
                    modifier = modifier,
                    listaValores = datosPintar,
                    posicionX = posicionX,
                    posicionY = posivionY
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
