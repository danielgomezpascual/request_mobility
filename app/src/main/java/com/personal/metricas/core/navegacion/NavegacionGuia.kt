package com.personal.metricas.core.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.personal.metricas.App
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.navegacion.NavegacionDashboard
import com.personal.metricas.endpoints.navegacion.NavegacionEndPoints
import com.personal.metricas.kpi.navegacion.NavegacionKpis
import com.personal.metricas.menu.navegacion.NavegavionMenu
import com.personal.metricas.menu.navegacion.ScreenMenu
import com.personal.metricas.organizaciones.ui.lista.ListaOrganizaciones
import com.personal.metricas.organizaciones.ui.navegacion.NavegacionOrganizaciones
import com.personal.metricas.organizaciones.ui.navegacion.ScreenOrganizacionesLista
import com.personal.metricas.paneles.navegacion.NavegacionPaneles
import com.personal.metricas.settings.navegacion.NavegacionSettings
import com.personal.metricas.sincronizacion.ui.navegacion.NavegacionSincronizacion
import com.personal.metricas.start.navegacion.NavegacionStart
import com.personal.metricas.start.navegacion.ScreenStart
import com.personal.metricas.transacciones.navegacion.NavegacionTransacciones

@Composable
fun NavegacionGuia() {
    val navController = rememberNavController()
    App.navController = navController
    //NavHost(navController = navController, startDestination = ScreenMenu) {
    val inicioConfigurador  = App.sharedPrerfences.get<Boolean>(Preferencias.CONFIGURACION_INICIAL, true)

    NavHost(navController = navController, startDestination = if3(inicioConfigurador, ScreenStart, ScreenMenu) ){
        NavegacionStart(navController)
        NavegavionMenu(navController)
        NavegacionTransacciones(navController)
        NavegacionKpis(navController)
        NavegacionPaneles(navController)
        NavegacionSincronizacion(navController)
        NavegacionDashboard(navController)
        NavegacionEndPoints(navController)
        NavegacionSettings(navController)
        NavegacionOrganizaciones(navController)
    }
}