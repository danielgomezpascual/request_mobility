package com.personal.metricas.core.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TiempoHora {
    fun ahora() : String {
        val current = LocalDateTime.now() // Obtiene la fecha y hora actual
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Define el formato
        val formatted = current.format(formatter) // Formatea la fecha y hora

        return formatted
    }
}