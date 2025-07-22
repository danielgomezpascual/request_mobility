package com.personal.metricas.core.log.domain

data class ConfiguracionMyLog (
    var TAG : String = "MX",
    var mostrarNombreHilo: Boolean = true,
    var mostrarInfoCodigoFuente: Boolean = true,
    var trazaInformacionStackTrace : Int =5,
    var trazaInformacionListasStackTrace : Int =7
    
)