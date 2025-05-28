package com.personal.requestmobility.paneles.domain.interactors

import com.personal.requestmobility.paneles.domain.repositorios.PanelesRepositorio

class EliminarTodosPanelesCU(private val repo: PanelesRepositorio) {

    suspend fun eliminarTodo() = repo.eliminarTodos()

}