package com.personal.metricas.sincronizacion.ui.navegacion

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.menu.navegacion.ScreenHerramientas
import com.personal.metricas.menu.screen.HerramientasScreen
import com.personal.metricas.sincronizacion.ui.SincronizacionMenuScreen
import com.personal.metricas.sincronizacion.ui.lista.ListaOrganizacinesSincronizar

fun NavGraphBuilder.NavegacionSincronizacion(navController: NavHostController) {

    composable<ScreenOrganizacionesSincronizacion> {
        ListaOrganizacinesSincronizar(){navegacion ->
            goto(navegacion, navController)
        }
    }



    composable<ScreenMenuSincronizacion> {
        SincronizacionMenuScreen() { navegacion ->
            goto(navegacion, navController)
        }
    }


}
