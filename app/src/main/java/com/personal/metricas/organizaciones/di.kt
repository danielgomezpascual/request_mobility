package com.personal.metricas.organizaciones

import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.kpi.data.ds.local.KpisRoomDS
import com.personal.metricas.kpi.data.ds.local.dao.KpisDao
import com.personal.metricas.kpi.domain.interactors.ObtenerKpisCU
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.organizaciones.data.ds.local.OrganizacionesRoomDS
import com.personal.metricas.organizaciones.data.ds.local.dao.OrganizacionesDao
import com.personal.metricas.organizaciones.data.ds.local.entidades.OrganizacionesRoom
import com.personal.metricas.organizaciones.data.ds.remote.OrganizacionesRemotoDS
import com.personal.metricas.organizaciones.data.ds.remote.servicio.OrganizacionesApiRemoto
import com.personal.metricas.organizaciones.data.repositorio.OrganizacionesRepoImp
import com.personal.metricas.organizaciones.domain.interactors.AlmacenarOrganizacionCU
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionCU
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones
import com.personal.metricas.organizaciones.ui.detalle.OrganizacionesDetalleVM
import com.personal.metricas.organizaciones.ui.lista.ListaOrganizaciones
import com.personal.metricas.organizaciones.ui.lista.ListaOrganizacionesVM
import com.personal.metricas.paneles.domain.interactors.EliminarPanelCU
import com.personal.metricas.paneles.domain.interactors.GuardarPanelCU
import com.personal.metricas.paneles.domain.interactors.ObtenerPanelCU
import com.personal.metricas.paneles.ui.screen.detalle.DetallePanelVM
import com.personal.metricas.sincronizacion.domain.interactors.RealizarSincronizacionCU
import com.personal.metricas.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM
import com.personal.metricas.transacciones.data.repositorios.TransaccionesRepoImp
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


	//single<IRepoOrganizaciones> { OrganizacionesRepoImp() }
	//CU
	single<ObtenerOrganizacionesCU> { ObtenerOrganizacionesCU(get<IRepoOrganizaciones>()) }
	single<ObtenerOrganizacionCU> { ObtenerOrganizacionCU(get<IRepoOrganizaciones>()) }

	viewModel {
		ListaOrganizacionesVM(
			obtenerOrganizacion = get<ObtenerOrganizacionesCU>()
		)
	}
	viewModel {
		OrganizacionesDetalleVM(
			obtenerOrganizacionCU = get<ObtenerOrganizacionCU>(),
			dialog = get<DialogManager>()
		)
	}


}


fun RetrofitServicioOrganizaciones(retrofit: Retrofit): OrganizacionesApiRemoto {
	return retrofit.create(OrganizacionesApiRemoto::class.java)
}