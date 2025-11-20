package com.personal.metricas.core.navegacion

sealed class EventosNavegacion {
	object HomeApp : EventosNavegacion()
	data class Cargar(val identificador: Int) : EventosNavegacion()


	object Volver : EventosNavegacion()

	//======== KPIS =============
	object MenuKpis : EventosNavegacion()
	data class CargarKPI(val identificador: Int) : EventosNavegacion()
	object NuevoKPI : EventosNavegacion()

	//======= Paneles =============
	object MenuPaneles : EventosNavegacion()
	object NuevoPanel : EventosNavegacion()
	data class CargarPanel(val identificador: Int) : EventosNavegacion()

	data class VisualizadorDashboard(val identificador: Int, val parametrosJson: String) : EventosNavegacion()


	//======= Dashboards ===========
	object MenuDashboard : EventosNavegacion()
	object NuevoDashboard : EventosNavegacion()
	data class CargarDashboard(val identificador: Int) : EventosNavegacion()
	object CuadriculaDashboard : EventosNavegacion()


	//Organizaciones --------------
	object ListaOrganizaciones: EventosNavegacion()
	object ListaOrganizacionesStart: EventosNavegacion()
	data class CargarSincronizacionOrganizacion(val identificador: String) : EventosNavegacion()

	//======== EndPoints =============
	object MenuEndPoints : EventosNavegacion()
	data class CargarEndPoint(val identificador: Int) : EventosNavegacion()
	object NuevoEndPonint : EventosNavegacion()


	object Sincronizacion : EventosNavegacion()

	object SincronizacionMenu : EventosNavegacion()


	object MenuHerramientas : EventosNavegacion()
	object MenuHerramientasStart : EventosNavegacion()


	object Settings : EventosNavegacion()
}