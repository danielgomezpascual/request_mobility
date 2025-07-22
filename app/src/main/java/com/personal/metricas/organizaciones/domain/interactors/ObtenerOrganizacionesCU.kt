package com.personal.metricas.organizaciones.domain.interactors

import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones

class ObtenerOrganizacionesCU(private val repo: IRepoOrganizaciones) {
    suspend fun getAll() = repo.getAll()

}