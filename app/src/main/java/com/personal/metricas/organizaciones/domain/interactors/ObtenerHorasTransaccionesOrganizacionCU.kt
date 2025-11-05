package com.personal.metricas.organizaciones.domain.interactors

import com.personal.metricas.App
import com.personal.metricas.core.composables.dialogos.AppGlobalDialogs
import com.personal.metricas.core.utils.if3
import com.personal.metricas.organizaciones.domain.entidades.HorasTransacciones
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones
import com.personal.metricas.transacciones.domain.entidades.Transacciones
import com.personal.metricas.transacciones.domain.repositorios.IRepoTransacciones
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

class ObtenerHorasTransaccionesOrganizacionCU(private val repoTransacciones: IRepoTransacciones) {

	suspend fun obtener(organizacion: Organizaciones) : String{
		val transaccionesPorOrganizacion = repoTransacciones.obtenerTransaccionesPorOrganizacion(organizacion.organizationCode)
		return agrupasPorHoras(transaccionesPorOrganizacion)
	}

	// Define los pesos para las transacciones
	 val WEIGHT_RECENT = 2
	 val WEIGHT_OLD = 1
	 val RECENT_WEEKS_CUTOFF = 1L

	fun agrupasPorHoras(transacciones: List<Transacciones>): String {
		val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

		// 1. Definimos la fecha de corte (hace una semana)
		val cutoffDate = LocalDateTime.now().minusWeeks(RECENT_WEEKS_CUTOFF)

		// 2. Agregamos la lógica de ponderación
		val horasPonderadas = transacciones.mapNotNull {
			// Asumiendo que HorasTransacciones existe y guarda el timestamp
			// Si falla el parseo, devolvemos null y mapNotNull lo descarta
			try {
				HorasTransacciones(it.mobRequestId, LocalDateTime.parse(it.creationDate, formatter))
			} catch (e: Exception) {
				null
			}
		}
			.groupBy { it.timestamp.hour }
			.mapValues { (_, transactionsInHour) ->
				// CAMBIO: En lugar de .size, usamos sumOf para la ponderación
				transactionsInHour.sumOf { transaccion ->
					if (transaccion.timestamp.isAfter(cutoffDate)) {
						WEIGHT_RECENT
					} else {
						WEIGHT_OLD
					}
				}
			}
			.entries.sortedByDescending { it.value } // Ordenamos por el peso total


		// 3. Lógica de grupos exclusivos
		val totalHorasDistintas = horasPonderadas.size
		if (totalHorasDistintas == 0) {
			return "" // No hay transacciones, devolvemos string vacío
		}

		// Calculamos los índices para cortar las listas
		val index10 = ceil(totalHorasDistintas * 0.10).toInt()
		val index25 = ceil(totalHorasDistintas * 0.25).toInt()
		val index50 = ceil(totalHorasDistintas * 0.50).toInt()

		// Usamos subList para obtener grupos exclusivos (evita solapamiento)
		// subList(fromIndex, toIndex) -> 'toIndex' es exclusivo
		val top10Group = horasPonderadas.subList(0, index10.coerceAtMost(totalHorasDistintas))
		val top25Group = horasPonderadas.subList(index10.coerceAtMost(totalHorasDistintas), index25.coerceAtMost(totalHorasDistintas))
		val top50Group = horasPonderadas.subList(index25.coerceAtMost(totalHorasDistintas), index50.coerceAtMost(totalHorasDistintas))

		// 4. Construcción del String (sin cambios, pero ahora usa los grupos correctos)
		// Usamos StringBuilder por eficiencia, recomendado por las guías de estilo.
		val strHoras = StringBuilder()

		// (Asumo que if3 es vuestro helper ternario: if3(condicion, siVerdadero, siFalso))
		// Nota: He cambiado los nombres de las variables de 'top10Percent' a 'top10Group'
		//       para reflejar que son listas de grupos, no solo el cálculo del porcentaje.

		top10Group.forEach { (hora, _) ->
			val h = if3((hora >= 23), 0, hora + 1)
			val horaFormateada = h.toString().padStart(2, '0')
			strHoras.append("$horaFormateada:00;")
		}

		top25Group.forEach { (hora, _) ->
			val h = if3((hora >= 23), 0, hora + 1)
			val horaFormateada = h.toString().padStart(2, '0')
			strHoras.append("$horaFormateada:30;")
		}

		top50Group.forEach { (hora, _) ->
			val h = if3((hora >= 23), 0, hora + 1)
			val horaFormateada = h.toString().padStart(2, '0')
			strHoras.append("$horaFormateada:00;")
		}

		return strHoras.toString()
	}
	/*fun agrupasPorHoras(transacciones : List<Transacciones>): String{
		val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

		val horas = transacciones.mapNotNull {
			HorasTransacciones(it.mobRequestId, LocalDateTime.parse(it.creationDate, formatter))
		}
			.groupBy { transacciones -> transacciones.timestamp.hour }
			.mapValues { (_, transactionsInHour) ->
				// Para cada grupo (cada hora), contamos cuántos elementos tiene la lista.
				transactionsInHour.size
			}
			.entries.sortedByDescending { it.value }



		//25% mas usadas -> van a tener peticiones dobles en las horas
		val top10Percent = horas.take(ceil(horas.size * 0.10).toInt())


		//25% mas usadas -> van a tener peticiones dobles en las horas
		val top25Percent = horas.take(ceil(horas.size * 0.25).toInt())

		//50% -> Peticion simple
		val top50Percent = horas.take(ceil(horas.size * 0.50).toInt())

		var strHoras : String =""

		top10Percent.forEach { (hora, _) ->
			val h  = if3((hora >=23) ,  0, hora +1 )
			var horaFormateada = h.toString().padStart(2, '0')
			strHoras += "$horaFormateada:00;"
		}

		top25Percent.forEach { (hora, _) ->
			val h  = if3((hora >=23) ,  0, hora +1 )
			var horaFormateada = h.toString().padStart(2, '0')

			strHoras += "$horaFormateada:30;"
		}

		top50Percent.forEach { (hora, _) ->
			val h  = if3((hora >=23) ,  0, hora +1 )
			var horaFormateada = h.toString().padStart(2, '0')

			strHoras += "$horaFormateada:00;"
		}


		return strHoras

	}*/

}