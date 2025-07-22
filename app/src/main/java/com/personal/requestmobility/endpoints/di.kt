package com.personal.requestmobility.endpoints

import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.endpoints.data.ds.local.EndPointLocalDS
import com.personal.requestmobility.endpoints.data.ds.local.dao.EndPointDao
import com.personal.requestmobility.endpoints.data.ds.remote.EndPointsRemotoDS
import com.personal.requestmobility.endpoints.data.repositorios.EndPointRepositorioImp
import com.personal.requestmobility.endpoints.domain.interactors.EliminarEndPointCU
import com.personal.requestmobility.endpoints.domain.interactors.GuardarEndPointCU
import com.personal.requestmobility.endpoints.domain.interactors.AlmacenarDatosRemotosEndPointCU
import com.personal.requestmobility.endpoints.domain.interactors.ObtenerEndPointCU
import com.personal.requestmobility.endpoints.domain.interactors.ObtenerEndPointsCU
import com.personal.requestmobility.endpoints.domain.repositorios.EndPointRepositorio
import com.personal.requestmobility.endpoints.domain.servicios.ConversorJsonToTabla
import com.personal.requestmobility.endpoints.ui.screen.detalle.DetalleEndPointVM
import com.personal.requestmobility.endpoints.ui.screen.listado.EndPointsListadoVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val moduloEndPoints = module {
	// DAO
	single<EndPointDao> { get<AppDatabase>().endPointDao() }

	// DataSource
	single<EndPointLocalDS> { EndPointLocalDS(get()) }

	// Repositorio
	single<EndPointRepositorio> {
		EndPointRepositorioImp(fuentesDatos = listOf(get<EndPointLocalDS>()))
	}

	// Casos de Uso
	single<ObtenerEndPointsCU> { ObtenerEndPointsCU(get()) }
	single<ObtenerEndPointCU> { ObtenerEndPointCU(get()) }
	single<GuardarEndPointCU> { GuardarEndPointCU(get()) }
	single<EliminarEndPointCU> { EliminarEndPointCU(get()) }


	single<ConversorJsonToTabla> { ConversorJsonToTabla() }

	single<AlmacenarDatosRemotosEndPointCU> {
		AlmacenarDatosRemotosEndPointCU(obtenerEndPointCU = get<ObtenerEndPointCU>(),
										accesoRemoto = get<EndPointsRemotoDS>(),
										conversonrJson = get<ConversorJsonToTabla>()
		)
	}

	//View Model
	viewModel {
		EndPointsListadoVM(obtenerEndPoins = get<ObtenerEndPointsCU>())
	}

	viewModel {
		DetalleEndPointVM(
			obtenerEndPointUI = get<ObtenerEndPointCU>(),
			guardarEndPoint = get<GuardarEndPointCU>(),
			eliminarEndPoint = get<EliminarEndPointCU>(),
			procesarEndPoint =  get<AlmacenarDatosRemotosEndPointCU>(),
			dialog = get<DialogManager>()
		)
	}


}