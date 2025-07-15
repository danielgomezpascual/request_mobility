package com.personal.requestmobility.paneles.navegacion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.personal.requestmobility.dashboards.navegacion.goto
import com.personal.requestmobility.paneles.ui.screen.detalle.DetallePanelScreen
import com.personal.requestmobility.paneles.ui.screen.listado.PanelesListadoScreen

fun NavGraphBuilder.NavegacionPaneles(navController: NavController) {

	composable<ScreenListadoPaneles> {
		PanelesListadoScreen() { navegacion ->
			goto(navegacion, navController)
		}
	}
	composable<ScreenDetallePanel> { bk ->

		val screenDetalle: ScreenDetallePanel = bk.toRoute()
		DetallePanelScreen(screenDetalle.id) { respuestaAccion ->
			// vuelve a la indicada...
			/*navController.navigate(ScreenListadoPaneles) {
				popUpTo<ScreenListadoPaneles>() { inclusive = true }
			}*/
			goto(respuestaAccion ,navController)
		}
	}
}