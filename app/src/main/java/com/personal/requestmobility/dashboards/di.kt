package com.personal.requestmobility.dashboards // O com.personal.requestmobility.dashboards


import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.dashboards.data.ds.local.DashboardLocalDS
import com.personal.requestmobility.dashboards.data.ds.local.dao.DashboardDao
import com.personal.requestmobility.dashboards.data.repositorios.DashboardRepositoriosImp
import com.personal.requestmobility.dashboards.domain.interactors.CargarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.EliminarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.EliminarTodosDashboardsCU
import com.personal.requestmobility.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerDashboardsCU
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerDashboardsHomeCU
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerKpisDashboard
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerSeleccionPanel
import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio
import com.personal.requestmobility.dashboards.ui.screen.cuadricula.CuadriculaDashboardVM
import com.personal.requestmobility.dashboards.ui.screen.detalle.DetalleDashboardVM
import com.personal.requestmobility.dashboards.ui.screen.listado.DashboardListadoVM
import com.personal.requestmobility.dashboards.ui.screen.visualizador.VisualizadorDashboardVM
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpiCU
import com.personal.requestmobility.kpi.domain.repositorios.KpisRepositorio
import com.personal.requestmobility.menu.screen.HomeVM
import com.personal.requestmobility.paneles.domain.interactors.ObtenerPanelesCU
import com.personal.requestmobility.paneles.domain.repositorios.PanelesRepositorio
import com.personal.requestmobility.paneles.ui.screen.listado.PanelesListadoVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moduloDashboards = module {

    // DAO for Dashboards
    single<DashboardDao> { get<AppDatabase>().dashboardDao() } // Asume que AppDatabase tiene dashboardDao()

    // DataSources for Dashboards
    single<DashboardLocalDS> { DashboardLocalDS(get()) }

    // Repository for Dashboards
    single<DashboardRepositorio> {
        DashboardRepositoriosImp(
            fuentesDatos = listOf( // El orden puede ser importante si BaseRepositorio lo considera
                get<DashboardLocalDS>()
            )
        )
    }

    // Use Cases for Dashboards
    single<ObtenerDashboardsCU> { ObtenerDashboardsCU(get<DashboardRepositorio>()) }
    single<ObtenerDashboardCU> { ObtenerDashboardCU(get<DashboardRepositorio>()) }
    single<EliminarTodosDashboardsCU> { EliminarTodosDashboardsCU(get<DashboardRepositorio>()) }
    single<EliminarDashboardCU> { EliminarDashboardCU(get<DashboardRepositorio>()) }
    single<CargarDashboardCU> { CargarDashboardCU(get<DashboardRepositorio>()) }
    single<GuardarDashboardCU> { GuardarDashboardCU(get<DashboardRepositorio>()) }

    single<ObtenerSeleccionPanel> { ObtenerSeleccionPanel(get<PanelesRepositorio>()) }
    single<ObtenerKpisDashboard> { ObtenerKpisDashboard(get<DashboardRepositorio>(), get<ObtenerKpiCU>()) }
    single<ObtenerDashboardsHomeCU> { ObtenerDashboardsHomeCU(get<DashboardRepositorio>()) }


    // ViewModels for Dashboards
    viewModel {
        CuadriculaDashboardVM(obtenerDashboardsCU = get<ObtenerDashboardsCU>())
    }



    viewModel {
        VisualizadorDashboardVM(obtenerDashboardCU = get<ObtenerDashboardCU>())
    }




    viewModel {
        DashboardListadoVM(obtenerDashboardsCU = get<ObtenerDashboardsCU>())
    }

    viewModel {
        HomeVM(obtenerDashboardHomeCU = get<ObtenerDashboardsHomeCU>())
    }



    viewModel {
        DetalleDashboardVM(
            cargarDashboardCU = get<CargarDashboardCU>(),
            eliminarDashboardCU = get<EliminarDashboardCU>(),
            guardarDashboardCU = get<GuardarDashboardCU>(),
            obtenerSeleccionPanel = get<ObtenerSeleccionPanel>(),
            dialog =  get<DialogManager>()
        )
    }
}

