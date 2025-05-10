package com.personal.requestmobility.core.log.domain

import com.personal.requestmobility.core.utils.TiempoHora

data class Traza(
    val key: String = "",
    val tipo: TipoTraza = TipoTraza.DEBUG,
    val mensaje: String = "",
    val tiempo: String = TiempoHora.ahora(),
    val hilo: String = Thread.currentThread().name
)


