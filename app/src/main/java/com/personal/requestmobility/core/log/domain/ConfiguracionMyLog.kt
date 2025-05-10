package com.personal.requestmobility.core.log.domain

data class ConfiguracionMyLog (
    val TAG : String = "MX",
    val mostrarNombreHilo: Boolean = true,
    val mostrarInfoCodigoFuente: Boolean = true,
    val trazaInformacionStackTrace : Int = 5

)