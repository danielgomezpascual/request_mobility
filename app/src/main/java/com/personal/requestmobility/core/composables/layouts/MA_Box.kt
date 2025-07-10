package com.personal.requestmobility.core.composables.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun MA_Box(modifier: Modifier = Modifier, contenido: @Composable () -> Unit) {

    Box(modifier.padding(vertical = 3.dp)) {
        contenido()
    }
}