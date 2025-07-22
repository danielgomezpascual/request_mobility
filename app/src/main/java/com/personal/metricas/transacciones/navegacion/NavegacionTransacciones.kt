package com.personal.metricas.transacciones.navegacion

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.personal.danility2.usuarios.navegacion.DockTransacciones
import com.personal.metricas.transacciones.ui.screens.listado.DockTransaccionesScreen


fun NavGraphBuilder.NavegacionTransacciones(navController: NavHostController) {

    composable<DockTransacciones> {
        DockTransaccionesScreen()
    }


}
