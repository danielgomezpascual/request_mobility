package com.personal.requestmobility.dashboards // O com.personal.requestmobility.dashboards


import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.dashboards.data.ds.local.DashboardLocalDS
import com.personal.requestmobility.dashboards.data.ds.local.dao.DashboardDao
import com.personal.requestmobility.dashboards.data.repositorios.DashboardRepositoriosImp
import com.personal.requestmobility.dashboards.domain.interactors.* // Importa todos los casos de uso del dominio
import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio
import com.personal.requestmobility.dashboards.ui.screen.detalle.DetalleDashboardVM
import com.personal.requestmobility.dashboards.ui.screen.listado.DashboardListadoVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

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
    single<ObtenerDashboardCU> { ObtenerDashboardCU(get<DashboardRepositorio>()) }
    single<EliminarTodosDashboardsCU> { EliminarTodosDashboardsCU(get<DashboardRepositorio>()) }
    single<EliminarDashboardCU> { EliminarDashboardCU(get<DashboardRepositorio>()) }
    single<GuardarDashboardCU> { GuardarDashboardCU(get<DashboardRepositorio>()) }
    single<CargarDashboardCU> { CargarDashboardCU(get<DashboardRepositorio>()) }


    // ViewModels for Dashboards
    viewModel {
        DashboardListadoVM(obtenerDashboardsCU = get<ObtenerDashboardCU>())
    }


    viewModel {
        DetalleDashboardVM(
            cargarDashboardCU = get<CargarDashboardCU>(),
            eliminarDashboardCU = get<EliminarDashboardCU>(),
            guardarDashboardCU = get<GuardarDashboardCU>()
        )
    }
}

