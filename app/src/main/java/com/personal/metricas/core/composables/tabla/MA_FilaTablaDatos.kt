package com.personal.metricas.core.composables.tabla

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.metricas.App
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.composables.dialogos.DialogosResultado
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils.esNumerico
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.core.utils.if3
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.notas.domain.interactors.GuardarNotaCU
import com.personal.metricas.notas.domain.interactors.ObtenerNotasCU
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.koin.androidx.compose.get
import org.koin.core.context.GlobalContext.get
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun MA_FilaTablaDatos(fila: Fila, notas: List<Notas>, configuracion: PanelConfiguracion, onClick: (Fila) -> Unit) {
	val modifier = Modifier.Companion
	val filasColor = configuracion.filasColor
	val ajustarContenidoAncho = configuracion.ajustarContenidoAncho
	val indicadorColor = configuracion.indicadorColor

	val color: Color = (fila.color).copy(alpha = 0.6f)
	val colorFondo = if3(filasColor, (fila.color).copy(alpha = 0.1f), Color.White)




	Row(
		modifier = modifier
			.background(if3(fila.seleccionada, color, colorFondo))
			.fillMaxWidth()
			.fillMaxHeight()
			.clickable {
				onClick(fila)
			},
		verticalAlignment = Alignment.Companion.CenterVertically
	) {


		//val notasManager = getKoin().get<NotasManager>()
		val notasManager = NotasManager.instancia()
		val dialog = notasManager.dialog
		val scope = notasManager.scope

		val nota: Notas? = notas.firstOrNull { it.hash.equals(fila.celdas.last().valor) }
		var an: String = if (nota == null) "" else nota!!.descripcion


		var anotacion by remember { mutableStateOf(an) }

		fila.celdas.forEachIndexed { indice, celda ->

			var modifierFila: Modifier = Modifier.Companion

			if (ajustarContenidoAncho) {
				modifierFila = modifierFila
					.fillMaxWidth()
					.weight(1f)
			} else {
				modifierFila = modifierFila.width(fila.size)
			}



			if (celda.titulo.equals(K.HASH_CODE)) {

				if (configuracion.permiteNotas) {
					Row(Modifier.clickable(enabled = true, onClick = {


						dialog.nota(texto = "${celda.valor}".toString(),
									 textoInicial = anotacion) { dialogosResultado, mensaje ->

							if (dialogosResultado == DialogosResultado.Si) {
								scope.launch {
									async {
										val nota = Notas(hash = celda.valor,
														 descripcion = mensaje.toString())
										notasManager.almacenarNota(nota)

									}.await()
									dialog.informacion("Nota almacenada") {

										anotacion = mensaje.toString()
									}
								}
							}


						}
					})) {
						MA_Icono(icono = Icons.Default.LightMode,
								 color = if3(anotacion.isEmpty(),
											 Color.LightGray,
											 Color(239, 111, 6, 100)),
								 modifier = Modifier.size(14.dp))
					}
				}
			} else {
				if (indicadorColor && indice == 0) {
					MA_Indicador(modifierFila, fila.color) {
						celda.contenido(modifierFila)
					}
				} else {
					celda.contenido(modifierFila)
				}
			}


		}

	}
	HorizontalDivider()

}