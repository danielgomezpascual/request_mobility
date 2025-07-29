package com.personal.metricas.kpi.ui.screen.detalle


import MA_IconBottom
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.checks.MA_CheckBoxNormal
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextoEditable
import com.personal.metricas.core.composables.edittext.MA_TextoNormal
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelLeyenda
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.composables.labels.MA_Titulo2
import com.personal.metricas.core.composables.modales.MA_BottomSheet
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.composables.tabla.MA_Tabla
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.kpi.ui.screen.detalle.DetalleKpiVM.UIState
import com.personal.metricas.menu.Features
import com.personal.metricas.transacciones.domain.entidades.ResultadoSQL
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.min


@Composable
fun DetalleKpiScreen(
	identificador: Int,
	viewModel: DetalleKpiVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.onEvent(DetalleKpiVM.Eventos.Cargar(identificador))
	}


	when (uiState) {
		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).mensaje)
		UIState.Loading    -> LoadingScreen()
		is UIState.Success -> SuccessScreenDetalleKpi(
			viewModel,
			(uiState as UIState.Success),
			navegacion
		)

	}


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreenDetalleKpi(
	viewModel: DetalleKpiVM,
	uiState: UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {

	val kpiUI = uiState.kpiUI
	val sheetParametros = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	val scope = rememberCoroutineScope() // Se mantiene dentro del componente

	MA_ScaffoldGenerico(
		tituloScreen = TituloScreen.Kpi,
		navegacion = navegacion,
		accionesSuperiores = {
			Row(modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.Top) {


				MA_IconBottom(icon = Features.Paneles().icono, color = Features.Paneles().color) { viewModel.onEvent(DetalleKpiVM.Eventos.CrearPanel(navegacion)) }
				MA_IconBottom(icon = Features.Duplicar().icono, color = Features.Duplicar().color) { viewModel.onEvent(DetalleKpiVM.Eventos.DuplicarKpi(navegacion)) }
				MA_Spacer()
				MA_IconBottom(icon = Features.Eliminar().icono, color = Features.Eliminar().color) { viewModel.onEvent(DetalleKpiVM.Eventos.Eliminar(navegacion)) }
				MA_IconBottom(icon = Features.Guardar().icono, color = Features.Guardar().color) { viewModel.onEvent(DetalleKpiVM.Eventos.Guardar(navegacion)) }
			}
		},
		contenido = {
			val scrollState = rememberScrollState() // 1. Recuerda el estado del scroll
			Column(modifier = Modifier.verticalScroll(scrollState)) {


				Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
					MA_Avatar(kpiUI.titulo)
					MA_Titulo(kpiUI.titulo)
				}

				val visible: Boolean = false

				if (visible) {
					MA_TextoNormal(valor = kpiUI.id.toString(), titulo = "ID", onValueChange = { valor -> null })
				}

				MA_Titulo2("Definicion")
				MA_Card {

					Column() {


						MA_TextoNormal(valor = kpiUI.titulo, titulo = "Nombre", onValueChange = { valor ->
							viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeTitulo(valor))
						})

						MA_TextoNormal(valor = kpiUI.descripcion, titulo = "Descripcion", onValueChange = { valor ->
							viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeDescripcion(valor))
						})

						var textValue by remember { mutableStateOf("") }
						var currentWord by remember { mutableStateOf("") }

						Box(modifier = Modifier.fillMaxWidth()) {
							Column {

								MA_TextoNormal(valor = kpiUI.sql, titulo = "SQL", onValueChange = { newText ->
									viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeSQL(newText))
									val oldText = textValue
									// Actualizamos el estado para que la UI se redibuje
									textValue = newText
									// 1. Deducimos la posición del cambio
									val changePosition = findChangeIndex(oldText, newText)
									// 2. Usamos la misma lógica de antes con la posición deducida
									currentWord = findWordAtIndex(newText, changePosition)
									viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeAutocompletarSQL(currentWord))


								})


								Row(modifier = Modifier.fillMaxWidth()	.horizontalScroll(rememberScrollState())){

									uiState.ocurrenciasSQL.forEach { palabraSugerida->
										Box(modifier = Modifier.padding(5.dp).background(color = Color(222, 222, 222, 100)).clickable(enabled = true, onClick = {
											// --- INICIO DE LA LÓGICA CLAVE ---

											// 1. Obtenemos el texto completo actual.
											val textoCompleto = kpiUI.sql

											// 2. Creamos el nuevo texto reemplazando la palabra que estabas
											//    escribiendo (`currentWord`) por la que has seleccionado (`palabraSugerida`).
											//    Usamos replaceLastWord para evitar reemplazar ocurrencias anteriores.
											val nuevoTexto = replaceLastWord(textoCompleto, currentWord, palabraSugerida)

											// 3. Notificamos al ViewModel del cambio, como si el usuario
											//    lo hubiera escrito todo.


											// 4. (Importante) Limpiamos las sugerencias para que desaparezcan.
											viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeAutocompletarSQL(""))



											viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeSQL(nuevoTexto))






											// --- FIN DE LA LÓGICA CLAVE ---
										})){
											MA_LabelNormal(valor = palabraSugerida)
										}

									}

								}


								MA_BotonSecundario(texto = "RUN  SQL") {
									viewModel.onEvent(DetalleKpiVM.Eventos.RunSQL)
								}


								if (!uiState.infoSQL.isEmpty()) {

									val color = if (uiState.estadoErrorSQL) Color.Red else Color.Black
									MA_LabelLeyenda(
										modifier = Modifier.fillMaxWidth(),
										alineacion = TextAlign.Center,
										valor = uiState.infoSQL,
										color = color)
								}
							}

						}


					}
				}

				MA_Titulo2("Parámetros")
				MA_IconBottom(icon = Icons.Default.PlusOne) {
					viewModel.onEvent(DetalleKpiVM.Eventos.NuevoParametro)
					scope.launch { sheetParametros.show() }
				}

				MA_Card {
					Column {
						kpiUI.parametros.ps.forEach { parametro ->
							Row(modifier = Modifier.clickable(enabled = true, onClick = {
								viewModel.onEvent(DetalleKpiVM.Eventos.SeleccionarParametro(parametro))
								scope.launch { sheetParametros.show() }

							})) {
								Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {


									MA_LabelNegrita(parametro.key)
									MA_Spacer()
									MA_LabelNormal(parametro.defecto)

									if (parametro.fijo) {
										Spacer(modifier = Modifier.padding(5.dp))
										MA_Icono(Icons.Default.PushPin, modifier = Modifier.size(15.dp))
									}

									Box(
										modifier = Modifier.clickable(onClick = {
											viewModel.onEvent(DetalleKpiVM.Eventos.EliminarParametro(parametro))

										})
									) {
										MA_Icono(Icons.Default.Delete, color = Color.Red)
									}


								}


							}

						}
					}
				}


				val filas = ResultadoSQL.fromSqlToTabla(kpiUI.sql, kpiUI.parametros).filas.take(10)
				MA_Titulo2("Resultado")
				MA_Card(Modifier.height(400.dp)) {
					Column() {
						MA_Tabla(filas = filas) { }
					}
				}
			}


			MA_BottomSheet(sheetParametros, onClose = {
				{ scope.launch { sheetParametros.hide() } }
			}, contenido = {
				Column {
					//	MA_BotonPrincipal("Cerrar") { scope.launch { sheetParametros.hide() } }
					MA_Titulo2(valor = "Definición de parámetros en el KPI")

					Column {

						MA_TextoEditable(valor = uiState.paramtrosSeleccionado.key,
										 titulo = "Key",
										 onValueChange = { viewModel.onEvent(DetalleKpiVM.Eventos.ModificarClaveParametrosSeleccionado(it)) }


						)

						/*MA_TextoEditable(valor = uiState.paramtrosSeleccionado.valor, titulo = "Valor") {
							viewModel.onEvent(DetalleKpiVM.Eventos.ModificarValorParametrosSeleccionado(it))
						}*/
						MA_TextoEditable(valor = uiState.paramtrosSeleccionado.defecto, titulo = "Valor Por Defecto") {
							viewModel.onEvent(DetalleKpiVM.Eventos.ModificarValorPorDefectoParametrosSeleccionado(it))
						}
						MA_CheckBoxNormal(valor = uiState.paramtrosSeleccionado.fijo, titulo = "Valor Fijo") {
							viewModel.onEvent(DetalleKpiVM.Eventos.ModificarValorFijoeParametrosSeleccionado(it))
						}

					}


					Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
						MA_BotonSecundario("Cancelar") {
							scope.launch { sheetParametros.hide() }
						}

						MA_BotonPrincipal("Guardar") {
							viewModel.onEvent(DetalleKpiVM.Eventos.GuardarParametrosSelecciconado)
							scope.launch { sheetParametros.hide() }
						}

					}


				}

			})

		}
	)


}


