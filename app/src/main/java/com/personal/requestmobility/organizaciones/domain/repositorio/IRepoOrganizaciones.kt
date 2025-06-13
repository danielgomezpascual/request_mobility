package com.personal.requestmobility.organizaciones.domain.repositorio

import com.personal.requestmobility.organizaciones.domain.entidades.Organizaciones

interface IRepoOrganizaciones {
    suspend fun getAll(): List<Organizaciones>
}