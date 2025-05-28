package com.personal.requestmobility.dashboards.navegacion


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.dashboards.ui.screen.cuadricula.CuadriculDashboardUI
import com.personal.requestmobility.dashboards.ui.screen.detalle.DetalleDashboardUI
import com.personal.requestmobility.dashboards.ui.screen.listado.DashboardListadoUI
import com.personal.requestmobility.dashboards.ui.screen.visualizador.VisualizadorDashboardUI
import com.personal.requestmobility.menu.navegacion.ScreenMenu

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
        is EventosNavegacion.Cargar ->
            navController.navigate(DetalleDashboard(navegacion.identificador))

        EventosNavegacion.MenuApp -> {
            // Asumiendo que ScreenMenu es un destino serializable también
            // navController.popBackStack() // Opcional, depende de cómo quieras la pila
            navController.navigate(ScreenMenu) { // ScreenMenu debe ser un objeto serializable o una ruta String
                popUpTo(navController.graph.startDestinationId) { // Ejemplo: pop hasta el inicio del grafo actual
                    inclusive = true
                }
                // O si ScreenMenu es parte de otro grafo y quieres limpiar hasta él:
                // popUpTo<ScreenMenuRouteType>() { inclusive = true } // Si ScreenMenu es un KClass o object serializable
            }
        }

        EventosNavegacion.Volver -> {
            // Volver a la lista de dashboards
            navController.navigate(ListadoDashboards) {
                popUpTo<ListadoDashboards>() { inclusive = true }
            }
        }

        is EventosNavegacion.VisualizadorDashboard -> {
            navController.navigate(VisualizadorDashboard(navegacion.identificador))
        }
        // Considera añadir un 'else' o manejar todos los casos posibles de EventosNavegacion
    }
}