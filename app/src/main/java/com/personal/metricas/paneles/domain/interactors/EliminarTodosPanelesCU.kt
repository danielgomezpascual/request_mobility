package com.personal.metricas.paneles.domain.interactors

import com.personal.metricas.paneles.domain.repositorios.PanelesRepositorio

class EliminarTodosPanelesCU(private val repo: PanelesRepositorio) {

    suspend fun eliminarTodo() = repo.eliminarTodos()

}