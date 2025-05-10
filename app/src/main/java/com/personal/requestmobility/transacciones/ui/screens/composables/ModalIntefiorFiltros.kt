package com.personal.requestmobility.transacciones.ui.screens.composables

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.personal.requestmobility.core.composables.modales.MiBottomSheet
import com.personal.requestmobility.transacciones.ui.entidades.TransaccionesUI
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalInferiorFiltros(contenido: @Composable () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope() // Se mantiene dentro del componente

    Button(onClick = { scope.launch { sheetState.show() } }) { Text("Abrir") }

    MiBottomSheet(
        sheetState,
        onClose = {
            { scope.launch { sheetState.hide() } }
        },
        contenido = {
            contenido()

        }
    )
}
