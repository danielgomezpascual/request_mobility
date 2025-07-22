package com.personal.metricas.kpi.domain.interactors

import com.personal.metricas.kpi.domain.repositorios.KpisRepositorio

class EliminarTodosKpisCU(private val reposiorioKpis: KpisRepositorio) {

    suspend fun eliminarTodo() = reposiorioKpis.eliminarTodos()

}