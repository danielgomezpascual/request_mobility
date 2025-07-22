package com.personal.metricas.organizaciones.data.ds

import com.personal.metricas.core.data.ds.IDS
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones

interface IDataSourceOrganizaciones: IDS {
    suspend fun getAll(): List<Organizaciones>
}