package com.personal.metricas.organizaciones.domain.interactors

import com.personal.metricas.organizaciones.domain.entidades.Organizaciones
import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones
import com.personal.metricas.paneles.domain.repositorios.PanelesRepositorio

class GuardarPlanificacionOrganizacinCU( private val repo: IRepoOrganizaciones ) {
	suspend fun guardar(organizacion: Organizaciones){
			repo.guardar(organizacion)

	}
}