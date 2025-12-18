package com.personal.metricas.core.composables.modales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.metricas.App


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_BottomSheet(
	sheetState: SheetState,
	color: Color = Color.White,
	onClose: () -> Unit,
	contenido: @Composable () -> Unit
) {
	if (sheetState.isVisible) {
		ModalBottomSheet(
			containerColor = color,
			onDismissRequest = {
				App.log.d("Cerrando desde el sheet")
				onClose()
			},
			sheetState = sheetState,
			shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
			tonalElevation = 0.dp,
			dragHandle = {
				// Handle visual moderno
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 12.dp),
					contentAlignment = Alignment.Center
				) {
					Box(
						modifier = Modifier
							.width(40.dp)
							.height(4.dp)
							.clip(RoundedCornerShape(2.dp))
							.background(Color(0xFFE5E7EB))
					)
				}
			},
			content = {
				AnimatedVisibility(
					visible = sheetState.isVisible,
					enter = slideInVertically(initialOffsetY = { it }),
					exit = slideOutVertically(targetOffsetY = { it })
				) {
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp, vertical = 8.dp)
					) {
						contenido()
					}
				}
			}
		)
	}
}

