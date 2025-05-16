package com.personal.requestmobility.kpi.domain.interactors

import com.personal.requestmobility.kpi.domain.repositorios.KpisRepositorio

class EliminarTodosKpisCU(private val reposiorioKpis: KpisRepositorio) {

    suspend fun eliminarTodo() = reposiorioKpis.eliminarTodos()

}