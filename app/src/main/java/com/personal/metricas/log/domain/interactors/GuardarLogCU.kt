package com.personal.metricas.log.domain.interactors

import com.personal.metricas.log.data.ds.repositorio.LogRepositorioImp
import com.personal.metricas.log.domain.entidades.Log

class GuardarLogCU(private val repo: LogRepositorioImp) {
	suspend fun guardar(log: Log) = repo.guardar(log)
}