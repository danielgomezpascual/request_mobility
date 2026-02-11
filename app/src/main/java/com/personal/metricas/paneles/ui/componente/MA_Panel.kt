package com.personal.metricas.paneles.ui.componente

import MA_IconBottom
import MA_Morph
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.App
import com.personal.metricas.App.Companion.context
import com.personal.metricas.App.Companion.dialog
import com.personal.metricas.App.Companion.log
import com.personal.metricas.R
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.graficas.MA_GraficoAnillo
import com.personal.metricas.core.composables.graficas.MA_GraficoBarras
import com.personal.metricas.core.composables.graficas.MA_GraficoBarrasVerticales
import com.personal.metricas.core.composables.graficas.MA_GraficoCircular
import com.personal.metricas.core.composables.graficas.MA_GraficoLineas
import com.personal.metricas.core.composables.graficas.MA_IndicadorHorizontal
import com.personal.metricas.core.composables.graficas.MA_IndicadorVertical
import com.personal.metricas.core.composables.graficas.MA_SignalHorizontal
import com.personal.metricas.core.composables.graficas.MA_SignalVertical
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.tabla.Celda
import com.personal.metricas.core.composables.tabla.Fila
import com.personal.metricas.core.composables.tabla.MA_Tabla
import com.personal.metricas.core.domain.usecases.ObtenerDatosTransaccionCU
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.utils.FcmSender
import com.personal.metricas.core.utils.FirebaseDatabase
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils._t
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.endpoints.domain.entidades.ResultadoEndPoint
import com.personal.metricas.endpoints.domain.interactors.AlmacenarDatosRemotosEndPointCU
import com.personal.metricas.menu.Features
import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.paneles.domain.entidades.PanelData
import com.personal.metricas.paneles.domain.entidades.PanelOrientacion
import com.personal.metricas.paneles.domain.entidades.PanelTipoGrafica
import com.personal.metricas.paneles.domain.entidades.TiposPanel
import kotlin.math.ceil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

