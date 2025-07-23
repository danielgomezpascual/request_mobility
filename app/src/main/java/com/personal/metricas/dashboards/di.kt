package com.personal.metricas.dashboards // O com.personal.metricas.dashboards


import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.dashboards.data.ds.local.DashboardLocalDS
import com.personal.metricas.dashboards.data.ds.local.dao.DashboardDao
import com.personal.metricas.dashboards.data.repositorios.DashboardRepositoriosImp
import com.personal.metricas.dashboards.domain.interactors.CargarDashboardCU
import com.personal.metricas.dashboards.domain.interactors.EliminarDashboardCU
import com.personal.metricas.dashboards.domain.interactors.EliminarTodosDashboardsCU
import com.personal.metricas.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.metricas.dashboards.domain.interactors.ObtenerDashboardCU
import com.personal.metricas.dashboards.domain.interactors.ObtenerDashboardsCU
import com.personal.metricas.dashboards.domain.interactors.ObtenerDashboardsHomeCU
import com.personal.metricas.dashboards.domain.interactors.ObtenerEtiquetasDashboardCU
import com.personal.metricas.dashboards.domain.interactors.ObtenerKpisDashboard
import com.personal.metricas.dashboards.domain.interactors.ObtenerSeleccionPanel
import com.personal.metricas.dashboards.domain.repositorios.DashboardRepositorio
import com.personal.metricas.dashboards.ui.screen.cuadricula.CuadriculaDashboardVM
import com.personal.metricas.dashboards.ui.screen.detalle.DetalleDashboardVM
import com.personal.metricas.dashboards.ui.screen.listado.DashboardListadoVM
import com.personal.metricas.dashboards.ui.screen.visualizador.VisualizadorDashboardVM
import com.personal.metricas.inicializador.domain.InicializadorManager
import com.personal.metricas.kpi.domain.interactors.ObtenerKpiCU
import com.personal.metricas.kpi.domain.interactors.ObtenerKpisCU
import com.personal.metricas.menu.screen.HomeVM
import com.personal.metricas.paneles.domain.repositorios.PanelesRepositorio
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.math.sin

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
    single<ObtenerKpisDashboard> { ObtenerKpisDashboard(get<DashboardRepositorio>(),
                                                        get<ObtenerKpiCU>()) }
    single<ObtenerDashboardsHomeCU> { ObtenerDashboardsHomeCU(get<DashboardRepositorio>()) }

    single<ObtenerEtiquetasDashboardCU> { ObtenerEtiquetasDashboardCU(get<ObtenerDashboardsCU>()) }

    // ViewModels for Dashboards
    viewModel {
        CuadriculaDashboardVM(
            obtenerDashboardsCU = get<ObtenerDashboardsCU>(),
            obtenerEtiquetasDashboardCU = get<ObtenerEtiquetasDashboardCU>())
    }



    viewModel {
        VisualizadorDashboardVM(obtenerDashboardCU = get<ObtenerDashboardCU>())
    }




    viewModel {
        DashboardListadoVM(obtenerDashboardsCU = get<ObtenerDashboardsCU>())
    }

    viewModel {
        HomeVM(
            inicalizadorManager = get<InicializadorManager>(),
            obtenerDashboardHomeCU = get<ObtenerDashboardsHomeCU>(),
            dialog = get<DialogManager>()
        )
    }



    viewModel {
        DetalleDashboardVM(
            cargarDashboardCU = get<CargarDashboardCU>(),
            eliminarDashboardCU = get<EliminarDashboardCU>(),
            guardarDashboardCU = get<GuardarDashboardCU>(),
            obtenerSeleccionPanel = get<ObtenerSeleccionPanel>(),
            obtenerKpisCU = get<ObtenerKpisCU>(),
            obtenerEtiquetas = get<ObtenerEtiquetasDashboardCU>(),
            dialog =  get<DialogManager>()
        )
    }
}

