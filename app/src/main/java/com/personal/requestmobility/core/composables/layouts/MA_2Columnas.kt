package com.personal.requestmobility.core.composables.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> MA_2Columnas(data: List<T>, contenido: @Composable (T) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Define exactamente 2 columnas
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp), // Espaciado alrededor de la cuadrícula
        verticalArrangement = Arrangement.spacedBy(8.dp), // Espaciado vertical entre items
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado horizontal entre items
    ) {

        items(items = data) { ds ->
            contenido(ds)
        }


    }
}