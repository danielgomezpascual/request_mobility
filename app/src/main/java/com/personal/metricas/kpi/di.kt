package com.personal.metricas.kpi

import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.kpi.data.ds.local.KpisRoomDS
import com.personal.metricas.kpi.data.ds.local.dao.KpisDao
import com.personal.metricas.kpi.data.repositorios.KpisRepositorioImp
import com.personal.metricas.kpi.domain.interactors.EliminarKpiCU
import com.personal.metricas.kpi.domain.interactors.EliminarTodosKpisCU
import com.personal.metricas.kpi.domain.interactors.GuardarKpiCU
import com.personal.metricas.kpi.domain.interactors.ObtenerKpiCU
import com.personal.metricas.kpi.domain.interactors.ObtenerKpisCU
import com.personal.metricas.kpi.domain.repositorios.KpisRepositorio
import com.personal.metricas.kpi.ui.screen.detalle.DetalleKpiVM
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.kpi.ui.entidades.OcurrenciasSQL
import com.personal.metricas.kpi.ui.screen.listado.KpisListadoVM
import com.personal.metricas.paneles.domain.interactors.GuardarPanelCU
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moduloKpis = module {



    // Database
    single<KpisDao> { get<AppDatabase>().kpisDao() }
    single<KpisRoomDS>{ KpisRoomDS(get<KpisDao>())}


    //Repositiorio
    single<KpisRepositorio>{
        KpisRepositorioImp(listOf(
            get<KpisRoomDS>(),
           ))
    }

    //Casos de uso
    single<EliminarKpiCU>{ EliminarKpiCU(get<KpisRepositorio>()) }
    single<EliminarTodosKpisCU>{ EliminarTodosKpisCU(get<KpisRepositorio>()) }
    single<GuardarKpiCU>{ GuardarKpiCU(get<KpisRepositorio>()) }
    single<ObtenerKpiCU>{ ObtenerKpiCU(get<KpisRepositorio>()) }
    single<ObtenerKpisCU>{ ObtenerKpisCU(get<KpisRepositorio>()) }


    single<DialogManager> { DialogManager() }

	single<OcurrenciasSQL> { OcurrenciasSQL() }


    //ViewMOdel
    viewModel { KpisListadoVM(get<ObtenerKpisCU>()) }
    viewModel { DetalleKpiVM(
		obtenerKpi = get<ObtenerKpiCU>(),
		todosKpisCU = get<ObtenerKpisCU>(),
		guardarKpi = get<GuardarKpiCU>(),
		eliminarKpi = get<EliminarKpiCU>(),
		guardarPanel = get<GuardarPanelCU>(),
		dialog = get <DialogManager>(),
		ocurrenciasSQL = get<OcurrenciasSQL>()


    )
    }


}
