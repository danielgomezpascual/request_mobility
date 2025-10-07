package com.personal.metricas.organizaciones.domain.interactors

import com.personal.metricas.organizaciones.domain.entidades.Organizaciones
import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones
import com.personal.metricas.sincronizacion.ui.entidades.OrganizacionesSincronizarUI
import com.personal.metricas.sincronizacion.ui.entidades.toOrganizacion

class AlmacenarOrganizacionCU(private val repo: IRepoOrganizaciones) {

	suspend fun guardar(organiazacionUI: OrganizacionesSincronizarUI) {
		val organizacionLocal: Organizaciones = repo.getPorID(organiazacionUI.organizationCode) ?: Organizaciones()
		val organizacion = organiazacionUI.toOrganizacion()
		repo.guardar(organizacion.copy(seleccionada = organizacionLocal.seleccionada, horas = organizacionLocal.horas))
	}


}