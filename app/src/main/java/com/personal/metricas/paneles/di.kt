package com.personal.metricas.paneles // O com.personal.metricas.dashboards


import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.kpi.domain.interactors.ObtenerKpisCU
import com.personal.metricas.paneles.data.ds.local.PanelesRoomDS
import com.personal.metricas.paneles.data.ds.local.dao.PanelesDao
import com.personal.metricas.paneles.data.repositorios.PanelesRepositorioImp
import com.personal.metricas.paneles.domain.interactors.EliminarPanelCU
import com.personal.metricas.paneles.domain.interactors.EliminarTodosPanelesCU
import com.personal.metricas.paneles.domain.interactors.GuardarPanelCU
import com.personal.metricas.paneles.domain.interactors.ObtenerPanelCU
import com.personal.metricas.paneles.domain.interactors.ObtenerPanelesCU
import com.personal.metricas.paneles.domain.repositorios.PanelesRepositorio
import com.personal.metricas.paneles.ui.screen.detalle.DetallePanelVM
import com.personal.metricas.paneles.ui.screen.listado.PanelesListadoVM
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
            get<ObtenerKpisCU>(),
            get<DialogManager>()
        )
    }
}

