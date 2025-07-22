package com.personal.metricas.core.composables.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.labels.MA_Titulo2

@Composable
fun <T> MA_Columnas(modifier: Modifier = Modifier, data: List<T>, columnas: Int = 2, contenido: @Composable (T) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnas), // Define exactamente 2 columnas
        modifier = modifier.fillMaxWidth(),
        //contentPadding = PaddingValues(8.dp), // Espaciado alrededor de la cuadrícula
        verticalArrangement = Arrangement.spacedBy(4.dp), // Espaciado vertical entre items
        horizontalArrangement = Arrangement.spacedBy(4.dp) // Espaciado horizontal entre items
    ) {

        items(items = data) { ds ->
            contenido(ds)
        }


    }
}


@Composable
fun MA_Columnas(titulo: String = "", elementos: List<@Composable () -> Unit>) {

    Column(  modifier = Modifier.sizeIn(minHeight = 100.dp, maxHeight = 450.dp)) {
        MA_Titulo2(titulo)
        LazyVerticalGrid(

            columns = GridCells.Fixed(2), // Define exactamente 2 columnas
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp), // Espaciado alrededor de la cuadrícula
            verticalArrangement = Arrangement.spacedBy(8.dp), // Espaciado vertical entre items
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado horizontal entre items
        ) {

            elementos.forEach { comp ->
                item {
                    comp()
                }
            }


        }
    }

}

@Composable
fun MA_2ColumnasHorizontales(titulo: String = "", elementos: List<@Composable () -> Unit>) {
    
    Column(  modifier = Modifier.sizeIn(minHeight = 100.dp, maxHeight = 250.dp)) {
        MA_Titulo2(titulo)
        LazyHorizontalGrid(
            
            rows = GridCells.Fixed(2), // Define exactamente 2 columnas
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp), // Espaciado alrededor de la cuadrícula
            verticalArrangement = Arrangement.spacedBy(4.dp), // Espaciado vertical entre items
            horizontalArrangement = Arrangement.spacedBy(4.dp) // Espaciado horizontal entre items
                        ) {
            
            elementos.forEach { comp ->
                item {
                    comp()
                }
            }
            
            
        }
    }
    
}