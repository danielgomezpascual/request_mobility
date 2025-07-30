package com.personal.metricas.core.composables.tabla

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.composables.listas.MA_Lista
import com.personal.metricas.core.utils.K
import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.notas.domain.interactors.GuardarNotaCU
import com.personal.metricas.notas.domain.interactors.ObtenerNotasCU
import com.personal.metricas.transacciones.ui.screens.composables.ModalInferiorFiltros
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.mp.KoinPlatform.getKoin


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
	filas: List<Fila>,
	notas: List<Notas> = emptyList<Notas>(),
	celdasFiltro: List<Celda> = emptyList<Celda>(),
	mostrarTitulos: Boolean = true,
	onClickSeleccionarFiltro: (Celda) -> Unit = {},
	onClickInvertir: (Celda) -> Unit = {},
	onClickSeleccionarFila: (Fila) -> Unit = {},
) {

	val estadoScroll = rememberScrollState()
	val indicadorColor = panelConfiguracion.indicadorColor
	val filasColor = panelConfiguracion.filasColor
	val ajustarContenidoAncho = panelConfiguracion.ajustarContenidoAncho
	var modifierColumn = modifier
	if (!ajustarContenidoAncho) {
		modifierColumn = modifierColumn.horizontalScroll(estadoScroll)
	}




	Column(modifierColumn) {
		if (celdasFiltro.isNotEmpty()) {
			ModalInferiorFiltros() {
				Column {
					MA_Titulo("Filtro")
					MA_Lista(celdasFiltro) { celdaFiltro ->
						MA_CeldaFiltro(celda = celdaFiltro, onClickSeleccion = { cf -> onClickSeleccionarFiltro(cf) }, onClickInvertir = { cf -> onClickInvertir(cf) })
					}
				}


			}
		}
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
						modifierBox = modifierBox.width(celda.size)
					}
					/*modifierBox = if (ajustarContenidoAncho) {
						modifierBox
                            .fillMaxWidth()
                            .weight(1f)
					} else {
						modifierBox.width(celda.size)
					}*/


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

