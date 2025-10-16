package com.personal.metricas.transacciones.ui.screens.composables

import MA_IconBottom
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.botones.MA_BotonSecundarioSinBorde
import com.personal.metricas.core.composables.modales.MA_BottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalInferiorFiltros(contenido: @Composable () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope() // Se mantiene dentro del componente



    MA_IconBottom(icon = Icons.Default.FilterAlt, color = Color.DarkGray, onClick = {
        scope.launch { sheetState.show() }
	})

    //MA_BotonSecundarioSinBorde(texto = "Filtrar" ) { }
    //Button(onClick = { scope.launch { sheetState.show() } }) { Text("Abrir") }

    MA_BottomSheet(
        sheetState,
                  onClose = {
            { scope.launch { sheetState.hide() } }
        },
                  contenido = {
            contenido()

        }
    )
}
