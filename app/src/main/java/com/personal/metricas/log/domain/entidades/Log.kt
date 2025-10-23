package com.personal.metricas.log.domain.entidades

import com.personal.metricas.sincronizacion.domain.interactors.TIPO_SINCRONIZACION

data class Log (val id: Int, val organization_code: String, val hora: String , val tipo: TIPO_SINCRONIZACION, val trx: Int )