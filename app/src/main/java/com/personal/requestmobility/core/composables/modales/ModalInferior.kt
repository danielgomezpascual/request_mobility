package com.personal.requestmobility.core.composables.modales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.log.domain.MyLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiBottomSheet(sheetState: SheetState, onClose: () -> Unit, contenido: @Composable () -> Unit) {
    if (sheetState.isVisible) {
        ModalBottomSheet(

            onDismissRequest = {
                /*App.log.d("SSh: Cerrandoi")
                scope.launch { sheetState.hide() }*/
                onClose()
            },
            sheetState = sheetState,
            content = {
                AnimatedVisibility( // Envuelve el contenido del sheet con AnimatedVisibility
                    visible = sheetState.isVisible,
                    enter = slideInVertically(initialOffsetY = { it }), // Animación de entrada
                    exit = slideOutVertically(targetOffsetY = { it }) // Animación de salida
                ) {

                    contenido()
                }
            }
        )
    }


}

