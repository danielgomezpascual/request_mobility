package com.personal.metricas.transacciones.ui.screens.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.modales.MA_BottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalInferiorFiltros(contenido: @Composable () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope() // Se mantiene dentro del componente

    TextButton(
        onClick = { scope.launch { sheetState.show() } },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.DarkGray
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
    ) {
        Icon(Icons.Default.FilterAlt, contentDescription = null, modifier = Modifier.size(16.dp))
     /*   Spacer(Modifier.width(4.dp))
        Text("Filtrar", style = MaterialTheme.typography.labelSmall)*/
    }

    MA_BottomSheet(
        sheetState,
        onClose = {
            scope.launch { sheetState.hide() }
        },
        contenido = {
            contenido()
        }
    )
}
