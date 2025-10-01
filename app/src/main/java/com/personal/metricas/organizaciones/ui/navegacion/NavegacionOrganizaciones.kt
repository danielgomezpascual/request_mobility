package com.personal.metricas.organizaciones.ui.navegacion

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.organizaciones.ui.detalle.ScreenDetalleOrganizacion
import com.personal.metricas.organizaciones.ui.lista.ListaOrganizaciones
import com.personal.metricas.paneles.navegacion.ScreenDetallePanel
import com.personal.metricas.paneles.ui.screen.detalle.DetallePanelScreen
import com.personal.metricas.sincronizacion.ui.SincronizacionMenuScreen
import com.personal.metricas.sincronizacion.ui.lista.ListaOrganizacinesSincronizar

fun NavGraphBuilder.NavegacionOrganizaciones(navController: NavHostController) {

    composable<ScreenOrganizacionesLista> {
        ListaOrganizaciones (){navegacion ->
            goto(navegacion, navController)
        }
    }



    composable<ScreenDetalleOrganizacionSincronizacion> { bk ->

        val screenDetalle: ScreenDetalleOrganizacionSincronizacion = bk.toRoute()
        ScreenDetalleOrganizacion(screenDetalle.organizationCode) { navegacion ->
            goto(navegacion, navController)
        }
    }




}
