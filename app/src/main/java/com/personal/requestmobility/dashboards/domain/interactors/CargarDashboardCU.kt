package com.personal.requestmobility.dashboards.domain.interactors

import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio
// Asegúrate de tener definida tu clase DomainExcepcion o ajusta el manejo de excepciones según tu proyecto.
// import com.personal.requestmobility.dashboards.domain.excepciones.DomainExcepcion

class CargarDashboardCU(private val repo: DashboardRepositorio) {
    suspend fun cargar(identificador: Int): Dashboard {
        try {
            return repo.obtener(identificador)
        } catch (e: Exception) {
            // Ejemplo de manejo, ajusta según tu implementación de DomainExcepcion
            // throw if (e is DomainExcepcion) {
            //     e
            // } else {
            //     DomainExcepcion.ErrorDesconocido(e)
            // }
            throw e // Re-lanza la excepción o maneja específicamente
        }
    }
}