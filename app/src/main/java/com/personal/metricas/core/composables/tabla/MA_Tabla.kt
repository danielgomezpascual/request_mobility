package com.personal.metricas.core.composables.tabla

import MA_IconBottom
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.TableRows
import androidx.compose.material.icons.filled.TableView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.core.content.FileProvider
import com.personal.metricas.App
import com.personal.metricas.App.Companion.dialog
import com.personal.metricas.R
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonSecundarioSinBorde
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.composables.edittext.MA_TextoEditable
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.composables.listas.MA_Lista
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils._t
import com.personal.metricas.core.utils.if3
import com.personal.metricas.excel.GenerateExcel
import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.transacciones.ui.screens.composables.ModalInferiorFiltros
import io.github.evanrupert.excelkt.workbook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.compose.getKoin
import java.io.File


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
) {/*
        Marco(modifier = modifier, titulo = titulo) {
            Tabla(modifier, tabla)
        }*/
}


@Composable
fun MA_Tabla(
	modifier: Modifier = Modifier,
	panelConfiguracion: PanelConfiguracion = PanelConfiguracion(ajustarContenidoAncho = false, indicadorColor = false, filasColor = false

	),
	filasOriginal: List<Fila>,
	notas: List<Notas> = emptyList<Notas>(),
	celdasFiltro: List<Celda> = emptyList<Celda>(),
	mostrarTitulos: Boolean = true,
	indice: Int = 0,
	elementos: Int = 1000,
	onClickSeleccionarFiltro: (Celda) -> Unit = {},
	onClickInvertir: (Celda) -> Unit = {},
	onClickSeleccionarFila: (Fila) -> Unit = {},
	onClickFiltrarTexto: (String) -> Unit = {},
	onClickBorrarFiltros: () -> Unit,
) {

	val estadoScroll = rememberScrollState()
	val indicadorColor = panelConfiguracion.indicadorColor
	val filasColor = panelConfiguracion.filasColor
	val ajustarContenidoAncho = panelConfiguracion.ajustarContenidoAncho
	var modifierColumn = modifier
	if (!ajustarContenidoAncho) {
		modifierColumn = modifierColumn.horizontalScroll(estadoScroll)
	}


	val filas = filasOriginal.filter { it.visible }.drop(indice * elementos).take(elementos)
	val context = LocalContext.current
	val d: DialogManager = getKoin().get()

	Column(modifier = Modifier.fillMaxWidth()) {
		//Acciones
		Row(modifier = Modifier
			.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

			//MA_Icono(icono =  Icons.Default.TableRows, color = Color.DarkGray	 )
			MA_LabelMini(modifier = Modifier, alineacion = TextAlign.Start, valor = "${filasOriginal.filter { it.visible }.size} registros")
			


			Row(modifier = Modifier

				.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

				if (celdasFiltro.isNotEmpty()) {

					ModalInferiorFiltros() {
						var str by remember { mutableStateOf(App.sharedPrerfences.get(K.TXT_FILTROS_LISTAS, "")) }
						Column {


							MA_Titulo("Filtro")
							MA_BotonSecundarioSinBorde("Borrar", color = Color.Red) { onClickBorrarFiltros() }
							MA_TextoEditable(valor = str, titulo = "Buscar") { texto ->
								str = texto
								App.sharedPrerfences.put(K.TXT_FILTROS_LISTAS, str)
								onClickFiltrarTexto(str)
							}
							MA_Lista(celdasFiltro) { celdaFiltro ->
								MA_CeldaFiltro(celda = celdaFiltro,
											   onClickSeleccion = { cf -> onClickSeleccionarFiltro(cf) },
											   onClickInvertir = { cf -> onClickInvertir(cf) })
							}
						}


					}

				}
				MA_Spacer()
				MA_IconBottom(icon = Icons.Default.TableView, color = Color.DarkGray, onClick = {
					val scope = CoroutineScope(Dispatchers.IO)

					scope.launch {
						val nombreFichero = if3(panelConfiguracion.titulo.isNullOrEmpty(), "Metricas", panelConfiguracion.titulo)

						val appSpecificDir: File? = App.Companion.context.getExternalFilesDir(null)
						val myExcelFile = File(appSpecificDir, "$nombreFichero.xls")




						GenerateExcel().generate(titulo = nombreFichero,
												 filas = filasOriginal.filter { it.visible },
												 fichero = myExcelFile
						)

						d.informacion(_t(str = R.string.datos_exportados)) {

							val uri = FileProvider.getUriForFile(
								context,
								"${context.packageName}.provider",
								myExcelFile
							)

							// 2. Prepara el Intent para ABRIR (VIEW) el archivo
							val viewIntent = Intent(Intent.ACTION_VIEW).apply {
								// Este es el tipo MIME para .xlsx. Usa "application/vnd.ms-excel" para .xls
								setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
								addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
							}

							// 3. Usa el bloque try-catch para manejar el error
							try {
								// Intenta iniciar la actividad para abrir el archivo
								context.startActivity(viewIntent)
							}
							catch (e: ActivityNotFoundException) {
								// Si falla, entra aquí
								/*Toast.makeText(
									context,
									"No se encontró una app para abrir el archivo. Intenta compartirlo.",
									Toast.LENGTH_LONG
								).show()*/

								// 4. Crea y lanza el Intent para COMPARTIR (SEND)
								val shareIntent = Intent(Intent.ACTION_SEND).apply {
									type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
									putExtra(Intent.EXTRA_STREAM, uri)
									addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
								}

								// Usamos createChooser para que siempre muestre el diálogo de selección de app
								val chooser = Intent.createChooser(shareIntent, "Compartir archivo con...")
								context.startActivity(chooser)
							}
							/*
							//val file = createFileInCache(context, "mi_documento.txt", "Hola, Kotlin!")

							// Obtenemos la Uri a través del FileProvider.
							val uri = FileProvider.getUriForFile(
								context,
								"${context.packageName}.provider", // Debe coincidir con 'authorities' en el manifest.
								myExcelFile
							)

							// Creamos el Intent para ver el fichero.
							val intent = Intent(Intent.ACTION_VIEW).apply {
								//setDataAndType(uri, "text/plain") // ¡Importante! Especifica el tipo MIME.
								setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") // ¡Importante! Especifica el tipo MIME.
								addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
							}

							// Lanzamos el Intent.
							context.startActivity(intent)*/
						}
					}
				})


			}


		}
		//Complejo de la tabla
		Column(modifierColumn, verticalArrangement = Arrangement.Center,
			   horizontalAlignment = Alignment.Start) {
			//Titulos de la tabla
			Row(modifier = Modifier
				.padding(4.dp)
				.fillMaxWidth()) {
				if (mostrarTitulos && !filas.isEmpty()) {
					filas.first().celdas.forEachIndexed { int, celda ->

						//var modifierBox: Modifier = Modifier
						var modifierBox: Modifier = Modifier.Companion

						if (ajustarContenidoAncho) {
							modifierBox = modifierBox
								.fillMaxWidth()
								.weight(1f)
						} else {
							//modifierBox = modifierBox.width(celda.size)
							modifierBox = modifierBox.width(celda.size)
						}




						if (celda.titulo.equals(K.HASH_CODE)) {
							//No pintamos titulo para el hashcode
						} else {

							Box(modifier = modifierBox.background(Color.Gray)) {
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

	}




}

