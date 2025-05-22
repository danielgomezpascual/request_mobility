package com.personal.requestmobility.dashboards.ui.screen.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.scaffold.ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI

import org.koin.androidx.compose.koinViewModel

@Composable
fun DetalleDashboardUI(
    identificador: Int,
    viewModel: DetalleDashboardVM = koinViewModel(),
    navegacion: (EventosNavegacion) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // LaunchedEffect con Unit en el ejemplo, pero es mejor usar identificador como key
    // si puede cambiar y queremos recargar. El ejemplo usa Unit.
    LaunchedEffect(Unit) { // Siguiendo el ejemplo
        viewModel.onEvento(DetalleDashboardVM.Eventos.Cargar(identificador))
    }
    // Si el identificador pudiera cambiar mientras la pantalla está en la pila,
    // LaunchedEffect(identificador) { ... } sería más robusto.

    when (val state = uiState) { // Renombrado uiState a state
        is DetalleDashboardVM.UIState.Error -> ErrorScreen(state.mensaje)
        is DetalleDashboardVM.UIState.Loading -> LoadingScreen(state.mensaje)
        is DetalleDashboardVM.UIState.Success -> DetalleDashboardUIScreen( // Nombre corregido
            viewModel = viewModel,
            dashboardUI = state.dashboardUI, // Pasando el objeto correcto
            navegacion = navegacion
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleDashboardUIScreen( // Nombre corregido del Composable de éxito
    viewModel: DetalleDashboardVM,
    dashboardUI: DashboardUI,
    navegacion: (EventosNavegacion) -> Unit
) {
    ScaffoldGenerico(
        titulo = if (dashboardUI.id == 0) "Nuevo Dashboard" else "Datos Dashboard", // Título adaptado
        // 'navegacion' en ScaffoldGenerico del ejemplo original es la acción del icono de navegación del TopAppBar
        navegacion = { navegacion(EventosNavegacion.Volver) }, // Adaptado para claridad, asume que ScaffoldGenerico tiene 'navigateUp'
        contenidoBottomBar = {
            BottomAppBar {
                IconButton(onClick = { navegacion(EventosNavegacion.Volver) }) {
                    Icon(Icons.Default.Menu, contentDescription = "Listado") // Icono como en el ejemplo
                }
                Spacer(Modifier.weight(1f)) // Para empujar los siguientes iconos a la derecha si es necesario
                IconButton(onClick = {
                    viewModel.onEvento(DetalleDashboardVM.Eventos.Eliminar(dashboardUI, navegacion))
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar")
                }
                IconButton(onClick = {
                    viewModel.onEvento(DetalleDashboardVM.Eventos.Guardar(dashboardUI, navegacion))
                }) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Guardar")
                }
            }
        },
        contenido = {
            Column(
                modifier = Modifier

                    .padding(horizontal = 16.dp) // Padding adicional para el contenido interno
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Para que el contenido sea scrollable
            ) {
                Spacer(modifier = Modifier.height(16.dp)) // Espacio superior

                // ID: Mostrar solo si es un dashboard existente, no editable
                if (dashboardUI.id != 0) {
                    MA_TextoNormal(
                        valor = dashboardUI.id.toString(),
                        titulo = "ID",
                        onValueChange = { /* No editable, función vacía o null como en el ejemplo */ },

                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                MA_TextoNormal(
                    valor = dashboardUI.nombre,
                    titulo = "Nombre", // Equivalente a "Item"
                    onValueChange = { valor ->
                        viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeNombre(valor))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // No hay CheckBoxNormal para "Global" en Dashboard

                MA_TextoNormal(
                    valor = dashboardUI.descripcion,
                    titulo = "Descripción", // Equivalente a "Proveedor"
                    onValueChange = { valor ->
                        viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeDescripcion(valor))
                    },

                )
                // No hay más campos como "Codigo Organizacion" o "Codigo" para Dashboard
                Spacer(modifier = Modifier.height(16.dp)) // Espacio inferior
            }
        }
    )
}