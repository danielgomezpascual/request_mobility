package com.personal.metricas.organizaciones.domain.repositorio

import com.personal.metricas.organizaciones.domain.entidades.Organizaciones

interface IRepoOrganizaciones {
    suspend fun getAll(): List<Organizaciones>
}