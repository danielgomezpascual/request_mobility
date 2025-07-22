package com.personal.metricas.menu.navegacion

sealed class Modulos{
    object Transacciones: Modulos()
    object Kpis: Modulos()
    object Dashboards: Modulos()
    object Cuadricula: Modulos()
    object Paneles: Modulos()

}