package com.personal.metricas.core.composables.modales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.personal.metricas.App


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_BottomSheet(sheetState: SheetState, color : Color = Color(224, 224, 224, 255), onClose: () -> Unit, contenido: @Composable () -> Unit) {
	if (sheetState.isVisible) {
		ModalBottomSheet(
			containerColor = color ,
			onDismissRequest = {
				App.log.d("Cerrando desde el sheet")
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

