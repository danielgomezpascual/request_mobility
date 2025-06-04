package com.personal.requestmobility.core.composables.listas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun MA_ListaReordenable(data: List<PanelUI>, itemContent: @Composable (PanelUI) -> Unit, onClick: (PanelUI) -> Unit) {
    var items by remember { mutableStateOf<List<PanelUI>>(data) }

    // Estado para el elemento que se está arrastrando
    var draggingItemIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    val coroutineScope = rememberCoroutineScope()
    var dragJob by remember { mutableStateOf<Job?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            val isDragging = index == draggingItemIndex


            Box(
                modifier = Modifier
                    .clickable(onClick = { onClick(item) })
                    .fillMaxWidth()
                    .graphicsLayer { // Aplicamos transformaciones gráficas
                        if (isDragging) {
                            translationY = dragOffset.y
                            shadowElevation = 8.dp.toPx() // Un poco de sombra mientras se arrastra
                            alpha = 0.8f // Un poco de transparencia
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset ->
                                dragJob?.cancel() // Cancela cualquier trabajo de arrastre anterior
                                dragJob = coroutineScope.launch {
                                    draggingItemIndex = index
                                    dragOffset = Offset.Zero // Reinicia el offset local al inicio
                                }
                            },
                            onDragEnd = {
                                dragJob?.cancel()
                                draggingItemIndex?.let { startIndex ->
                                    val currentList = items.toMutableList()
                                    val draggedItem = currentList.removeAt(startIndex)
                                    val targetIndex = (startIndex + (dragOffset.y / 100).toInt()).coerceIn(0, currentList.size)
                                    currentList.add(targetIndex, draggedItem)
                                    items = currentList
                                }
                                draggingItemIndex = null
                                dragOffset = Offset.Zero
                            },
                            onDragCancel = {
                                dragJob?.cancel()
                                draggingItemIndex = null
                                dragOffset = Offset.Zero
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount


                            }
                        )
                    }
            ) {
                itemContent(item)
            }
        }
    }
}


@Composable
fun Box(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}