package com.personal.metricas.core.composables.listas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> MA_Lista(data: List<T>, itemContent: @Composable (T) -> Unit) {
   // val its = remember { mutableStateOf(data) }

    if (data.isEmpty()){
       MA_NoData()


    }else {
        LazyColumn(modifier = Modifier.Companion.fillMaxWidth()) {


            items(items = data) { elememto ->
                itemContent(elememto)
            }
        }
    }
}