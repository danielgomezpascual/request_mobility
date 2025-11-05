package com.personal.metricas.sincronizacion

import com.personal.metricas.core.composables.dialogos.DialogManager

import com.personal.metricas.endpoints.data.ds.remote.EndPointsRemotoDS
import com.personal.metricas.endpoints.data.ds.remote.servicio.EndPointRemotos
import com.personal.metricas.endpoints.domain.ObtenerDatosEndPoint
import com.personal.metricas.log.domain.interactors.GuardarLogCU
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.organizaciones.domain.interactors.AlmacenarOrganizacionCU
import com.personal.metricas.organizaciones.domain.interactors.GenerarPlanificacionAutomaticaOrganizaciones
import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones
import com.personal.metricas.sincronizacion.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.sincronizacion.domain.interactors.RealizarSincronizacionCU
import com.personal.metricas.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM
import com.personal.metricas.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.metricas.transacciones.domain.interactors.GuardarTransacciones
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


val moduloSincronizacion = module {


	//


	//Retrofit
	single<EndPointRemotos> { RetrofitServicioEndPointRemotos(get()) }
	single<EndPointsRemotoDS> { EndPointsRemotoDS(get<EndPointRemotos>()) }


	//CU
	single<ObtenerDatosEndPoint> { ObtenerDatosEndPoint(get<EndPointsRemotoDS>()) }
	single<RealizarSincronizacionCU> {
		RealizarSincronizacionCU(
			repoTrx = get<TransaccionesRepoImp>(),
			guardar = get<GuardarTransacciones>(),
			log =  get<GuardarLogCU>()
		)
	}


	single<AlmacenarOrganizacionCU> { AlmacenarOrganizacionCU(repo = get<IRepoOrganizaciones>()) }
	single<ObtenerOrganizacionesCU> { ObtenerOrganizacionesCU(repo = get<IRepoOrganizaciones>()) }

	//ViewModel
	viewModel {
		ListaOrganizacionesSincronizarVM(
			obtenerOrganizacion = get<ObtenerOrganizacionesCU>(),
			realizarSincronizacionCU = get<RealizarSincronizacionCU>(),
			repoTrx = get<TransaccionesRepoImp>(),
			//guardar = get<GuardarTransacciones>(),
			dialog = get<DialogManager>(),
			notas = get<NotasManager>(),
			guardarOrganizacion = get<AlmacenarOrganizacionCU>(),
			autoPlanificacion = get<GenerarPlanificacionAutomaticaOrganizaciones>()

		)
	}

}


fun RetrofitServicioEndPointRemotos(retrofit: Retrofit): EndPointRemotos {
	return retrofit.create(EndPointRemotos::class.java)
}