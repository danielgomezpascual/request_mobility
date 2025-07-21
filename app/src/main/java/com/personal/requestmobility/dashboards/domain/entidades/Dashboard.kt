package com.personal.requestmobility.dashboards.domain.entidades

import com.personal.requestmobility.core.utils.Parametros
import com.personal.requestmobility.kpi.domain.entidades.Kpi
import com.personal.requestmobility.paneles.domain.entidades.Panel

data class Dashboard(
	val id: Int,
	val tipo: TipoDashboard,
	val nombre: String,
	val logo: String,
	val home: Boolean,
	val descripcion: String,
	val kpiOrigenDatos: Kpi,
	val paneles: List<Panel> = listOf<Panel>(),
	val parametros : Parametros = Parametros(),
	val autogenerado :Boolean = false,

	){
	
}



