package com.personal.requestmobility.sincronizacion.ui.navegacion

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.personal.requestmobility.dashboards.navegacion.goto
import com.personal.requestmobility.paneles.navegacion.ScreenListadoPaneles
import com.personal.requestmobility.paneles.ui.screen.listado.PanelesListadoScreen
import com.personal.requestmobility.sincronizacion.ui.lista.ListaOrganizacinesSincronizar

fun NavGraphBuilder.NavegacionSincronizacion(navController: NavHostController) {

    composable<ScreenOrganizacionesSincronizacion> {
        ListaOrganizacinesSincronizar(){navegacion ->
            goto(navegacion, navController)
        }
    }


}
