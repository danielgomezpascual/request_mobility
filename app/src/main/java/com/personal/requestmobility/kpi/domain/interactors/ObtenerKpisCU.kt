package com.personal.requestmobility.kpi.domain.interactors

import com.personal.requestmobility.kpi.domain.repositorios.KpisRepositorio

class ObtenerKpisCU(private val repoKpi: KpisRepositorio) {
    suspend fun getAll() = repoKpi.getAll()

}