/**
 * Encuentra el índice del primer carácter que difiere entre dos cadenas.
 * Esto nos sirve como una aproximación de la posición del cursor.
 */
private fun findChangeIndex(oldText: String, newText: String): Int {
	val commonLength = min(oldText.length, newText.length)
	// Buscamos la primera diferencia en la parte común de ambos textos
	for (i in 0 until commonLength) {
		if (oldText[i] != newText[i]) {
			return i
		}
	}
	// Si la parte común es idéntica, el cambio está al final.
	// Esto cubre tanto añadir como borrar caracteres al final.
	return commonLength
}


/**
 * ESTA FUNCIÓN ES LA MISMA DE LA RESPUESTA ANTERIOR.
 * La incluimos aquí para que el ejemplo sea completo.
 *
 * Función de ayuda para encontrar la palabra en un índice específico de un texto.
 * @param text El texto completo.
 * @param cursorPosition El índice de la posición actual del cursor.
 * @return La palabra que se encuentra en esa posición.
 */
private fun findWordAtIndex(text: String, cursorPosition: Int): String {
	if (text.isEmpty() || cursorPosition < 0) {
		return ""
	}

	val correctedCursorPos = cursorPosition.coerceIn(0, text.length)

	if (correctedCursorPos > 0 && correctedCursorPos < text.length && text[correctedCursorPos].isWhitespace()) {
		return ""
	}
	if (correctedCursorPos > 0 && text[correctedCursorPos - 1].isWhitespace()) {
		return ""
	}

	val startIndex = text.lastIndexOf(char = ' ', startIndex = (correctedCursorPos - 1).coerceAtLeast(0)) + 1
	var endIndex = text.indexOf(char = ' ', startIndex = correctedCursorPos)
	if (endIndex == -1) {
		endIndex = text.length
	}
	if (startIndex > endIndex) {
		return ""
	}

	return text.substring(startIndex, endIndex)
}


fun replaceLastWord(texto: String, palabraBuscada: String, nuevaPalabra: String): String {
	val lastIndex = texto.lastIndexOf(palabraBuscada)
	if (lastIndex == -1) {
		return texto // La palabra no se encontró, no hacer nada
	}
	val start = texto.substring(0, lastIndex)
	val end = texto.substring(lastIndex + palabraBuscada.length)
	// Añadimos un espacio después de la palabra para una mejor experiencia de usuario
	return "$start$nuevaPalabra $end"
}