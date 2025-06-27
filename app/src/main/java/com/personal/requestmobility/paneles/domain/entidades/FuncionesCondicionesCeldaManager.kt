package com.personal.requestmobility.paneles.domain.entidades

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.util.splitToIntList
import com.personal.requestmobility.App
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenAssets
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenDrawable
import com.personal.requestmobility.core.composables.tabla.Celda
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.MA_LabelCelda
import com.personal.requestmobility.paneles.ui.entidades.CondicionesCelda
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.truncate

class FuncionesCondicionesCeldaManager {

    fun get() = listOf<FuncionesCondicionCelda>(
        FuncionesCondicionCelda(0, "Ninguna", false, {}),
        FuncionesCondicionCelda(1, "Banderas", false, { MA_LabelCelda(valor = "Prueas") }),
        FuncionesCondicionCelda(2, "Estrellas", true, { MA_LabelCelda(valor = "Prueas") })
    )

    fun get(i: Int) = get()[i]


    fun aplicarCondicion(valor: String, condicion: CondicionesCelda, celda: Celda, filas: List<Fila> = emptyList<Fila>()): FuncionesCondicionCelda {

        return when (condicion.condicionCelda) {
            1 -> banderas(valor)
            2 -> estrellas(valor, condicion, filas)
            else -> FuncionesCondicionCelda(0, "Ninguna", true, composable = { celda.contenido })
        }

    }

    fun banderas(valor: String): FuncionesCondicionCelda {
        val r = FuncionesCondicionCelda(sobreTodoConjunto = true, composable = {
            Row() {
                MA_ImagenAssets("banderas/es.png", Modifier.width(20.dp))
                MA_LabelCelda(valor = valor)
            }
        })
        return r
    }

    fun estrellas(valor: String, condicion: CondicionesCelda, filas: List<Fila> = emptyList<Fila>()): FuncionesCondicionCelda {

        var listaValores: List<String> = emptyList()
        filas.forEach { fila ->
            // App.log.lista("Valore de fila ", fila.celdas)
            listaValores = listaValores.plus(fila.celdas[condicion.columna.posicion].valor)
        }
        try {
            listaValores = listaValores.sortedBy { it.toFloat() }
        } catch (e: Exception) {
            listaValores = listaValores.sortedBy { it }
        }

        App.log.lista("XXL Valores ", listaValores)


        val elementos = listOf<Int>(R.drawable.star_1, R.drawable.star_2, R.drawable.star_3, R.drawable.star_4, R.drawable.star_5)
       /* App.log.d("Elementos en los que va a romprer ${listaValores.size } / ${elementos.size} ")


        //val elementosPorParte = floor((listaValores.size / elementos.size).toFloat())
        val elementosPorParte =  elementos.size % listaValores.size

        App.log.d("elementosPorParte : ${elementosPorParte} ")



        val partes = listaValores.chunked(elementosPorParte.toInt())
        //val partes = listaValores.splitIntoParts(elementos.size)


        App.log.d("XXL Partes generadas ${partes.size} ")
*/
        val partes = listaValores.repartirEnOrden(elementos.size)

        var listaEncontrada: Int = 0
        partes.forEachIndexed { indice, lista ->
            App.log.lista("XXL $indice - $valor", lista)
            if (lista.contains(valor)) {
                listaEncontrada = indice
            }
        }


        val rd = elementos.get(listaEncontrada)
        /*val rd: Int = when (listaEncontrada) {
            0 -> R.drawable.star_1
            1 -> R.drawable.star_2
            2 -> R.drawable.star_3
            3 -> R.drawable.star_4
            4 -> R.drawable.star_5
            else -> R.drawable.star_3
        }*/

        val r = FuncionesCondicionCelda(sobreTodoConjunto = true, composable = {
            Row() {
                MA_ImagenDrawable(imagen = rd, s = 24.dp)
                MA_LabelCelda(valor = valor)
            }
        })
        return r

    }


}

/**
 * Reparte los elementos de una lista en un número específico de sublistas,
 * manteniendo el orden original de los elementos.
 * Si la división no es exacta, los elementos extra se distribuyen
 * entre las primeras sublistas.
 *
 * @param T El tipo de elementos en la lista.
 * @param numeroDeListas El número de sublistas a crear.
 * @return Una lista de listas con los elementos distribuidos en orden.
 */
fun <T> List<T>.repartirEnOrden(numeroDeListas: Int): List<List<T>> {
    // --- Validaciones iniciales ---
    if (numeroDeListas <= 0 || this.isEmpty()) {
        return emptyList()
    }

    // Si hay más listas que elementos, cada elemento va en su propia lista.
    if (numeroDeListas >= this.size) {
        return this.map { listOf(it) }
    }

    // --- Lógica principal de cálculo ---
    val tamanoTotal = this.size
    val tamanoBase = tamanoTotal / numeroDeListas // División entera (el tamaño mínimo de cada lista)
    val elementosExtra = tamanoTotal % numeroDeListas // Cuántos elementos "sobran"

    val resultado = mutableListOf<List<T>>()
    var punteroInicio = 0

    // Iteramos para crear cada una de las sublistas deseadas
    for (i in 0 until numeroDeListas) {
        // Las primeras `elementosExtra` listas tendrán un tamaño de `tamanoBase + 1`
        val tamanoSublista = if (i < elementosExtra) tamanoBase + 1 else tamanoBase

        // Calculamos el final del trozo
        val punteroFin = punteroInicio + tamanoSublista

        // Añadimos el "trozo" (sublista) de la lista original al resultado
        resultado.add(this.subList(punteroInicio, punteroFin))

        // Movemos el puntero de inicio para la siguiente iteración
        punteroInicio = punteroFin
    }

    return resultado
}