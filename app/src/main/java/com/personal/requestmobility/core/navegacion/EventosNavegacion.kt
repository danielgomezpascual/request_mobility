package com.personal.requestmobility.core.navegacion

sealed class EventosNavegacion{
    object MenuApp: EventosNavegacion()
    data class Cargar(val identificador: Int): EventosNavegacion()


    object Volver: EventosNavegacion()

    //======== KPIS =============
    object MenuKpis: EventosNavegacion()
    data class CargarKPI(val identificador: Int): EventosNavegacion()
    object NuevoKPI: EventosNavegacion()

    //======= Paneles =============

    object MenuPaneles: EventosNavegacion()
    object NuevoPanel: EventosNavegacion()
    data class CargarPanel(val identificador : Int): EventosNavegacion()

    data class VisualizadorDashboard(val identificador: Int): EventosNavegacion()


    //======= Dashboards ===========
    object MenuDashboard: EventosNavegacion()
    object NuevoDashboard: EventosNavegacion()
    data class CargarDashboard(val identificador : Int): EventosNavegacion()
    object MenuVisualizadorDashboard: EventosNavegacion()


    object Sincronizacion: EventosNavegacion()


    object MenuHerramientas: EventosNavegacion()
}