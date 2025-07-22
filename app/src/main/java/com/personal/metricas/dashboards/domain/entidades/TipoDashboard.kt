package com.personal.metricas.dashboards.domain.entidades

sealed class TipoDashboard
{
	data class Estatico(
		val id: Int = 0, val tipo: String = "Estatico",
		val origen: String = "Tabla",
					   ) : TipoDashboard()
	
	data class Dinamico(
		val id: Int = 1, val tipo: String = "Dinamico",
		val origen: String = "KPI",
					   ) : TipoDashboard()
	
	
	companion object
	{
		fun fromId(id: Int): TipoDashboard =
			when (id) {
				Estatico().id -> Estatico()
				Dinamico().id -> Dinamico()
				else          -> Estatico()
			}
	
		
	}

	
	
	
}