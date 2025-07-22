package com.personal.requestmobility.endpoints.domain.interactors

import com.personal.requestmobility.endpoints.domain.repositorios.EndPointRepositorio

class ObtenerEndPointCU(private val repo: EndPointRepositorio) {
	suspend fun obtener(id: Int) = repo.obtener(id)
}