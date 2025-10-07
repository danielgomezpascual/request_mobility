package com.personal.metricas.organizaciones.ui.entidades

import com.personal.metricas.organizaciones.domain.entidades.Organizaciones

enum class FORMA_SINCRONIZAR { MANUAL, AUTO }
data class OrganizacionUI(
	val organizationCode: String = "",
	val organizationName: String = "",
	val organizationId: String = "",
	val masterOrganizationId: String = "",
	val activo: Boolean = false,
	val visible: Boolean = true,
	val color: Int = -12156236,
	val formaSincronizar: FORMA_SINCRONIZAR = FORMA_SINCRONIZAR.AUTO,
	val horas: String = "",

	) {
	companion object {
		fun fromOrganizacion(organizacion: Organizaciones) =
			OrganizacionUI(
				organizationCode = organizacion.organizationCode,
				organizationName = organizacion.organizationName,
				organizationId = organizacion.organizationId,
				masterOrganizationId = organizacion.masterOrganizationId,
				activo = organizacion.activo,
				visible = organizacion.visible,
				color = organizacion.color,
				formaSincronizar = organizacion.formaSincronizar,
				horas = organizacion.horas,
			)
	}

}


fun OrganizacionUI.toOrganizacion() =
	Organizaciones(
		organizationCode = this.organizationCode,
		organizationName = this.organizationName,
		organizationId = this.organizationId,
		masterOrganizationId = this.masterOrganizationId,
		activo = this.activo,
		visible = this.visible,
		color = this.color,
		formaSincronizar = this.formaSincronizar,
		horas = this.horas,
	)

