package com.personal.metricas.worker.sincronizacion

import com.personal.metricas.organizaciones.data.ds.local.OrganizacionesRoomDS
import com.personal.metricas.organizaciones.data.ds.remote.OrganizacionesRemotoDS
import com.personal.metricas.organizaciones.data.repositorio.OrganizacionesRepoImp
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesPlanificacionCU
import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones
import com.personal.metricas.sincronizacion.domain.interactors.RealizarSincronizacionCU
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import org.koin.dsl.single
import kotlin.math.sin

val moduloWorker = module {


	single<IRepoOrganizaciones> {
		OrganizacionesRepoImp(
			listOf(
				get<OrganizacionesRemotoDS>(),
				get<OrganizacionesRoomDS>()
			)
		)
	}
	single<ObtenerOrganizacionesPlanificacionCU>{ ObtenerOrganizacionesPlanificacionCU(get<IRepoOrganizaciones>()) }

	// ✅ ¡Aquí defines tu Worker!
	// Koin sabrá que para crear un DataSyncWorker, primero debe crear un MyUseCase.
	worker {
		DataSyncWorker(
			appContext = get(),
			workerParams = get(),
			realizarSincronizacionCU = get<RealizarSincronizacionCU>(),
			obtenerPlanificacionOrganizacion =  get<ObtenerOrganizacionesPlanificacionCU>()

		)
	}
}