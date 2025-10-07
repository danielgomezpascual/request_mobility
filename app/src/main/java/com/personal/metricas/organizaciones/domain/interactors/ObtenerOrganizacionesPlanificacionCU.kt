package com.personal.metricas.organizaciones.domain.interactors

import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones

class ObtenerOrganizacionesPlanificacionCU(private val repo: IRepoOrganizaciones) {
    suspend fun damePlanificaciones() = repo.getAllLocal()

}