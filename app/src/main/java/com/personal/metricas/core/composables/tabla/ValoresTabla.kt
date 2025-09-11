package com.personal.metricas.core.composables.tabla

import android.graphics.Typeface
import android.util.TypedValue
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.App
import com.personal.metricas.App.Companion.context
import com.personal.metricas.core.composables.componentes.MA_Colores
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils.esNumerico
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import kotlin.collections.plus

data class Columnas(val nombre: String, val posicion: Int, var maximaLongitudCaracteres : Int = -1 ,
					var maximaLongitudDp: Dp = 0.dp,
					var valorMaximo: String = "",
					var valores: List<String> = emptyList()){

	val CONVERSOR : Int = 12

	fun addValor(valor: String= ""): List<String>{

		//App.log.d("valor $valor")
		valores = valores.plus(valor)

		/*if (maximaLongitudCaracteres < 0){
			maximaLongitudCaracteres = nombre.length
			maximaLongitudDp = (maximaLongitudCaracteres * CONVERSOR ).dp
			valorMaximo = nombre
		}*/

		if (valor.length> maximaLongitudCaracteres){
			maximaLongitudCaracteres = valor.length
			maximaLongitudDp = (maximaLongitudCaracteres * CONVERSOR).dp
			valorMaximo = valor
		}


		if (maximaLongitudDp > 250.dp){
			maximaLongitudDp = 180.dp
		}

		if (maximaLongitudDp < 30.dp){
			maximaLongitudDp = 40.dp
		}

		if (posicion == 0){
			//a la primera coluimna le summos 20 ya que en la mayoruia de las ocasiones lelva el idnicador
			maximaLongitudDp = maximaLongitudDp +30.dp
		}
		return valores
	}
}

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
					 .sortedByDescending {
						 it.celdas[orden].valor.toFloatOrNull() ?: 0f

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


