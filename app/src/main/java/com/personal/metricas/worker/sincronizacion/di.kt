package com.personal.metricas.worker.sincronizacion

import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.sincronizacion.domain.interactors.RealizarSincronizacionCU
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val moduloWorker = module {



	// ✅ ¡Aquí defines tu Worker!
	// Koin sabrá que para crear un DataSyncWorker, primero debe crear un MyUseCase.
	worker {
		DataSyncWorker(
			appContext = get(),
			workerParams = get(),
			realizarSincronizacionCU = get<RealizarSincronizacionCU>(),
			obtenerOrganizacion = get<ObtenerOrganizacionesCU>()
		)
	}
}