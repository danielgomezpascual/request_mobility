package com.personal.requestmobility.paneles.navegacion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.personal.requestmobility.dashboards.navegacion.goto
import com.personal.requestmobility.kpi.ui.screen.detalle.DetalleKpiScreen
import com.personal.requestmobility.kpi.ui.screen.listado.KpisListadoScreen
import com.personal.requestmobility.paneles.ui.screen.detalle.DetallePanelScreen
import com.personal.requestmobility.paneles.ui.screen.listado.PanelesListadoScreen

fun NavGraphBuilder.NavegacionPaneles(navController: NavController) {

    composable<ScreenListadoPaneles> {
        PanelesListadoScreen () { navegacion ->
            //navController.navigate(ScreenDetallePanel(id = panel.id))
            goto(navegacion, navController)
        }
    }
    composable<ScreenDetallePanel> { bk ->

        val screenDetalle: ScreenDetallePanel = bk.toRoute()
        DetallePanelScreen (screenDetalle.id) { respuestaAccion ->
            // vuelve a la indicada...
            navController.navigate(ScreenListadoPaneles) {
                popUpTo<ScreenListadoPaneles>() { inclusive = true }
            }
        }
    }
}