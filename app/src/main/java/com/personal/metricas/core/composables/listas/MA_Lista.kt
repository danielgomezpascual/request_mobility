package com.personal.metricas.core.composables.listas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> MA_Lista(
        data: List<T>,
        modifier: Modifier = Modifier.fillMaxWidth(),
        itemContent: @Composable (T) -> Unit
) {
    // val its = remember { mutableStateOf(data) }

    if (data.isEmpty()) {
        Box(modifier = Modifier.Companion.fillMaxSize()) { MA_NoData() }
    } else {
        LazyColumn(modifier = modifier) {
            items(items = data) { elememto -> itemContent(elememto) }
        }
    }
}
