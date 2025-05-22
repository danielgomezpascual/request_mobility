package com.personal.requestmobility.menu.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.personal.requestmobility.core.composables.botones.BotonNormal
import com.personal.requestmobility.menu.navegacion.Modulos

@Composable
fun ScreenMenu(accion: (Modulos) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Menu")
        BotonNormal("Peticiones") { accion(Modulos.Transacciones) }
        BotonNormal("Kpis") { accion(Modulos.Kpis) }
        BotonNormal("Dashboard") { accion(Modulos.Dashboards) }
    }
}