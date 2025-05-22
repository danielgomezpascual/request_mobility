package com.personal.requestmobility.core.composables.listas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.personal.requestmobility.App

@Composable
fun <T> MA_ListaColumn(data: List<T>, itemContent: @Composable (T) -> Unit) {
    Column (modifier = Modifier.Companion.fillMaxWidth()) {
       data.forEach {
           App.log.d(it)
           itemContent(it)
       }
        // items(items = data) { elememto ->

       // }
    }
}