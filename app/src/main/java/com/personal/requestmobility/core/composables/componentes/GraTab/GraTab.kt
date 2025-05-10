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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.personal.requestmobility.core.composables.componentes.Marco
import com.personal.requestmobility.core.composables.graficas.GraficoAnillo
import com.personal.requestmobility.core.composables.graficas.GraficoBarras
import com.personal.requestmobility.core.composables.graficas.GraficoBarrasVerticales
import com.personal.requestmobility.core.composables.graficas.GraficoCircular
import com.personal.requestmobility.core.composables.graficas.GraficoLineas
import com.personal.requestmobility.core.composables.graficas.ElementoGrafica
import com.personal.requestmobility.core.composables.tabla.Tabla

import com.personal.requestmobility.core.composables.tabla.ValoresTabla
import com.personal.requestmobility.core.composables.tabla.toValoresGrafica


@Composable
fun GraTab(modifier: Modifier = Modifier,
           gr: GraTabData

) {
     val graTabData by remember{ mutableStateOf<GraTabData>(gr) }



    val configGrafica = graTabData.graTabConfiguracion

    if (configGrafica.ordenado) {
        graTabData.ordenarElementos()
    }

    if (configGrafica.limiteElementos > 0) {
        graTabData.limiteElementos()
    }
    graTabData.setupValores()

//--------------------------------------------------


   // var datosTabla: ValoresTabla = ValoresTabla()

   // if (graTabData.graTabConfiguracion.mostrarTabla) {


        val valoresTabla by remember {mutableStateOf<ValoresTabla>(graTabData.valoresTabla)}









//--------------------------------------------------
    var tablaComposable: @Composable () -> Unit = {}
    var graficaComposable: @Composable () -> Unit = {}

    if (graTabData.graTabConfiguracion.mostrarGrafica) {
//        val valoresGrafica = graTabData.valoresGrafica
        //var datosGrafica: List<ElementoGrafica> =


        val listaElementosMostrarGrafica : List<ElementoGrafica> =  valoresTabla.toValoresGrafica(columnaTexto = 0, columnaValor = 1).elementos
        graficaComposable = dameTipoGrafica(configGrafica, modifier,listaElementosMostrarGrafica)
        //App.log.lista("GR", datosGrafica)
    }

    if (graTabData.graTabConfiguracion.mostrarTabla) {
        tablaComposable = dameTipoTabla(configGrafica, modifier, valoresTabla)
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
fun dameTipoGrafica(graTabConfiguracion: GraTabConfiguracion, modifier: Modifier, datos: List<ElementoGrafica>): @Composable () -> Unit {
    if (!graTabConfiguracion.mostrarGrafica) {
        return {}
    }
    var datosPintar = datos
    if (!graTabConfiguracion.mostrarEtiquetas) {
        datosPintar = datos.map { it.copy(leyenda = "") }
    }

    return {
        when (graTabConfiguracion.tipo) {
            GraTabTipoGrafica.BARRAS_ANCHAS_VERTICALES -> {

                /*Box(Modifier
                    .width(500.dp)
                    .height(300.dp)) {*/
                GraficoBarras(
                    modifier = modifier,
                    listaValores = datosPintar
                )
                //}
            }

            GraTabTipoGrafica.BARRAS_FINAS_VERTICALES -> {
                GraficoBarrasVerticales(
                    modifier = modifier,
                    listaValores = datosPintar
                )
            }

            GraTabTipoGrafica.CIRCULAR -> {
                GraficoCircular(
                    modifier = modifier,
                    listaValores = datosPintar
                )

            }

            GraTabTipoGrafica.ANILLO -> {

                GraficoAnillo(
                    modifier = modifier,
                    listaValores = datosPintar
                )

            }

            GraTabTipoGrafica.LINEAS -> {
                GraficoLineas(
                    modifier = modifier,
                    listaValores = datosPintar
                )
            }
        }
    }


}

@Composable
fun dameTipoTabla(graTabConfiguracion: GraTabConfiguracion, modifier: Modifier, valoresTabla: ValoresTabla): @Composable () -> Unit {
    if (graTabConfiguracion.mostrarTabla) {
        return {
            Tabla(
                modifier = Modifier.fillMaxSize(),
                tabla = valoresTabla,
                mostrarTitulos = graTabConfiguracion.mostrarTituloTabla
            )
        }
    } else {
        return {}
    }
}
