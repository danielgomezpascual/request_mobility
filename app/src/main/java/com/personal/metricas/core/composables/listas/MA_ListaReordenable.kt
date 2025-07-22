package com.personal.metricas.core.composables.listas
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.metricas.paneles.ui.entidades.PanelUI
import kotlinx.coroutines.Job
import kotlin.math.roundToInt

// Asumo que PanelUI tiene una propiedad 'id' que es un String único y estable
// data class PanelUI(val id: String, val titulo: String, var seleccionado: Boolean)

@Composable
fun MA_ListaReordenable_EstiloYouTube(
    data: List<PanelUI>,
    itemContent: @Composable (item: PanelUI, isDragging: Boolean) -> Unit, // 'isDragging' para que el itemContent pueda cambiar su apariencia
    onItemClick: (item: PanelUI) -> Unit,
    onListReordered: (reorderedList: List<PanelUI>) -> Unit,
    itemHeight: Dp, // Altura de cada ítem, crucial para los cálculos
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState() // Permite control y observación del scroll
) {
    var items by remember { mutableStateOf<List<PanelUI>>(emptyList()) }
    val density = LocalDensity.current
    val itemHeightPx = remember(itemHeight) { with(density) { itemHeight.toPx() } }

    LaunchedEffect(data) {
        if (items != data) {
            items = data
        }
    }

    var draggingItemId by remember { mutableStateOf<String?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    // Índice del slot original del ítem arrastrado
    var initialDraggingItemIndex by remember { mutableStateOf<Int?>(null) }
    // Índice del slot sobre el que el ítem arrastrado está actualmente (donde se soltaría)
    var currentDropIndex by remember { mutableStateOf<Int?>(null) }

    // Offset Y absoluto del inicio del arrastre, relativo al viewport de LazyColumn
    // Esto ayuda a calcular la posición real del puntero dentro de la lista.
    var dragStartPointerOffsetYInList by remember { mutableFloatStateOf(0f) }


    val coroutineScope = rememberCoroutineScope()
    var dragJob by remember { mutableStateOf<Job?>(null) }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp) // El espaciado lo manejaremos con el itemHeight
    ) {
        itemsIndexed(items, key = { _, listItem -> listItem.id }) { currentIndexInList, listItem ->
            val isCurrentlyDraggingThisItem = listItem.id.toString() == draggingItemId

            // --- Lógica para el desplazamiento de ítems que NO se están arrastrando (efecto "hueco") ---
            val nonDraggedItemTranslationYTarget by remember(initialDraggingItemIndex, currentDropIndex, isCurrentlyDraggingThisItem, currentIndexInList) {
                derivedStateOf {
                    if (!isCurrentlyDraggingThisItem && initialDraggingItemIndex != null && currentDropIndex != null) {
                        val initialIdx = initialDraggingItemIndex!!
                        val currentTargetSlotIdx = currentDropIndex!!

                        when {
                            // Ítem arrastrado se mueve HACIA ABAJO, pasando por encima de este ítem
                            initialIdx < currentTargetSlotIdx && currentIndexInList in (initialIdx + 1)..currentTargetSlotIdx -> -itemHeightPx
                            // Ítem arrastrado se mueve HACIA ARRIBA, pasando por debajo de este ítem
                            initialIdx > currentTargetSlotIdx && currentIndexInList in currentTargetSlotIdx until initialIdx -> itemHeightPx
                            else -> 0f
                        }
                    } else {
                        0f
                    }
                }
            }
            val animatedNonDraggedItemTranslationY by animateFloatAsState(
                targetValue = nonDraggedItemTranslationYTarget,
                animationSpec = tween(durationMillis = 200), // Animación suave para el hueco
                label = "nonDraggedItemOffset-${listItem.id}"
            )

            Box(
                modifier = Modifier
                    .clickable(onClick = { onItemClick(listItem) })
                    .fillMaxWidth()
                    .graphicsLayer {
                        if (isCurrentlyDraggingThisItem) {
                            translationY = dragOffset.y
                            shadowElevation = 8.dp.toPx()
                            alpha = 0.9f // Un poco más opaco que antes
                            // Podrías añadir un ligero scaleX o scaleY si quieres
                            // scaleX = 1.03f
                            // scaleY = 1.03f
                        } else {
                            translationY = animatedNonDraggedItemTranslationY
                        }
                    }
                    .pointerInput(listItem.id) { // Usar listItem.id como clave para reiniciar si el ítem cambia
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset ->
                                dragJob?.cancel()
                                val currentItemActualIndex = items.indexOfFirst { it.id == listItem.id }
                                if (currentItemActualIndex != -1) {
                                    val initialItemInfo = listState.layoutInfo.visibleItemsInfo.find { it.index == currentItemActualIndex }
                                    val initialItemOffsetWithinViewport = initialItemInfo?.offset ?: 0
                                    val listScrollOffset = listState.firstVisibleItemScrollOffset

                                    dragStartPointerOffsetYInList = listScrollOffset + initialItemOffsetWithinViewport + offset.y

                                    initialDraggingItemIndex = currentItemActualIndex
                                    currentDropIndex = currentItemActualIndex
                                    draggingItemId = listItem.id.toString()
                                    dragOffset = Offset.Zero
                                }
                                // App.log.d("onDragStart: itemId=${listItem.id}, initialIndex=$initialDraggingItemIndex, pointerOffsetInList=$dragStartPointerOffsetYInList")
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount

                                initialDraggingItemIndex?.let {
                                    // Calcular la posición Y actual del puntero dentro de toda la lista
                                    val currentPointerYAbsolute = dragStartPointerOffsetYInList + dragOffset.y

                                    // Estimar el nuevo índice de "drop" basándose en la posición del puntero
                                    // Cada "slot" de ítem ocupa itemHeightPx
                                    val newDropIdx = (currentPointerYAbsolute / itemHeightPx)
                                        .roundToInt()
                                        .coerceIn(0, items.size -1) // Coercer al rango de índices válidos de la lista

                                    if (newDropIdx != currentDropIndex) {
                                        currentDropIndex = newDropIdx
                                        // App.log.d("onDrag: currentDropIndex updated to $newDropIdx")
                                    }
                                }
                            },
                            onDragEnd = {
                                dragJob?.cancel()
                                if (draggingItemId != null && initialDraggingItemIndex != null && currentDropIndex != null) {
                                    val finalDropIndex = currentDropIndex!!
                                    val originalIndex = initialDraggingItemIndex!!

                                    if (originalIndex != finalDropIndex) {
                                        val mutableList = items.toMutableList()
                                        val draggedItem = mutableList.removeAt(originalIndex)
                                        mutableList.add(finalDropIndex, draggedItem)
                                        val newList = mutableList.toList()
                                        items = newList // Actualiza el estado interno primero
                                        onListReordered(newList) // Luego notifica al exterior
                                        // App.log.d("onDragEnd: Reordered. From $originalIndex to $finalDropIndex.")
                                    } else {
                                        // App.log.d("onDragEnd: No reorder needed. Item returned to original slot.")
                                    }
                                }
                                // Resetear estados
                                draggingItemId = null
                                initialDraggingItemIndex = null
                                currentDropIndex = null
                                dragOffset = Offset.Zero
                                dragStartPointerOffsetYInList = 0f
                            },
                            onDragCancel = {
                                dragJob?.cancel()
                                // Resetear estados
                                draggingItemId = null
                                initialDraggingItemIndex = null
                                currentDropIndex = null
                                dragOffset = Offset.Zero
                                dragStartPointerOffsetYInList = 0f
                                // App.log.d("onDragCancel")
                            }
                        )
                    }
            ) {
                // Pasar 'isCurrentlyDraggingThisItem' para que el contenido del ítem pueda reaccionar
                itemContent(listItem, isCurrentlyDraggingThisItem)
            }
        }
    }
}