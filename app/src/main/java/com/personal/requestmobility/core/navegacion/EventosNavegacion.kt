package com.personal.requestmobility.core.navegacion

sealed class EventosNavegacion{
    object MenuApp: EventosNavegacion()
    data class Cargar(val identificador: Int): EventosNavegacion()


    object Volver: EventosNavegacion()
    object MenuKpis: EventosNavegacion()
    data class CargarKPI(val identificador: Int): EventosNavegacion()
    object NuevoKPI: EventosNavegacion()

    data class VisualizadorDashboard(val identificador: Int): EventosNavegacion()
}