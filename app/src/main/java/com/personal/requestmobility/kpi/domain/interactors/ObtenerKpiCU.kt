package com.personal.requestmobility.kpi.domain.interactors

import com.personal.requestmobility.kpi.domain.repositorios.KpisRepositorio

class ObtenerKpiCU(private val repositorio: KpisRepositorio) {
    suspend fun obtener(id: Int) = repositorio.obtener(id)

}