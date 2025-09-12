package com.personal.metricas.core.utils

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter


object TiempoHora {
	fun ahora(): String {
		val current = LocalDateTime.now() // Obtiene la fecha y hora actual
		val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Define el formato
		val formatted = current.format(formatter) // Formatea la fecha y hora
		return formatted
	}


	fun obtenerDiaSemana(): String {
		val current = LocalDateTime.now()
		val dayOfWeek = current.dayOfWeek

		return when (dayOfWeek) {
			DayOfWeek.MONDAY    -> "Lunes"
			DayOfWeek.TUESDAY   -> "Martes"
			DayOfWeek.WEDNESDAY -> "Miércoles"
			DayOfWeek.THURSDAY  -> "Jueves"
			DayOfWeek.FRIDAY    -> "Viernes"
			DayOfWeek.SATURDAY  -> "Sábado"
			DayOfWeek.SUNDAY    -> "Domingo"
			else                -> ""
		}
	}

	fun obtenerDiaDelMesNumerico(): Int {
		val current = LocalDateTime.now()
		return current.dayOfMonth // Retorna el día del mes como un número (1-31)
	}

	fun obtenerMesNumerico(): Int {
		val current = LocalDateTime.now()
		return current.monthValue // Retorna el mes como un número (1-12)
	}

	fun obtenerNombreDelMes(): String {
		val current = LocalDateTime.now()
		val month = current.month // Retorna el mes como un enum (Month.JANUARY, Month.FEBRUARY, etc.)

		return when (month) {
			Month.JANUARY   -> "Enero"
			Month.FEBRUARY  -> "Febrero"
			Month.MARCH     -> "Marzo"
			Month.APRIL     -> "Abril"
			Month.MAY       -> "Mayo"
			Month.JUNE      -> "Junio"
			Month.JULY      -> "Julio"
			Month.AUGUST    -> "Agosto"
			Month.SEPTEMBER -> "Septiembre"
			Month.OCTOBER   -> "Octubre"
			Month.NOVEMBER  -> "Noviembre"
			Month.DECEMBER  -> "Diciembre"
			else            -> ""
		}
	}

	fun obtenerAnio(): Int {
		val current = LocalDateTime.now()
		return current.year // Retorna el año como un número
	}
}