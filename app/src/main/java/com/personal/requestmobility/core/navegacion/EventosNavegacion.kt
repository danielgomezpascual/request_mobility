package com.personal.requestmobility.core.navegacion

sealed class EventosNavegacion{
    object MenuApp: EventosNavegacion()
    object Volver: EventosNavegacion()
    data class Cargar(val identificador: Int): EventosNavegacion()
}