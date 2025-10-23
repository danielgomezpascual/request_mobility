package com.personal.metricas.organizaciones

import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.organizaciones.data.ds.local.OrganizacionesRoomDS
import com.personal.metricas.organizaciones.data.ds.local.dao.OrganizacionesDao
import com.personal.metricas.organizaciones.data.ds.remote.OrganizacionesRemotoDS
import com.personal.metricas.organizaciones.data.ds.remote.servicio.OrganizacionesApiRemoto
import com.personal.metricas.organizaciones.data.repositorio.OrganizacionesRepoImp
import com.personal.metricas.organizaciones.domain.interactors.AlmacenarOrganizacionCU
import com.personal.metricas.organizaciones.domain.interactors.GenerarPlanificacionAutomaticaOrganizaciones
import com.personal.metricas.organizaciones.domain.interactors.GuardarPlanificacionOrganizacinCU
import com.personal.metricas.organizaciones.domain.interactors.ObtenerHorasTransaccionesOrganizacionCU
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionCU

import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesLocalCU
import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones
import com.personal.metricas.organizaciones.domain.repositorio.TransaccionesOrganizacionImp
import com.personal.metricas.organizaciones.ui.detalle.OrganizacionesDetalleVM
import com.personal.metricas.organizaciones.ui.lista.ListaOrganizacionesVM
import com.personal.metricas.sincronizacion.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.transacciones.data.ds.local.TransaccionesLocalDS
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val moduloOrganizaciones = module {


	single<OrganizacionesDao> { get<AppDatabase>().organizacionesDao() }
	//single<KpisRoomDS>{ KpisRoomDS(get<KpisDao>())}


	//Retrofit
	single<OrganizacionesApiRemoto> { RetrofitServicioOrganizaciones(get<Retrofit>()) }
	single<OrganizacionesRemotoDS> { OrganizacionesRemotoDS(get<OrganizacionesApiRemoto>()) }


	single<OrganizacionesRoomDS> { OrganizacionesRoomDS(get<OrganizacionesDao>()) }

	//Repo
	single<IRepoOrganizaciones> {
		OrganizacionesRepoImp(
			listOf(
				get<OrganizacionesRemotoDS>(),
				get<OrganizacionesRoomDS>()
			)
		)
	}

	single<TransaccionesOrganizacionImp> {
		TransaccionesOrganizacionImp(listOf(
			get<TransaccionesLocalDS>()
			//get<TransaccionesRemotoDS>())
		))
	}


	//single<IRepoOrganizaciones> { OrganizacionesRepoImp() }
	//CU
	single<ObtenerOrganizacionesLocalCU> { ObtenerOrganizacionesLocalCU(get<IRepoOrganizaciones>()) }
	single<ObtenerOrganizacionCU> { ObtenerOrganizacionCU(get<IRepoOrganizaciones>()) }
	single<ObtenerHorasTransaccionesOrganizacionCU> { ObtenerHorasTransaccionesOrganizacionCU(get<TransaccionesOrganizacionImp>()) }
	single<GuardarPlanificacionOrganizacinCU> { GuardarPlanificacionOrganizacinCU(get<IRepoOrganizaciones>()) }

	single<GenerarPlanificacionAutomaticaOrganizaciones> {
		GenerarPlanificacionAutomaticaOrganizaciones(
			get<ObtenerOrganizacionesLocalCU>(),
			obtenerHoras = get<ObtenerHorasTransaccionesOrganizacionCU>(),
			guardarOrganizacion = get<GuardarPlanificacionOrganizacinCU>()
		)
	}





	viewModel {
		ListaOrganizacionesVM(
			obtenerOrganizacion = get<ObtenerOrganizacionesLocalCU>(),
			autoPlanificacion = get<GenerarPlanificacionAutomaticaOrganizaciones>(),
			guardar = get<AlmacenarOrganizacionCU>(),
			obtenerOrganizacionesRemoto = get<ObtenerOrganizacionesCU>(),
			dialog = get<DialogManager>()
		)
	}


	viewModel {
		OrganizacionesDetalleVM(
			obtenerOrganizacionCU = get<ObtenerOrganizacionCU>(),
			obtenerHorasTransaccionesOrganizacionCU = get<ObtenerHorasTransaccionesOrganizacionCU>(),
			guardarPlanificacionOrganizacinCU = get<GuardarPlanificacionOrganizacinCU>(),
			dialog = get<DialogManager>()
		)
	}


}


fun RetrofitServicioOrganizaciones(retrofit: Retrofit): OrganizacionesApiRemoto {
	return retrofit.create(OrganizacionesApiRemoto::class.java)
}