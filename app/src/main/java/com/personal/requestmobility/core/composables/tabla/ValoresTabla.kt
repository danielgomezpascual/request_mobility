package com.personal.requestmobility.core.composables.tabla

import android.opengl.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.componentes.Colores
import com.personal.requestmobility.core.composables.graficas.ValoresGrafica
import com.personal.requestmobility.core.composables.graficas.ElementoGrafica
import com.personal.requestmobility.core.utils.esNumerico
import com.personal.requestmobility.core.utils.if3
import kotlin.collections.plus


data class ValoresTabla(
    //var titulos: List<Header> = emptyList<Header>(),
    var filas: List<Fila> = emptyList<Fila>(),
    var ajustarContenidoAncho: Boolean = true,
    var indicadorColor: Boolean = true,
    var filasColor: Boolean = true
) {
    companion object {

        fun fromValoresGrafico(data: ValoresGrafica, indicadorColor: Boolean = true, filasColor: Boolean = false): ValoresTabla {


            //  var titulos: List<Header> = emptyList<Header>()
            var filas: List<Fila> = emptyList<Fila>()

            //cabecera, titulo de columna
            /*  titulos = titulos.plus(Header(data.textoX))
              titulos = titulos.plus(Header(data.textoY))*/

            //datos
            data.elementos.forEach { d ->

                val celdaX: Celda = Celda(valor = d.x.toString(), titulo = d.leyenda)
                val celdaY: Celda = Celda(valor = d.x.toString(), titulo = d.leyenda)
                val fila: Fila = Fila(celdas = listOf(celdaX, celdaY), color = d.color)
                filas = filas.plus(fila)
            }

            return ValoresTabla(/*titulos = titulos,*/ filas = filas, indicadorColor = indicadorColor, filasColor = filasColor)
        }
    }

    fun tieneContenido(): Boolean = (filas.isNotEmpty())

    fun dameElementosOrdenados(campoOrdenacionTabla: Int = 1) = filas.sortedByDescending { it.celdas[campoOrdenacionTabla].valor.toFloat() }

    fun dameElementosTruncados(limite: Int, agrupar: Boolean, indiceCampoSumar: Int = 1): List<Fila> {
        if (limite == 0) return filas
        val elementosHastaLimite = filas.take(limite)
        if (!agrupar) return elementosHastaLimite
        if (filas.size > limite) {
            val elemetosDespuesLimite = filas.drop(limite)
            //val totalResto = elemetosDespuesLimite.sumOf { it.valor[indiceCampoSumar].toDouble() }.toFloat() // Realizamos la suma en Double y luego convertimos a Float
            val totalResto = elemetosDespuesLimite.sumOf { fila -> fila.celdas[indiceCampoSumar].valor.toDouble() }.toFloat()
            val celdaRestoTexto = Celda(valor = "Resto", titulo = "Agrupacion")
            val celdasRestoValor = Celda(valor = totalResto.toString(), titulo = "Total")

            val valorResto = Fila(celdas = listOf(celdaRestoTexto, celdasRestoValor), color = Colores.obtenerColorAleatorio())
            return (elementosHastaLimite + valorResto)
        } else {
            return elementosHastaLimite
        }
    }

}


data class Fila(val celdas: List<Celda> = emptyList<Celda>(),
                val size: Dp = 200.dp,
                val color: Color = Color.Black,
                val seleccionada: Boolean = false,
                val visible: Boolean = true)

data class Celda(val valor: String,
                 val size: Dp = 200.dp,
                 val colorCelda: Color = Color.Black,
                 val fondoCelda: Color = Color.White,
                 val contenido: @Composable (Modifier) -> Unit = { modifier ->
                     LabelCelda(modifier = modifier, valor = valor,/* color = colorCelda,*/
                         alineacion = if3(valor.esNumerico(), TextAlign.End, TextAlign.Start))
                 },
                 val titulo: String,
                 val colorTitulo: Color = Color.White,
                 val fondoTitulo: Color = Color.DarkGray,
                 val celdaTitulo: @Composable (Modifier) -> Unit = { modifierTitulo ->
                     LabelCeldaTitulo(valor = titulo, color = colorTitulo, fondo = fondoTitulo)
                 },

                val seleccionada : Boolean = false
)

fun ValoresTabla.toValoresGrafica(columnaTexto: Int, columnaValor: Int): ValoresGrafica {

    var elementos: List<ElementoGrafica> = filas.filter { it.visible }.map { fila ->

        var valorTexto = fila.celdas[columnaTexto].valor
        var valor: Float = fila.celdas[columnaValor].valor.toFloat()
        val color = Colores.obtenerColorAleatorio()
        ElementoGrafica(x = valorTexto, y = valor, leyenda = valorTexto, color = color)

    }

    filasColor = false

    return ValoresGrafica(elementos = elementos, textoX = "Tipo", textoY = "Valor")
}