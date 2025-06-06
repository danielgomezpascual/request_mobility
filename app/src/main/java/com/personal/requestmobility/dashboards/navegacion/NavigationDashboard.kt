package com.personal.requestmobility.dashboards.navegacion


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.personal.requestmobility.App
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.dashboards.ui.screen.cuadricula.CuadriculDashboardUI
import com.personal.requestmobility.dashboards.ui.screen.detalle.DetalleDashboardUI
import com.personal.requestmobility.dashboards.ui.screen.listado.DashboardListadoUI
import com.personal.requestmobility.dashboards.ui.screen.visualizador.VisualizadorDashboardUI
import com.personal.requestmobility.kpi.navegacion.ScreenDetalleKpi
import com.personal.requestmobility.kpi.navegacion.ScreenListadoKpis
import com.personal.requestmobility.kpi.ui.screen.detalle.DetalleKpiScreen
import com.personal.requestmobility.kpi.ui.screen.listado.KpisListadoScreen
import com.personal.requestmobility.menu.navegacion.ScreenMenu
import com.personal.requestmobility.paneles.navegacion.ScreenDetallePanel
import com.personal.requestmobility.paneles.navegacion.ScreenListadoPaneles

fun NavGraphBuilder.NavegacionDashboard(navController: NavController) {
    composable<CuadriculaDashboards> {
        CuadriculDashboardUI() { navegacion ->
            goto(navegacion, navController)
        }
    }


    composable<VisualizadorDashboard> { backStackEntry ->
        val visualizadorDashboardRoute: VisualizadorDashboard = backStackEntry.toRoute<VisualizadorDashboard>()
        VisualizadorDashboardUI(visualizadorDashboardRoute.id) { navegacion ->
            goto(navegacion, navController)
        }
    }


    composable<ListadoDashboards> {
        DashboardListadoUI() { navegacion ->
            goto(navegacion, navController)
        }
    }

    composable<DetalleDashboard> { backStackEntry ->
        val detalleDashboardRoute: DetalleDashboard = backStackEntry.toRoute<DetalleDashboard>()
        DetalleDashboardUI(detalleDashboardRoute.id) { navegacion ->
            goto(navegacion, navController)
        }
    }
}

// Esta función goto es específica para la navegación de este módulo,
// basándose en el ejemplo proporcionado.
fun goto(navegacion: EventosNavegacion, navController: NavController) {
    when (navegacion) {
        is EventosNavegacion.Cargar -> navController.navigate(DetalleDashboard(navegacion.identificador))

        EventosNavegacion.MenuApp -> {
            navController.navigate(ScreenMenu) { // ScreenMenu debe ser un objeto serializable o una ruta String
                popUpTo(navController.graph.startDestinationId) { // Ejemplo: pop hasta el inicio del grafo actual
                    inclusive = true
                }
            }
        }

        EventosNavegacion.Volver -> {
            navController.navigate(ListadoDashboards) {
                popUpTo<ListadoDashboards>() { inclusive = true }
            }
        }


        is EventosNavegacion.VisualizadorDashboard -> {
            navController.navigate(VisualizadorDashboard(navegacion.identificador))
        }

        //====== KPI =====
        EventosNavegacion.MenuKpis -> navController.navigate(ScreenListadoKpis)
        EventosNavegacion.NuevoKPI -> navController.navigate(ScreenDetalleKpi(0))
        is EventosNavegacion.CargarKPI -> navController.navigate(ScreenDetalleKpi(navegacion.identificador))


        //======= Paneles =========
        EventosNavegacion.MenuPaneles -> navController.navigate(ScreenListadoPaneles)
        EventosNavegacion.NuevoPanel -> navController.navigate(ScreenDetallePanel(0))
        is EventosNavegacion.CargarPanel -> {
            App.log.d("Cargamos.-->" + navegacion.identificador)


            navController.navigate(ScreenDetallePanel(navegacion.identificador))
        }


        //==== Dashboard =======================
        EventosNavegacion.MenuDashboard ->      navController.navigate(ListadoDashboards)
        EventosNavegacion.NuevoDashboard ->   navController.navigate(DetalleDashboard(0))
        is EventosNavegacion.CargarDashboard -> {
            navController.navigate(DetalleDashboard(navegacion.identificador))
        }
        EventosNavegacion.MenuVisualizadorDashboard -> navController.navigate(CuadriculaDashboards)
    }
}