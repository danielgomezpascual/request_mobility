package com.personal.metricas.organizaciones.domain.entidades

import com.personal.metricas.organizaciones.ui.entidades.FORMA_SINCRONIZAR

data class Organizaciones(

	val organizationCode: String = "",
	val organizationName: String = "",
	val organizationId: String = "",
	val masterOrganizationId: String = "",
	val seleccionada: String = "0",

	val activo: Boolean = false,
	val visible: Boolean = true,
	val color: Int = -12156236,
	val formaSincronizar: FORMA_SINCRONIZAR = FORMA_SINCRONIZAR.AUTO,
	val horas: String = ""

)


