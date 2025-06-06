package com.personal.requestmobility.paneles // O com.personal.requestmobility.dashboards


import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpisCU
import com.personal.requestmobility.paneles.data.ds.local.PanelesRoomDS
import com.personal.requestmobility.paneles.data.ds.local.dao.PanelesDao
import com.personal.requestmobility.paneles.data.repositorios.PanelesRepositorioImp
import com.personal.requestmobility.paneles.domain.interactors.EliminarPanelCU
import com.personal.requestmobility.paneles.domain.interactors.EliminarTodosPanelesCU
import com.personal.requestmobility.paneles.domain.interactors.GuardarPanelCU
import com.personal.requestmobility.paneles.domain.interactors.ObtenerPanelCU
import com.personal.requestmobility.paneles.domain.interactors.ObtenerPanelesCU
import com.personal.requestmobility.paneles.domain.repositorios.PanelesRepositorio
import com.personal.requestmobility.paneles.ui.screen.detalle.DetallePanelVM
import com.personal.requestmobility.paneles.ui.screen.listado.PanelesListadoVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moduloPaneles = module {


    // Database
    single<PanelesDao> { get<AppDatabase>().panelesDao() }
    single<PanelesRoomDS> { PanelesRoomDS(get<PanelesDao>()) }


    //Repositiorio
    single<PanelesRepositorio> {
        PanelesRepositorioImp(
            listOf(
                get<PanelesRoomDS>()
            )
        )
    }

    //Casos de uso
    single<EliminarPanelCU> { EliminarPanelCU(get<PanelesRepositorio>()) }
    single<EliminarTodosPanelesCU> { EliminarTodosPanelesCU(get<PanelesRepositorio>()) }
    single<GuardarPanelCU> { GuardarPanelCU(get<PanelesRepositorio>()) }
    single<ObtenerPanelCU> { ObtenerPanelCU(get<PanelesRepositorio>()) }
    single<ObtenerPanelesCU> { ObtenerPanelesCU(get<PanelesRepositorio>()) }

    //ViewMOdel
    viewModel { PanelesListadoVM(get<ObtenerPanelesCU>()) }
    viewModel {
        DetallePanelVM(
            get<ObtenerPanelCU>(),
            get<GuardarPanelCU>(),
            get<EliminarPanelCU>(),
            get<ObtenerKpisCU>()
        )
    }
}

