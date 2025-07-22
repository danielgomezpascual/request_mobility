package com.personal.metricas.core.log.domain

import com.personal.metricas.core.utils.TiempoHora

data class Traza(
    val key: String = "",
    val tipo: TipoTraza = TipoTraza.DEBUG,
    val mensaje: String = "",
    val tiempo: String = TiempoHora.ahora(),
    val hilo: String = Thread.currentThread().name
)


