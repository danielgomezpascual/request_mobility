package com.personal.metricas.kpi.domain.interactors

import com.personal.metricas.kpi.domain.repositorios.KpisRepositorio
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.kpi.ui.entidades.toKpi

class GuardarKpiCU(private val repositorio: KpisRepositorio) {
    suspend fun guardar(kpiUI: KpiUI) = repositorio.guardar(kpiUI.toKpi())

}