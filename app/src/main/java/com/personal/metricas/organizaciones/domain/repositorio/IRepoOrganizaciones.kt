package com.personal.metricas.organizaciones.domain.repositorio

import com.personal.metricas.organizaciones.domain.entidades.Organizaciones

interface IRepoOrganizaciones {
    suspend fun getAll(): List<Organizaciones>
    suspend fun guardar(organizacion: Organizaciones): Long
    suspend fun getPorID(organizatinCode: String): Organizaciones?
}