@Composable
fun MA_Panel(
        modifier: Modifier = Modifier,
        panelData: PanelData,
) {

    var tieneErrores: Boolean = false
    var mensajeError: String = ""
    var trazaError: String = ""

    // variable para controlar el estado de las filas que se estan presentado en la tabla
    var filas by remember { mutableStateOf<List<Fila>>(panelData.valoresTabla.filas) }

    // varaible para controlar el estadp de  las celdas y los atributos que se seleccionan
    var celdasFiltro by remember { mutableStateOf<List<Celda>>(emptyList()) }

    var tablaComposable: @Composable () -> Unit = {}
    var graficaComposable: @Composable () -> Unit = {}

    val configuracion =
            panelData.panelConfiguracion.copy(
                    titulo = panelData.panel.titulo,
                    descripcion = panelData.panel.descripcion
            )
    lateinit var fs: List<Fila>
    lateinit var filasPintar: List<Fila>

    try {

        if (configuracion.limiteElementos > 0) {
            filas = panelData.limiteElementos()
            panelData.valoresTabla.filas = filas
        }

        if (configuracion.ordenado) {
            filas = panelData.ordenarElementos()
        }

        filas = panelData.aplicarCondicionesFilas()

        // --------------------------------------------------
        filasPintar =
                filas.filter {
                    it.visible == true
                } // solo pintamos las filas que estas visibles, el resto no.

        // establecemos los colores
        val hayFilaSeleccionada: Boolean = !filasPintar.none { it.seleccionada }
        fs =
                filasPintar.map { fila ->
                    var colorAlpha = fila.color.copy(alpha = 1.0f)
                    if (hayFilaSeleccionada) {
                        colorAlpha = fila.color.copy(alpha = 0.20f)
                        if (fila.seleccionada) {
                            colorAlpha = fila.color.copy(alpha = 1.0f)
                        }
                    }
                    fila.copy(color = colorAlpha)
                }
    } catch (e: Exception) {
        mensajeError = e.message.toString()
        trazaError = e.stackTrace.take(5).toString()
        tieneErrores = true
    }

    if (tieneErrores) {
        Card(
                colors =
                        CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                        imageVector = Icons.Default.Error,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        contentDescription = ""
                )
                MA_LabelNegrita("ERROR", color = MaterialTheme.colorScheme.onErrorContainer)
                MA_LabelMini(panelData.panel.descripcion)
                MA_LabelNegrita(mensajeError)
                MA_LabelNormal(trazaError)
            }
        }
        return
    }
    val scope = rememberCoroutineScope() // Se mantiene dentro del componente
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        MA_Morph()
    }

    val identificador = panelData.dameIdentificador()

    when (panelData.panel.tipoPanel) {
        TiposPanel.PANEL_TEXTO -> {
            ElevatedCard(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    colors =
                            CardDefaults.elevatedCardColors(
                                    containerColor =
                                            if (panelData.panelConfiguracion.colorPanel != 0)
                                                    Color(panelData.panelConfiguracion.colorPanel)
                                            else MaterialTheme.colorScheme.surface
                            ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    MA_LabelNormal(panelData.panel.titulo)
                    MA_Spacer(Modifier.padding(4.dp))
                    MA_LabelMini(panelData.panel.descripcion)

                    MA_Spacer(Modifier.padding(8.dp))
                    MA_LabelMini(
                            modifier = Modifier.fillMaxWidth(),
                            valor = identificador,
                            alineacion = TextAlign.End,
                            size = 9.sp,
                            fontStyle = FontStyle.Italic
                    )
                }
            }
        }
        TiposPanel.PANEL_END_POINT -> {
            ElevatedCard(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    colors =
                            CardDefaults.elevatedCardColors(
                                    containerColor =
                                            if (panelData.panelConfiguracion.colorPanel != 0)
                                                    Color(panelData.panelConfiguracion.colorPanel)
                                            else MaterialTheme.colorScheme.surface
                            ),
                    onClick = {
                        scope.launch {
                            isLoading = true
                            try {
                                log.lista("Paramtros Dashboard", panelData.parametrosOrigenDatos.ps)
                                async() {
                                            val procesarEndPoint: AlmacenarDatosRemotosEndPointCU =
                                                    KoinJavaComponent.getKoin().get()
                                            val resultado: ResultadoEndPoint =
                                                    procesarEndPoint.obtenerRemoto(
                                                            panelData.panel.endPoint
                                                    )
                                        }
                                        .await()
                                dialog.informacion(_t(R.string.information_actualizada)) {}
                            } finally {
                                isLoading = false
                            }
                        }
                    }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MA_IconBottom(
                                icon = Features.EndPoints().icono,
                                labelText = "", // Label vacio aqui por diseño
                                color = Features.EndPoints().color,
                                onClick = {} // Sin accion extra
                        )
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            MA_LabelNormal(panelData.panel.titulo)
                            MA_LabelMini(identificador, size = 9.sp, fontStyle = FontStyle.Italic)
                        }
                    }
                }
            }
        }
        TiposPanel.PANEL_CONECTOR -> {
            log.lista("Valres tabla", panelData.valoresTabla.filas)
            log.lista("Valres tabla", filasPintar)

            Row(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .horizontalScroll(state = rememberScrollState())
                                    .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filasPintar.forEach { fila ->
                    pintarPanelConectores(panelData, panelData.panel.conector.identificador, fila)
                }
            }
        }
        TiposPanel.PANEL_KPI -> {
            var panelDataState by remember { mutableStateOf(panelData) }

            graficaComposable =
                    dameTipoGrafica(
                            panelConfiguracion = configuracion,
                            modifier = modifier,
                            filas = fs,
                            posicionX = panelData.panelConfiguracion.columnaX,
                            posivionY = panelData.panelConfiguracion.columnaY
                    )

            tablaComposable =
                    dameTipoTabla(
                            panelDataState,
                            panelConfiguracion = configuracion,
                            modifier = modifier,
                            filas = filasPintar,
                            notas = panelData.notasManager.notas,
                            celdasFiltro = celdasFiltro,
                            onClickSeleccionarFila = { fila ->
                                if (fila.seleccionada) {
                                    panelData.filaSeleccionada = null
                                } else {
                                    panelData.filaSeleccionada = fila.copy(seleccionada = true)
                                }
                                filas =
                                        filas.map { f ->
                                            log.d("Cambio en la seleccion del filtro")
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
                                panelDataState = panelDataState.copy(indice = 0)
                                log.d("Cambio en la INVERSION DEL FILTRO")
                                celdasFiltro =
                                        celdasFiltro.map { c ->
                                            if (c.titulo.equals(cfi.titulo)) {
                                                if (!cfi.filtroInvertido) {
                                                    c.copy(
                                                            filtroInvertido = true,
                                                            seleccionada = true
                                                    )
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
                                panelDataState = panelDataState.copy(indice = 0)
                                celdasFiltro =
                                        celdasFiltro.map { c ->
                                            if (c.titulo.equals(cf.titulo)) {
                                                cf.copy(seleccionada = !cf.seleccionada)
                                            } else {
                                                c
                                            }
                                        }
                                filas = cumplenFiltro(filas, celdasFiltro)
                                panelData.valoresTabla.filas = filas
                            },
                            onClickBorrarFiltros = {
                                panelDataState = panelDataState.copy(indice = 0)
                                celdasFiltro = emptyList() // Limpiar filtros de celdas
                                filas =
                                        filas.map { fila ->
                                            fila.copy(
                                                    visible = true,
                                                    seleccionada = false
                                            ) // Restaurar visibilidad y quitar
                                            // seleccion
                                        }
                                panelData.valoresTabla.filas = filas
                            },
                            onClickFiltrarTexto = { str ->
                                panelDataState = panelDataState.copy(indice = 0)

                                // Filtrar las filas buscando el texto en el
                                // contenido de las celdas
                                filas =
                                        filas.map { fila ->
                                            // Buscar en todas las celdas de la
                                            // fila
                                            // (case-insensitive)
                                            val cumpleFiltro =
                                                    if (str.isBlank()) {
                                                        true // Si el texto
                                                        // de búsqueda
                                                        // está vacío,
                                                        // mostrar todas las
                                                        // filas
                                                    } else {
                                                        fila.celdas.any { celda ->
                                                            celda.valor
                                                                    .toString()
                                                                    .contains(
                                                                            str,
                                                                            ignoreCase = true
                                                                    )
                                                        }
                                                    }

                                            log.d(
                                                    "Filtro texto: '$str' - Fila cumple: $cumpleFiltro"
                                            )
                                            fila.copy(visible = cumpleFiltro)
                                        }

                                // Actualizar el estado del panel
                                panelData.valoresTabla.filas = filas
                            },
                            onClickIndicePaginacion = { indice ->
                                panelDataState = panelDataState.copy(indice = indice)
                            }
                    )

            val transparencia =
                    if3(Color(panelData.panelConfiguracion.colorPanel) == Color.White, 1.0f, 0.9f)

            Surface(
                    color =
                            if (panelData.panelConfiguracion.colorPanel != 0)
                                    Color(panelData.panelConfiguracion.colorPanel)
                                            .copy(alpha = transparencia)
                            else MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(6.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    when (configuracion.orientacion) {
                        PanelOrientacion.VERTICAL -> {
                            MA_GraficaConTablaVertical(
                                    modifier = modifier,
                                    panelConfiguracion = configuracion,
                                    grafica = { graficaComposable() },
                                    tabla = { tablaComposable() },
                                    alarmas = panelData.listaAlarmas
                            )
                        }
                        PanelOrientacion.HORIZONTAL -> {
                            MA_GraficaConTablaHorizontal(
                                    modifier = modifier,
                                    panelConfiguracion = configuracion,
                                    grafica = { graficaComposable() },
                                    tabla = { tablaComposable() },
                                    alarmas = panelData.listaAlarmas
                            )
                        }
                    }

                    MA_Spacer(Modifier.padding(4.dp))
                    MA_LabelMini(
                            modifier = Modifier.fillMaxWidth(),
                            valor = identificador,
                            alineacion = TextAlign.End,
                            size = 9.sp,
                            fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
fun pintarPanelConectores(panelData: PanelData, identificadorDashboard: Int, fila: Fila = Fila()) {

    ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            colors =
                    CardDefaults.elevatedCardColors(
                            containerColor =
                                    if (panelData.panelConfiguracion.colorPanel != 0)
                                            Color(panelData.panelConfiguracion.colorPanel)
                                    else MaterialTheme.colorScheme.surface
                    ),
            modifier =
                    Modifier.padding(4.dp).clickable {
                        goto(
                                EventosNavegacion.VisualizadorDashboard(
                                        identificadorDashboard,
                                        _toJson(fila.toParametros())
                                ),
                                App.navController
                        )
                    }
    ) {
        Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val s: String =
                    Parametros.reemplazar(
                            panelData.panel.titulo,
                            fila.toParametros(),
                            fila.toParametros()
                    )

            MA_IconBottom(
                    icon = Features.Dashboard().icono,
                    labelText = "",
                    color = Features.Dashboard().color
            ) {}

            MA_Spacer(Modifier.padding(4.dp))
            MA_LabelNormal(valor = s, alineacion = TextAlign.Center)
        }
    }
}

fun cumplenFiltro(filas: List<Fila>, celdasFiltro: List<Celda>): List<Fila> =
        filas.map { fila ->
            var cumpleFiltro: Boolean = true
            fila.celdas.forEach { celdaFila ->
                celdasFiltro.filter { it.seleccionada }.forEach { celdaFiltro ->
                    if ((celdaFila.titulo.equals(celdaFiltro.titulo)) &&
                                    (!celdaFiltro.filtroInvertido &&
                                            !(celdaFila.valor.equals(celdaFiltro.valor))) ||
                                    (celdaFiltro.filtroInvertido &&
                                            (celdaFila.valor.equals(celdaFiltro.valor)))
                    ) {
                        cumpleFiltro = false
                    }
                }
            }
            fila.copy(visible = cumpleFiltro)
        }

@Composable
fun dameTipoGrafica(
        panelConfiguracion: PanelConfiguracion,
        modifier: Modifier,
        filas: List<Fila>,
        posicionX: Int = 0,
        posivionY: Int = 1,
): @Composable () -> Unit {
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
            is PanelTipoGrafica.IndicadorVertical -> {
                MA_IndicadorVertical(
                        modifier = modifier,
                        listaValores = datosPintar,
                        posicionX = posicionX,
                        posicionY = posivionY,
                        panelConfiguracion = panelConfiguracion
                )
            }
            is PanelTipoGrafica.IndicadorHorizontal -> {
                MA_IndicadorHorizontal(
                        modifier = modifier,
                        listaValores = datosPintar,
                        posicionX = posicionX,
                        posicionY = posivionY,
                        panelConfiguracion = panelConfiguracion
                )
            }
            is PanelTipoGrafica.BarrasAnchasVerticales -> {

                MA_GraficoBarras(
                        modifier = modifier,
                        listaValores = datosPintar,
                        posicionX = posicionX,
                        posicionY = posivionY,
                        panelConfiguracion = panelConfiguracion,
                )
            }
            is PanelTipoGrafica.BarrasFinasVerticales -> {
                MA_GraficoBarrasVerticales(
                        modifier = modifier,
                        listaValores = datosPintar,
                        posicionX = posicionX,
                        posivionY = posivionY,
                        panelConfiguracion = panelConfiguracion
                )
            }
            is PanelTipoGrafica.Circular -> {
                MA_GraficoCircular(
                        modifier = modifier,
                        listaValores = datosPintar,
                        posicionX = posicionX,
                        posivionY = posivionY,
                        panelConfiguracion = panelConfiguracion
                )
            }
            is PanelTipoGrafica.Anillo -> {
                MA_GraficoAnillo(
                        modifier = modifier,
                        listaValores = datosPintar,
                        posicionX = posicionX,
                        posivionY = posivionY,
                        panelConfiguracion = panelConfiguracion
                )
            }
            is PanelTipoGrafica.Lineas -> {
                MA_GraficoLineas(
                        modifier = modifier,
                        listaValores = datosPintar,
                        posicionX = posicionX,
                        posivionY = posivionY,
                        panelConfiguracion = panelConfiguracion
                )
            }
            is PanelTipoGrafica.SignalVertical -> {
                MA_SignalVertical(
                        modifier = modifier,
                        listaValores = datosPintar,
                        posicionX = posicionX,
                        posicionY = posivionY,
                        panelConfiguracion = panelConfiguracion
                )
            }
            is PanelTipoGrafica.SignalHorizontal -> {
                MA_SignalHorizontal(
                        modifier = modifier,
                        listaValores = datosPintar,
                        posicionX = posicionX,
                        posicionY = posivionY,
                        panelConfiguracion = panelConfiguracion
                )
            }
        }
    }
}

@Composable
fun dameTipoTabla(
        panelData: PanelData,
        panelConfiguracion: PanelConfiguracion,
        modifier: Modifier,
        filas: List<Fila>,
        notas: List<Notas>,
        celdasFiltro: List<Celda>,
        onClickSeleccionarFiltro: (Celda) -> Unit,
        onClickInvertir: (Celda) -> Unit,
        onClickSeleccionarFila: (Fila) -> Unit,
        onClickFiltrarTexto: (String) -> Unit,
        onClickBorrarFiltros: () -> Unit,
        onClickIndicePaginacion: (Int) -> Unit,
): @Composable () -> Unit {

    if (panelConfiguracion.mostrarTabla) {
        return {
            Column(modifier /*.background(color = Color.Magenta)*/) {
                Box(modifier = Modifier.weight(1f)) {
                    MA_Tabla(
                            modifier =
                                    Modifier /*.background(color = Color(129, 199, 132, 255))*/.fillMaxSize(),
                            panelConfiguracion = panelConfiguracion,
                            // tabla = valoresTabla,
                            filasOriginal = filas,
                            notas = notas,
                            celdasFiltro = celdasFiltro,
                            mostrarTitulos = panelConfiguracion.mostrarTituloTabla,
                            indice = panelData.indice,
                            elementos = panelData.elementos,
                            onClickSeleccionarFiltro = onClickSeleccionarFiltro,
                            onClickInvertir = onClickInvertir,
                            onClickSeleccionarFila = onClickSeleccionarFila,
                            onClickFiltrarTexto = onClickFiltrarTexto,
                            onClickBorrarFiltros = onClickBorrarFiltros,
                            onClickAbrirMobility = { celda ->

                                // Token del dispositivo que va a recibir el mensaje

                                log.c("--> ${panelData.filaSeleccionada!!.dameValor("firebase")}")
                                log.c(
                                        "--> ${panelData.filaSeleccionada!!.dameValor("MOB_REQUEST_ID")}"
                                )

                                /*	val tokenDestino =
                                								"cXheVvR5S-K2A0qd57lBvs:APA91bF2MP6s0lwivmJYYRgUpc-HGfAcV7xp-E4BXrdCAPnnK96p0FXDNGPvsKGuEgSwvmbX6crqeX6LAHyhtIiCkSohwRlyou8m5RC1mSnuD0XM3r6HwVE"
                                */
                                GlobalScope.launch {
                                    val obtenerDatosTransaccion: ObtenerDatosTransaccionCU =
                                            KoinJavaComponent.getKoin().get()
                                    // Dentro de tu corrutina/bloque de ejecución:
                                    try {
                                        val mob_request_id =
                                                panelData.filaSeleccionada!!.dameValor(
                                                        "MOB_REQUEST_ID"
                                                )
                                        val transaccion =
                                                obtenerDatosTransaccion.ejecutar(mob_request_id)
                                        val tokenDestino = transaccion.firebase
                                        // ... continuar con el envío

                                        if (tokenDestino.isNullOrEmpty()) {
                                            return@launch
                                        }
                                        println("Iniciando envío...")
                                        // val mob_request_id = "825945"
                                        val resultado =
                                                FcmSender.sendNotification(
                                                        deviceToken = tokenDestino,
                                                        title = "¡Hola desde Kotlin!",
                                                        mob_request_id = mob_request_id
                                                )



                                        dialog.informacion(
                                            "Se ha enviado la solicitud para : $mob_request_id , abra Mobility para intentar la recuperacion de estos datos",
                                            onResultadoDialog = {},
                                        )

                                        println("Resultado: $resultado")
/*


                                        // Monitorizar si se crea el registro en Firebase Realtime
                                        // Database
                                        FirebaseDatabase.monitorizar(
                                                "intercambio_datos/$mob_request_id"
                                        ) { code, data ->
                                            if (code == 200) {
                                                dialog.informacion(
                                                        "Se ha detectado la subida de datos para la transacción: $mob_request_id",
                                                        onResultadoDialog = {},
                                                )
                                            }
                                        }*/
                                    } catch (e: Exception) {

										dialog.informacion(
											"Error al obtener transacción: ${e.message}",
											onResultadoDialog = {},
										)
                                        log.e("Error al obtener transacción: ${e.message}")
                                    }
                                }
                            }
                    )
                }
                MA_Spacer()

                val paginasComoDouble = filas.size.toDouble() / panelData.elementos.toDouble()
                val numeroDePaginas = ceil(paginasComoDouble).toInt()
                if (numeroDePaginas > 1) {
                    Row(
                            modifier =
                                    Modifier.fillMaxWidth() /*.background(color = Color(220, 231, 117, 255))*/
                                            .horizontalScroll(rememberScrollState())
                                            .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                    ) {
                        (0..numeroDePaginas - 1).forEach { it ->
                            if (it == panelData.indice) {
                                MA_LabelNegrita(
                                        valor = it.toString(),
                                        modifier.clickable(
                                                enabled = true,
                                                onClick = { onClickIndicePaginacion(it) }
                                        )
                                )
                            } else {
                                MA_LabelMini(
                                        valor = it.toString(),
                                        modifier.clickable(
                                                enabled = true,
                                                onClick = { onClickIndicePaginacion(it) }
                                        )
                                )
                            }

                            MA_Spacer()
                        }
                    }
                }
            }
        }
    } else {
        return {}
    }
}

fun probarSincronizacionFirebase(context: Context, transactionId: String) {
    val intent =
            Intent("com.maxam.ACTION_SYNC_FIREBASE").apply {
                // Ponemos el ID que queremos monitorizar
                putExtra("transaction_id", transactionId)
            }

    // Enviamos el broadcast para despertar al FirebaseSyncReceiver
    context.sendBroadcast(intent)

    // android.util.Log.d("TEST_FIREBASE", "Broadcast enviado para monitorizar ID: $transactionId")
}
