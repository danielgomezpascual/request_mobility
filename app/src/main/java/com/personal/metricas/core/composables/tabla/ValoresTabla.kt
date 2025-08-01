package com.personal.metricas.core.composables.tabla

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.metricas.App
import com.personal.metricas.core.composables.componentes.MA_Colores
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils.Parametro
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils.esNumerico
import com.personal.metricas.core.utils.if3
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import kotlin.collections.plus

data class Columnas(val nombre: String, val posicion: Int, var valores: List<String> = emptyList())

data class ValoresTabla(
	//var titulos: List<Header> = emptyList<Header>(),
	var filas: List<Fila> = emptyList<Fila>(),
	var columnas: List<Columnas> = emptyList<Columnas>(),
) {

	fun addColumnaHash() {
		var valoresColumna: List<String> = emptyList()
		filas.forEach { fila ->

			val h = fila.hashCode()
			fila.celdas = fila.celdas.plus(Celda(valor = h.toString(), titulo = K.HASH_CODE))
			valoresColumna = valoresColumna.plus(h.toString())

		}
		val columnaHash = Columnas(nombre = K.HASH_CODE, columnas.size, valores = valoresColumna)
		columnas = columnas.plus(columnaHash)

	}

	fun dameColumnaPosicion(posicion: Int): Columnas {
		val todasColumnas: List<Columnas> = dameColumnas()
		if (todasColumnas.isEmpty()) return Columnas("Sin Definir", 0)
		todasColumnas.forEach { columna ->
			if (columna.posicion == posicion) {
				return columna
			}
		}
		return todasColumnas.last()

	}

	fun dameColumnas(): List<Columnas> {
		var columnas: List<Columnas> = emptyList()
		if (filas.isNotEmpty()) {
			filas.first().celdas.forEachIndexed { index, celda ->
				columnas = columnas.plus(Columnas(celda.titulo, index))
			}
		}
		return columnas
	}

	fun dameColumnasNumericas(): List<Columnas> {
		val todasColumnas: List<Columnas> = dameColumnas()
		var columnasNumericas: List<Columnas> = emptyList()

		todasColumnas.forEach { columna ->
			var numerica = true
			filas.filter { it.obtenidaDesdeKPI }.forEach { fila ->
				if (numerica) {
					if (!fila.celdas.get(columna.posicion).valor.esNumerico()) {
						numerica = false
					}
				}
			}
			if (numerica) {
				columnasNumericas = columnasNumericas.plus(columna)
			}
		}
		return columnasNumericas
	}

	fun dameElementosOrdenados(campoOrdenacionTabla: Int = 1): List<Fila> {
		if (filas.isEmpty()) return listOf<Fila>()
		val orden =
			if (campoOrdenacionTabla >= filas.first().celdas.size) 0 else campoOrdenacionTabla

		val fs = filas.filter { it.obtenidaDesdeKPI }
					 .sortedByDescending {						 						 it.celdas[orden].valor.toFloatOrNull() ?: 0f

					 } + filas.filter { !it.obtenidaDesdeKPI }
		return fs
	}

	fun dameElementosTruncados(panelConfiguracion: PanelConfiguracion): List<Fila> {


		val limite = panelConfiguracion.limiteElementos
		val agrupar = panelConfiguracion.agruparValores
		val indiceCampoSumar = panelConfiguracion.columnaY
		val incluirResto = panelConfiguracion.agruparResto




		if (limite == 0) return filas
		val elementosHastaLimite = filas.take(limite)
		if (!agrupar) return elementosHastaLimite
		if (!incluirResto) return elementosHastaLimite

		if (filas.size > limite) {
			val elemetosDespuesLimite = filas.drop(limite)
			val fila0 = filas.first()
			val campoSuma =
				if (fila0.celdas.size < indiceCampoSumar) fila0.celdas.size - 1 else indiceCampoSumar
			var totalResto: Float = 0f

			try {
				totalResto =
					elemetosDespuesLimite.sumOf { fila -> fila.celdas[campoSuma].valor.toDouble() }
						.toFloat()
			}
			catch (e: Exception) {
				totalResto = 0f
			}

			//se decalran los mismos titulos que en el resto de filas, estos titulos son por los queluego se filtra...
			val tituloResto = fila0.celdas[0].titulo
			val tituloAgrupar = fila0.celdas[campoSuma].titulo

			val celdaRestoTexto = Celda(valor = "Resto", titulo = tituloResto, seleccionada = false)
			val celdasRestoValor =
				Celda(valor = totalResto.toString(), titulo = tituloAgrupar, seleccionada = false)
			val celdaVacia = Celda(valor = "", titulo = "", seleccionada = false)

			var filaResultado: List<Celda> = emptyList()
			filas.first().celdas.forEachIndexed { index, celda ->

				when (index) {
					panelConfiguracion.columnaX -> filaResultado =
						filaResultado.plus(celdaRestoTexto)

					panelConfiguracion.columnaY -> filaResultado =
						filaResultado.plus(celdasRestoValor)

					else                        -> filaResultado = filaResultado.plus(celdaVacia)
				}
			}

			val filaResto = Fila(celdas = filaResultado,
								 color = MA_Colores.listaColoresDefecto.last(),
								 obtenidaDesdeKPI = false)
			return (elementosHastaLimite + filaResto)
		} else {
			return elementosHastaLimite
		}
	}

}


data class Fila(
	var celdas: List<Celda> = emptyList<Celda>(), val size: Dp = 200.dp,
	val color: Color = Color.White, val seleccionada: Boolean = false,
	val visible: Boolean = true, val obtenidaDesdeKPI: Boolean = true,

	) {
	fun toParametros() = Parametros(ps = this.celdas.map { celda -> Parametro(celda.titulo, celda.valor) })

}

data class Celda(
	val valor: String = "",
	val size: Dp = 200.dp,
	val colorCelda: Color = Color.Blue,
	val fondoCelda: Color = Color.White,
	val contenido: @Composable (Modifier) -> Unit = { modifier ->
		MA_LabelCelda(modifier = modifier, valor = valor,/* color = colorCelda,*/
					  alineacion = if3(valor.esNumerico(), TextAlign.End, TextAlign.Start))
	},
	val titulo: String = "", val colorTitulo: Color = Color.White,
	val fondoTitulo: Color = Color.DarkGray,
	val celdaTitulo: @Composable (Modifier) -> Unit = { modifierTitulo ->
		//MA_LabelCeldaTitulo(valor = titulo, color = colorTitulo, fondo = fondoTitulo)

		MA_LabelCelda(modifier = modifierTitulo, valor = titulo,/* color = colorCelda,*/
					  color = colorTitulo, fondo = fondoTitulo,
					  alineacion = if3(valor.esNumerico(), TextAlign.End, TextAlign.Start))
	},
	val seleccionada: Boolean = false,
	val filtroInvertido: Boolean = false,
)

