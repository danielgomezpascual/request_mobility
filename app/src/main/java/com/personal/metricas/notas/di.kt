package com.personal.metricas.notas

import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.notas.data.ds.local.NotasLocalDS
import com.personal.metricas.notas.data.ds.local.dao.NotasDao
import com.personal.metricas.notas.data.repositorios.NotasRepositorioImp
import com.personal.metricas.notas.domain.interactors.EliminarTodasNotasCU
import com.personal.metricas.notas.domain.interactors.GuardarNotaCU
import com.personal.metricas.notas.domain.interactors.ObtenerNotasCU
import com.personal.metricas.notas.domain.repositorios.NotasRepositorio
import org.koin.dsl.module

val moduloNotas = module {

	// DAO
	single<NotasDao> { get<AppDatabase>().notasDao() }

	// DataSource
	single<NotasLocalDS> { NotasLocalDS(get()) }

	// Repositorio
	single<NotasRepositorio> {
		NotasRepositorioImp(fuentesDatos = listOf(get<NotasLocalDS>()))
	}

	// Casos de Uso
	single<GuardarNotaCU> { GuardarNotaCU(get<NotasRepositorio>()) }
	single<ObtenerNotasCU> { ObtenerNotasCU(get<NotasRepositorio>()) }
	single<EliminarTodasNotasCU> { EliminarTodasNotasCU(get<NotasRepositorio>()) }




	single<NotasManager> { NotasManager(get<DialogManager>()) }
}