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
import kotlin.collections.plus
import kotlin.text.get


data class ValoresTabla(
    //var titulos: List<Header> = emptyList<Header>(),
    var filas: List<Fila> = emptyList<Fila>(),

) {


    fun tieneContenido(): Boolean = (filas.isNotEmpty())

    fun dameElementosOrdenados(campoOrdenacionTabla: Int = 1): List<Fila> {
        if (filas.isEmpty()) return listOf<Fila>()
        val orden = if (campoOrdenacionTabla >= filas.first().celdas.size) 0 else campoOrdenacionTabla
        return filas.sortedByDescending { it.celdas[orden].valor.toFloat() }
    }


    fun dameElementosTruncados(limite: Int, agrupar: Boolean, indiceCampoSumar: Int = 1): List<Fila> {


        if (limite == 0) return filas
        val elementosHastaLimite = filas.take(limite)
        if (!agrupar) return elementosHastaLimite
        if (filas.size > limite) {
            val elemetosDespuesLimite = filas.drop(limite)
            val fila0 = filas.first()
            val campoSuma = if (fila0.celdas.size < indiceCampoSumar) fila0.celdas.size-1 else indiceCampoSumar
            var totalResto : Float = 0f

            try {
                totalResto  = elemetosDespuesLimite.sumOf { fila -> fila.celdas[campoSuma].valor.toDouble() }.toFloat()
            }catch (e: Exception){
                totalResto = 0f
            }

            //se decalran los mismos titulos que en el resto de filas, estos titulos son por los queluego se filtra...
            val tituloResto = fila0.celdas[0].titulo
            val tituloAgrupar = fila0.celdas[campoSuma].titulo

            val celdaRestoTexto = Celda(valor = "Resto", titulo =tituloResto, seleccionada =  false)
            val celdasRestoValor = Celda(valor = totalResto.toString(), titulo = tituloAgrupar, seleccionada = false)

            val valorResto = Fila(celdas = listOf(celdaRestoTexto, celdasRestoValor), color = MA_Colores.obtenerColorAleatorio())
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
                val visible: Boolean = true,

    )

data class Celda(val valor: String,
                 val size: Dp = 200.dp,
                 val colorCelda: Color = Color.Black,
                 val fondoCelda: Color = Color.White,
                 val contenido: @Composable (Modifier) -> Unit = { modifier ->
                     MA_LabelCelda(modifier = modifier, valor = valor,/* color = colorCelda,*/
                         alineacion = if3(valor.esNumerico(), TextAlign.End, TextAlign.Start))
                 },
                 val titulo: String,
                 val colorTitulo: Color = Color.White,
                 val fondoTitulo: Color = Color.DarkGray,
                 val celdaTitulo: @Composable (Modifier) -> Unit = { modifierTitulo ->
                     MA_LabelCeldaTitulo(valor = titulo, color = colorTitulo, fondo = fondoTitulo)
                 },

                val seleccionada : Boolean = false,
                val filtroInvertido : Boolean = false
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