package com.personal.requestmobility.menu.navegacion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personal.danility2.usuarios.navegacion.DockTransacciones

import com.personal.requestmobility.menu.screen.ScreenMenu

fun NavGraphBuilder.NavegavionMenu(navController: NavController) {
    composable<ScreenMenu> {
        ScreenMenu() { modulo ->
            when (modulo) {
                Modulos.Transacciones -> navController.navigate(DockTransacciones)

            }
        }
    }
}