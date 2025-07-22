package com.personal.metricas.endpoints

import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.endpoints.data.ds.local.EndPointLocalDS
import com.personal.metricas.endpoints.data.ds.local.dao.EndPointDao
import com.personal.metricas.endpoints.data.ds.remote.EndPointsRemotoDS
import com.personal.metricas.endpoints.data.repositorios.EndPointRepositorioImp
import com.personal.metricas.endpoints.domain.interactors.EliminarEndPointCU
import com.personal.metricas.endpoints.domain.interactors.GuardarEndPointCU
import com.personal.metricas.endpoints.domain.interactors.AlmacenarDatosRemotosEndPointCU
import com.personal.metricas.endpoints.domain.interactors.ObtenerEndPointCU
import com.personal.metricas.endpoints.domain.interactors.ObtenerEndPointsCU
import com.personal.metricas.endpoints.domain.repositorios.EndPointRepositorio
import com.personal.metricas.endpoints.domain.servicios.ConversorJsonToTabla
import com.personal.metricas.endpoints.ui.screen.detalle.DetalleEndPointVM
import com.personal.metricas.endpoints.ui.screen.listado.EndPointsListadoVM
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