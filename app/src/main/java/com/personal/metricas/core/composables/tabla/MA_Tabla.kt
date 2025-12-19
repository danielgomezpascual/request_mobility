package com.personal.metricas.core.composables.tabla

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.personal.metricas.App
import com.personal.metricas.R
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonSecundarioSinBorde
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.composables.edittext.MA_TextoEditable
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.composables.listas.MA_Lista
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils._t
import com.personal.metricas.core.utils.if3
import com.personal.metricas.excel.GenerateExcel
import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.transacciones.ui.screens.composables.ModalInferiorFiltros
import java.io.File
import kotlin.math.ceil
import kotlin.math.max
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

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
fun TablaDatos(
        modifier: Modifier = Modifier,
        titulo: String = "",
        tabla: ValoresTabla,
) {
    /*
    Marco(modifier = modifier, titulo = titulo) {
        Tabla(modifier, tabla)
    }*/
}

fun logicaAjusteCeldaAncho(columnas: Int, ajustarContenido: Boolean): Boolean {
    val pantallasGrandes =
            App.windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium ||
                    App.windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    if (!pantallasGrandes && columnas > 4) return false

    if (!pantallasGrandes) return ajustarContenido
    return true
}

@Composable
fun MA_Tabla(
        modifier: Modifier = Modifier,
        panelConfiguracion: PanelConfiguracion =
                PanelConfiguracion(
                        ajustarContenidoAncho = false,
                        indicadorColor = false,
                        filasColor = false
                ),
        filasOriginal: List<Fila>,
        notas: List<Notas> = emptyList<Notas>(),
        celdasFiltro: List<Celda> = emptyList<Celda>(),
        mostrarTitulos: Boolean = true,
        indice: Int = 0,
        elementos: Int = 20,
        onClickSeleccionarFiltro: (Celda) -> Unit = {},
        onClickInvertir: (Celda) -> Unit = {},
        onClickSeleccionarFila: (Fila) -> Unit = {},
        onClickFiltrarTexto: (String) -> Unit = {},
        onClickBorrarFiltros: () -> Unit,
) {

    val estadoScroll = rememberScrollState()
    val indicadorColor = panelConfiguracion.indicadorColor
    val filasColor = panelConfiguracion.filasColor

    var paginaActual by remember { mutableIntStateOf(indice) }

    var nuemroCeldas = 0
    try {
        nuemroCeldas =
                if3(
                        (filasOriginal.isEmpty() || filasOriginal.first().celdas.isEmpty()),
                        0,
                        filasOriginal.first().celdas.size
                )
    } catch (e: Exception) {
        App.log.e(e.message)
    }

    val ajustarContenidoAncho =
            logicaAjusteCeldaAncho(nuemroCeldas, panelConfiguracion.ajustarContenidoAncho)
    var modifierColumn = modifier
    if (!ajustarContenidoAncho) {
        modifierColumn = modifierColumn.horizontalScroll(estadoScroll)
    }

    val listaFiltrada = filasOriginal.filter { it.visible }
    val totalPaginas =
            if (elementos > 0) ceil(listaFiltrada.size.toFloat() / elementos).toInt() else 1

    LaunchedEffect(totalPaginas) {
        if (paginaActual >= totalPaginas) {
            paginaActual = max(0, totalPaginas - 1)
        }
    }

    val filas = listaFiltrada.drop(paginaActual * elementos).take(elementos)
    val context = LocalContext.current
    val d: DialogManager = getKoin().get()

    Column(verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxWidth()) {
        // Acciones
        Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Contador de registros y navegación - Parte izquierda
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (celdasFiltro.isNotEmpty()) {
                    ModalInferiorFiltros {
                        var str by remember {
                            mutableStateOf(App.sharedPrerfences.get(K.TXT_FILTROS_LISTAS, ""))
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Cabecera del Filtro
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MA_Titulo("Filtros Avanzados")
                                TextButton(
                                    onClick = { onClickBorrarFiltros() },
                                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Borrar todo")
                                }
                            }

                            // Campo de búsqueda principal
                            MA_TextoEditable(
                                valor = str,
                                titulo = "Búsqueda rápida...",
                                onValueChange = { texto ->
                                    str = texto
                                    App.sharedPrerfences.put(K.TXT_FILTROS_LISTAS, str)
                                    onClickFiltrarTexto(str)
                                }
                            )

                            // Lista de filtros por columna
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                MA_LabelNormal("Filtrar por columna", size = 14.sp, color = MaterialTheme.colorScheme.primary)
                                MA_Lista(celdasFiltro) { celdaFiltro ->
                                    MA_CeldaFiltro(
                                        celda = celdaFiltro,
                                        onClickSeleccion = { cf -> onClickSeleccionarFiltro(cf) },
                                        onClickInvertir = { cf -> onClickInvertir(cf) }
                                    )
                                }
                            }
                            
                            MA_Spacer(Modifier.padding(8.dp))
                        }
                    }
                }
                
                // Paginación
                IconButton(
                    onClick = { if (paginaActual > 0) paginaActual-- },
                    enabled = paginaActual > 0
                ) { 
                    Icon(Icons.Default.NavigateBefore, contentDescription = "Anterior") 
                }
                
                val registroInicio = if (listaFiltrada.isEmpty()) 0 else (paginaActual * elementos) + 1
                val registroFin = minOf((paginaActual + 1) * elementos, listaFiltrada.size)
                val totalRegistros = listaFiltrada.size
                
                Text(
                    text = "$registroInicio a $registroFin de $totalRegistros",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                IconButton(
                    onClick = { if (paginaActual < totalPaginas - 1) paginaActual++ },
                    enabled = paginaActual < totalPaginas - 1
                ) { 
                    Icon(Icons.Default.NavigateNext, contentDescription = "Siguiente") 
                }
            }

            // Botón de exportar - Parte derecha
            Row(modifier = Modifier, horizontalArrangement = Arrangement.End) {
                TextButton(
                        onClick = {
                            val scope = CoroutineScope(Dispatchers.IO)

                            scope.launch {
                                val nombreFichero =
                                        if3(
                                                panelConfiguracion.titulo.isNullOrEmpty(),
                                                "Metricas",
                                                panelConfiguracion.titulo
                                        )

                                val appSpecificDir: File? =
                                        App.Companion.context.getExternalFilesDir(null)
                                val myExcelFile = File(appSpecificDir, "$nombreFichero.xls")

                                GenerateExcel()
                                        .generate(
                                                titulo = nombreFichero,
                                                filas = filasOriginal.filter { it.visible },
                                                fichero = myExcelFile
                                        )

                                d.informacion(_t(str = R.string.datos_exportados)) {
                                    val uri =
                                            FileProvider.getUriForFile(
                                                    context,
                                                    "${context.packageName}.provider",
                                                    myExcelFile
                                            )

                                    // 2. Prepara el Intent para ABRIR (VIEW) el archivo
                                    val viewIntent =
                                            Intent(Intent.ACTION_VIEW).apply {
                                                // Este es el tipo MIME para .xlsx. Usa
                                                // "application/vnd.ms-excel" para .xls
                                                setDataAndType(
                                                        uri,
                                                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                                                )
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }

                                    // 3. Usa el bloque try-catch para manejar el error
                                    try {
                                        // Intenta iniciar la actividad para abrir el archivo
                                        context.startActivity(viewIntent)
                                    } catch (e: ActivityNotFoundException) {
                                        // Si falla, entra aquí
                                        /*Toast.makeText(
                                        	context,
                                        	"No se encontró una app para abrir el archivo. Intenta compartirlo.",
                                        	Toast.LENGTH_LONG
                                        ).show()*/

                                        // 4. Crea y lanza el Intent para COMPARTIR (SEND)
                                        val shareIntent =
                                                Intent(Intent.ACTION_SEND).apply {
                                                    type =
                                                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                                                    putExtra(Intent.EXTRA_STREAM, uri)
                                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                }

                                        // Usamos createChooser para que siempre muestre el diálogo
                                        // de selección de app
                                        val chooser =
                                                Intent.createChooser(
                                                        shareIntent,
                                                        "Compartir archivo con..."
                                                )
                                        context.startActivity(chooser)
                                    }
                                }
                            }
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF2E7D32)),
                        contentPadding =
                                androidx.compose.foundation.layout.PaddingValues(
                                        horizontal = 12.dp,
                                        vertical = 0.dp
                                ),
                        modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                            Icons.Default.FileDownload,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        // Complejo de la tabla
        Column(
                modifierColumn,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
        ) {

            // Titulos de la tabla
            Row(modifier = Modifier.padding(4.dp).fillMaxWidth()) {
                if (mostrarTitulos && !filas.isEmpty()) {
                    filas.first().celdas.forEachIndexed { int, celda ->

                        // var modifierBox: Modifier = Modifier
                        var modifierBox: Modifier = Modifier.Companion

                        if (ajustarContenidoAncho) {
                            modifierBox = modifierBox.fillMaxWidth().weight(1f)
                        } else {
                            // modifierBox = modifierBox.width(celda.size)
                            modifierBox = modifierBox.width(celda.size)
                        }

                        if (celda.titulo.equals(K.HASH_CODE)) {
                            // No pintamos titulo para el hashcode
                        } else {

                            Box(modifier = modifierBox.background(Color(0xFFF5F5F5))) {
                                celda.celdaTitulo(modifierBox)
                            }
                        }
                    }
                }
            }
            MA_Lista(filas) { fila ->
                MA_FilaTablaDatos(fila, notas, panelConfiguracion) { fila ->
                    onClickSeleccionarFila(fila)
                }
            }




        }
        
        // Pagination Controls - Contador de registros
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,

            ) {
            IconButton(
                onClick = { if (paginaActual > 0) paginaActual-- },
                enabled = paginaActual > 0
            ) { Icon(Icons.Default.NavigateBefore, contentDescription = "Anterior") }
            
            val registroInicio = if (listaFiltrada.isEmpty()) 0 else (paginaActual * elementos) + 1
            val registroFin = minOf((paginaActual + 1) * elementos, listaFiltrada.size)
            val totalRegistros = listaFiltrada.size
            
            Text(
                text = "$registroInicio a $registroFin de $totalRegistros",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            
            IconButton(
                onClick = { if (paginaActual < totalPaginas - 1) paginaActual++ },
                enabled = paginaActual < totalPaginas - 1
            ) { Icon(Icons.Default.NavigateNext, contentDescription = "Siguiente") }
        }




    }
}
