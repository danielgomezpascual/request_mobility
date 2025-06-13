package com.personal.requestmobility.organizaciones.domain.interactors

import com.personal.requestmobility.organizaciones.domain.repositorio.IRepoOrganizaciones

class ObtenerOrganizacionesCU(private val repo: IRepoOrganizaciones) {
    suspend fun getAll() = repo.getAll()

}