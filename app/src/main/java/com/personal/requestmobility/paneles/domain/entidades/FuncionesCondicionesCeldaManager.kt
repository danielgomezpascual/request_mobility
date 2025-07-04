package com.personal.requestmobility.paneles.domain.entidades

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenAssets
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenDrawable
import com.personal.requestmobility.core.composables.tabla.Columnas
import com.personal.requestmobility.core.composables.tabla.MA_LabelCelda
import kotlin.Int
import kotlin.collections.listOf

class FuncionesCondicionesCeldaManager {
	
	fun get() = listOf<FuncionesCondicionCelda>(FuncionesCondicionCelda(0,
			nombre = "Ninguna",
			sobreTodoConjunto = false,
			composable = {}),
			
			FuncionesCondicionCelda(1,
					nombre = "Banderas",
					sobreTodoConjunto = false,
					composable = { MA_LabelCelda(valor = "Prueas") }),
			
			FuncionesCondicionCelda(id = 2,
					nombre = "Estrellas",
					sobreTodoConjunto = true,
					representaciones = listOf<Int>(R.drawable.star_1,
							R.drawable.star_2,
							R.drawable.star_3,
							R.drawable.star_4,
							R.drawable.star_5),
					composable = { MA_LabelCelda(valor = "Prueas") }),
			
			
			FuncionesCondicionCelda(id = 3,
					nombre = "Máximo / Mínimo",
					sobreTodoConjunto = true,
					representaciones = listOf<Int>(R.drawable.star_1, R.drawable.star_5),
					composable = { MA_LabelCelda(valor = "Prueas") })
											   
											   
											   )
	
	
	fun get(i: Int) = get()[i]
	
	
	fun aplicarCondicion(valor: String, condicion: Condiciones, columna: Columnas): FuncionesCondicionCelda {
		
		return when (condicion.condicionCelda) {
			1 -> banderas(valor)
			2 -> iconosPorPartes(valor, condicion, columna)
			3 -> maximoMinimo(valor, condicion, columna)
			else -> FuncionesCondicionCelda(0, "Ninguna", true, composable = { })
		}
		
	}
	
	fun banderas(valor: String): FuncionesCondicionCelda {
		val paisIso = valor.substring(0,2).lowercase()
		val r = FuncionesCondicionCelda(sobreTodoConjunto = true, composable = {
			Row() {
				MA_ImagenAssets("banderas/$paisIso.png", Modifier.width(20.dp))
				MA_LabelCelda(valor = valor)
			}
		})
		return r
	}
	
	fun ordenarValoresColumna(columna: Columnas) = try {
		columna.valores.sortedBy { it.toFloat() }
	}
	catch (e: Exception) {
		columna.valores.sortedBy { it }
	}
	
	
	fun iconosPorPartes(
		valor: String,
		condicion: Condiciones,
		columna: Columnas,
					   ): FuncionesCondicionCelda {
		
		val representaciones = get(condicion.condicionCelda).representaciones
		
		val listaValores = ordenarValoresColumna(columna)
		val partes = listaValores.repartirEnOrden(representaciones.size)
		
		
		var listaEncontrada: Int = 0
		partes.forEachIndexed { indice, lista ->
			try {
				if (lista.map { it.toFloat() * 1.0f }
						.contains(valor.toFloat() * 1.0f)) listaEncontrada = indice
			}
			catch (e: Exception) {
				if (lista.map { it }.contains(valor)) listaEncontrada = indice
			}
		}
		
		
		val rd = representaciones.get(listaEncontrada)
		val r = FuncionesCondicionCelda(sobreTodoConjunto = true, composable = {
			Row() {
				MA_ImagenDrawable(imagen = rd, s = 24.dp)
				MA_LabelCelda(valor = valor)
			}
		})
		return r
		
	}
	
	
	fun maximoMinimo(
		valor: String,
		condicion: Condiciones,
		columna: Columnas,
					): FuncionesCondicionCelda {
		
		
		val representaciones = get(condicion.condicionCelda).representaciones
		
		val listaValores = ordenarValoresColumna(columna)
		
		
		val max = listaValores.map { it.toFloat() }.max()
		val min = listaValores.map { it.toFloat() }.min()
		
		
		val rd = when (valor.toFloat()) {
			max -> representaciones.get(1)
			min -> representaciones.get(0)
			else -> -1
		}
		
		if (rd > 0) {
			return FuncionesCondicionCelda(sobreTodoConjunto = true, composable = {
				Row() {
					MA_ImagenDrawable(imagen = rd, s = 24.dp)
					MA_LabelCelda(valor = valor)
				}
			})
			
		} else {
			return FuncionesCondicionCelda(sobreTodoConjunto = true, composable = {
				Row() {
					MA_LabelCelda(valor = valor)
				}
			})
		}
		
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
fun <T> List<T>.repartirEnOrden(numeroDeListas: Int): List<List<T>> { // --- Validaciones iniciales ---
	if (numeroDeListas <= 0 || this.isEmpty()) {
		return emptyList()
	}
	
	// Si hay más listas que elementos, cada elemento va en su propia lista.
	if (numeroDeListas >= this.size) {
		return this.map { listOf(it) }
	}
	
	// --- Lógica principal de cálculo ---
	val tamanoTotal = this.size
	val tamanoBase =
		tamanoTotal / numeroDeListas // División entera (el tamaño mínimo de cada lista)
	val elementosExtra = tamanoTotal % numeroDeListas // Cuántos elementos "sobran"
	
	val resultado = mutableListOf<List<T>>()
	var punteroInicio = 0
	
	// Iteramos para crear cada una de las sublistas deseadas
	for (i in 0 until numeroDeListas) { // Las primeras `elementosExtra` listas tendrán un tamaño de `tamanoBase + 1`
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