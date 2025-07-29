package com.personal.metricas.dashboards.navegacion


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.personal.metricas.App
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.dashboards.ui.screen.cuadricula.CuadriculDashboardUI
import com.personal.metricas.dashboards.ui.screen.detalle.DetalleDashboardUI
import com.personal.metricas.dashboards.ui.screen.listado.DashboardListadoUI
import com.personal.metricas.dashboards.ui.screen.visualizador.VisualizadorDashboardUI
import com.personal.metricas.endpoints.navegacion.ScreenDetalleEndPoints
import com.personal.metricas.endpoints.navegacion.ScreenListadoEndPoints
import com.personal.metricas.kpi.navegacion.ScreenDetalleKpi
import com.personal.metricas.kpi.navegacion.ScreenListadoKpis
import com.personal.metricas.menu.navegacion.ScreenHerramientas
import com.personal.metricas.menu.navegacion.ScreenMenu
import com.personal.metricas.paneles.navegacion.ScreenDetallePanel
import com.personal.metricas.paneles.navegacion.ScreenListadoPaneles
import com.personal.metricas.sincronizacion.ui.navegacion.ScreenMenuSincronizacion
import com.personal.metricas.sincronizacion.ui.navegacion.ScreenOrganizacionesSincronizacion

fun NavGraphBuilder.NavegacionDashboard(navController: NavController) {
	composable<CuadriculaDashboards> {
		CuadriculDashboardUI() { navegacion ->
			goto(navegacion, navController)
		}
	}


	composable<VisualizadorDashboard> { backStackEntry ->
		val visualizadorDashboardRoute: VisualizadorDashboard = backStackEntry.toRoute<VisualizadorDashboard>()
		VisualizadorDashboardUI(visualizadorDashboardRoute.id,
								visualizadorDashboardRoute.parametrosJson) { navegacion ->
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

	App.log.d("Nasvegacion")

	when (navegacion) {
		is EventosNavegacion.Cargar                 -> navController.navigate(DetalleDashboard(navegacion.identificador))

		EventosNavegacion.MenuApp                   -> {
			navController.navigate(ScreenMenu) { // ScreenMenu debe ser un objeto serializable o una ruta String
				popUpTo(navController.graph.startDestinationId) { // Ejemplo: pop hasta el inicio del grafo actual
					inclusive = true
				}
			}
		}

		EventosNavegacion.Volver                    -> {
			navController.navigate(ListadoDashboards) {
				popUpTo<ListadoDashboards>() { inclusive = true }
			}
		}


		is EventosNavegacion.VisualizadorDashboard  -> {
			navController.navigate(VisualizadorDashboard(navegacion.identificador,
														 navegacion.parametrosJson))
		}

		//====== KPI =====
		EventosNavegacion.MenuKpis                  -> navController.navigate(ScreenListadoKpis)
		EventosNavegacion.NuevoKPI                  -> navController.navigate(ScreenDetalleKpi(0))
		is EventosNavegacion.CargarKPI              -> {

			App.log.d("Avbrirr detalle")
			navController.navigate(
				ScreenDetalleKpi(navegacion.identificador)
			)
		}


		//======= Paneles =========
		EventosNavegacion.MenuPaneles               -> navController.navigate(ScreenListadoPaneles)
		EventosNavegacion.NuevoPanel                -> navController.navigate(ScreenDetallePanel(0))
		is EventosNavegacion.CargarPanel            -> navController.navigate(ScreenDetallePanel(navegacion.identificador))

		//==== Dashboard =======================
		EventosNavegacion.MenuDashboard             -> navController.navigate(ListadoDashboards)
		EventosNavegacion.NuevoDashboard            -> navController.navigate(DetalleDashboard(0))
		is EventosNavegacion.CargarDashboard  -> navController.navigate(DetalleDashboard(navegacion.identificador))
		EventosNavegacion.CuadriculaDashboard -> navController.navigate(CuadriculaDashboards)


		//==== Sincronizacion =======================
		EventosNavegacion.Sincronizacion            -> navController.navigate(ScreenOrganizacionesSincronizacion)


		//==== Herramientas =======================
		EventosNavegacion.MenuHerramientas          -> navController.navigate(ScreenHerramientas)


		//==== End Points =======================
		is EventosNavegacion.CargarEndPoint         -> navController.navigate(ScreenDetalleEndPoints(navegacion.identificador))
		EventosNavegacion.MenuEndPoints             -> navController.navigate(ScreenListadoEndPoints)
		EventosNavegacion.NuevoEndPonint            -> navController.navigate(ScreenDetalleEndPoints(0))

		EventosNavegacion.SincronizacionMenu        -> navController.navigate(ScreenMenuSincronizacion)
	}
}