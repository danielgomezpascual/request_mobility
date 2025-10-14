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

	fun agrupasPorHoras(transacciones : List<Transacciones>): String{
		val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

		val horas = transacciones.mapNotNull { HorasTransacciones(it.mobRequestId, LocalDateTime.parse(it.creationDate, formatter)) }
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

	}

}