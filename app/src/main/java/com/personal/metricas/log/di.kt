package com.personal.metricas.log

import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.log.data.ds.local.LogLocalDS
import com.personal.metricas.log.data.ds.local.dao.LogDao
import com.personal.metricas.log.data.ds.repositorio.LogRepositorioImp
import com.personal.metricas.log.domain.interactors.GuardarLogCU
import com.personal.metricas.notas.data.ds.local.NotasLocalDS
import com.personal.metricas.notas.data.ds.local.dao.NotasDao
import com.personal.metricas.notas.data.repositorios.NotasRepositorioImp
import com.personal.metricas.notas.domain.interactors.EliminarTodasNotasCU
import com.personal.metricas.notas.domain.interactors.GuardarNotaCU
import com.personal.metricas.notas.domain.interactors.ObtenerNotasCU
import com.personal.metricas.notas.domain.repositorios.NotasRepositorio
import org.koin.dsl.module

val moduloLogSync = module {

	// DAO
	single<LogDao> { get<AppDatabase>().logDao() }

	// DataSource
	single<LogLocalDS> { LogLocalDS(get<LogDao>()) }

	// Repositorio
	single<LogRepositorioImp> {
		LogRepositorioImp(fuentesDatos = listOf(get<LogLocalDS>()))
	}

	// Casos de Uso
	single<GuardarLogCU> { GuardarLogCU(get<LogRepositorioImp>()) }

}