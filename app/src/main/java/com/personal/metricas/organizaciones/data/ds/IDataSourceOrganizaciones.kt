package com.personal.metricas.organizaciones.data.ds

import com.personal.metricas.core.data.ds.IDS
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones

interface IDataSourceOrganizaciones : IDS {
	suspend fun getAll(): List<Organizaciones>
	suspend fun guardar(organizacion: Organizaciones): Long
	suspend fun eliminar(organizacion: Organizaciones)
	suspend fun eliminarTodas()
	suspend fun obtener(id: String): Organizaciones
}