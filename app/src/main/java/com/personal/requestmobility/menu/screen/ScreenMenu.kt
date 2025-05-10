package com.personal.requestmobility.menu.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.personal.requestmobility.core.composables.botones.BotonNormal
import com.personal.requestmobility.menu.navegacion.Modulos
import kotlinx.coroutines.launch

@Composable
fun ScreenMenu(accion: (Modulos) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Menu")
        BotonNormal("Peticiones") { accion(Modulos.Transacciones) }
    }

}

@Composable
fun Dialogo() {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Mostrar diálogo")
        }

        // El diálogo se anima hacia arriba desde abajo
        AnimatedVisibility(
            visible = showDialog,
            enter = slideInVertically(initialOffsetY = { it -> it }),
            exit = slideOutVertically(targetOffsetY = { it -> it })
        ) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    // Contenido del diálogo aquí
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Este es un diálogo animado")
                        Button(onClick = { showDialog = false }) {
                            Text("Cerrar")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetMaterial3Sample() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { scope.launch { sheetState.show() } }) {
            Text("Click to show sheet")
        }
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(
            modifier = Modifier.background(color =Color.Red),
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
            },
            //  Aquí puedes personalizar la animación (aunque con skipHalfExpanded ya es suave)
            //  sheetPeekHeight = 0.dp // Para que empiece oculta y se anime al mostrarse
        ) {
            LazyColumn {
                items(50) { it ->
                    Text("Item $it", modifier = Modifier.padding(16.dp)) // Añade padding a los items
                }
            }
        }
    }
}

