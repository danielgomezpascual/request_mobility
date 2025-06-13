package com.personal.requestmobility.organizaciones.data.ds

import com.personal.requestmobility.core.data.ds.IDS
import com.personal.requestmobility.organizaciones.domain.entidades.Organizaciones

interface IDataSourceOrganizaciones: IDS {
    suspend fun getAll(): List<Organizaciones>
}