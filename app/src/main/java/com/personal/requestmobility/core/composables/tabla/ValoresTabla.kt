package com.personal.requestmobility.core.composables.tabla

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.componentes.MA_Colores
import com.personal.requestmobility.core.composables.graficas.ValoresGrafica
import com.personal.requestmobility.core.composables.graficas.ElementoGrafica
import com.personal.requestmobility.core.utils.esNumerico
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion
import kotlin.collections.plus

data class Columnas(val nombre: String, val posicion: Int)
data class ValoresTabla(
    //var titulos: List<Header> = emptyList<Header>(),
    var filas: List<Fila> = emptyList<Fila>(),

    ) {

    fun dameColumnaPosicion(posicion: Int): Columnas {
        val todasColumnas: List<Columnas> = dameColumnas()
        todasColumnas.forEach { columna ->
            if (columna.posicion == posicion) {
                return columna
            }
        }
        return todasColumnas.last()

    }

    fun dameColumnas(): List<Columnas> {
        var columnas: List<Columnas> = emptyList()
        filas.first().celdas.forEachIndexed { index, celda ->

            columnas = columnas.plus(Columnas(celda.titulo, index))
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

    fun tieneContenido(): Boolean = (filas.isNotEmpty())

    fun dameElementosOrdenados(campoOrdenacionTabla: Int = 1): List<Fila> {
        if (filas.isEmpty()) return listOf<Fila>()
        val orden = if (campoOrdenacionTabla >= filas.first().celdas.size) 0 else campoOrdenacionTabla

        val fs = filas.filter { it.obtenidaDesdeKPI }.sortedByDescending { it.celdas[orden].valor.toFloat() } +
                filas.filter { !it.obtenidaDesdeKPI }
        return fs
    }


    fun dameElementosTruncados(panelConfiguracion: PanelConfiguracion, limite: Int, agrupar: Boolean, indiceCampoSumar: Int = 1): List<Fila> {


        if (limite == 0) return filas
        val elementosHastaLimite = filas.take(limite)
        if (!agrupar) return elementosHastaLimite
        if (filas.size > limite) {
            val elemetosDespuesLimite = filas.drop(limite)
            val fila0 = filas.first()
            val campoSuma = if (fila0.celdas.size < indiceCampoSumar) fila0.celdas.size - 1 else indiceCampoSumar
            var totalResto: Float = 0f

            try {
                totalResto = elemetosDespuesLimite.sumOf { fila -> fila.celdas[campoSuma].valor.toDouble() }.toFloat()
            } catch (e: Exception) {
                totalResto = 0f
            }

            //se decalran los mismos titulos que en el resto de filas, estos titulos son por los queluego se filtra...
            val tituloResto = fila0.celdas[0].titulo
            val tituloAgrupar = fila0.celdas[campoSuma].titulo

            val celdaRestoTexto = Celda(valor = "Resto", titulo = tituloResto, seleccionada = false)
            val celdasRestoValor = Celda(valor = totalResto.toString(), titulo = tituloAgrupar, seleccionada = false)
            val celdaVacia = Celda(valor = "", titulo = "", seleccionada = false)

            var filaResultado: List<Celda> = emptyList()
            filas.first().celdas.forEachIndexed { index, celda ->

                when (index) {
                    panelConfiguracion.columnaX -> filaResultado = filaResultado.plus(celdaRestoTexto)
                    panelConfiguracion.columnaY -> filaResultado = filaResultado.plus(celdasRestoValor)
                    else -> filaResultado = filaResultado.plus(celdaVacia)
                }


            }

            val filaResto = Fila(celdas = filaResultado, color = MA_Colores.listaColoresDefecto.last(), obtenidaDesdeKPI = false)
            return (elementosHastaLimite + filaResto)
        } else {
            return elementosHastaLimite
        }
    }

}


data class Fila(
    val celdas: List<Celda> = emptyList<Celda>(),
    val size: Dp = 200.dp,
    val color: Color = Color.White,
    val seleccionada: Boolean = false,
    val visible: Boolean = true,
    val obtenidaDesdeKPI : Boolean= true
    )

data class Celda(val valor: String,
                 val size: Dp = 200.dp,
                 val colorCelda: Color = Color.Blue,
                 val fondoCelda: Color = Color.White,
                 val contenido: @Composable (Modifier) -> Unit = { modifier ->
                     MA_LabelCelda(
                         modifier = modifier, valor = valor,/* color = colorCelda,*/
                         alineacion = if3(valor.esNumerico(), TextAlign.End, TextAlign.Start)
                     )
                 },
                 val titulo: String,
                 val colorTitulo: Color = Color.White,
                 val fondoTitulo: Color = Color.DarkGray,
                 val celdaTitulo: @Composable (Modifier) -> Unit = { modifierTitulo ->
                     MA_LabelCeldaTitulo(valor = titulo, color = colorTitulo, fondo = fondoTitulo)
                 },

                 val seleccionada: Boolean = false,
                 val filtroInvertido: Boolean = false
)

fun ValoresTabla.toValoresGrafica(columnaTexto: Int, columnaValor: Int): ValoresGrafica {

    var elementos: List<ElementoGrafica> = filas.filter { it.visible }.map { fila ->

        var valorTexto = fila.celdas[columnaTexto].valor
        var valor: Float = fila.celdas[columnaValor].valor.toFloat()
        val color = MA_Colores.obtenerColorAleatorio()
        ElementoGrafica(x = valorTexto, y = valor, leyenda = valorTexto, color = color)

    }

    // filasColor = false

    return ValoresGrafica(elementos = elementos, textoX = "Tipo", textoY = "Valor")
}