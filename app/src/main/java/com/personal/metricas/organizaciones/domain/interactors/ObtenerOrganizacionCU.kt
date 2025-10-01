package com.personal.metricas.organizaciones.domain.interactors

import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones

class ObtenerOrganizacionCU(private val repo: IRepoOrganizaciones) {
    suspend fun get(identificador: String) = repo.getPorID(identificador)

}