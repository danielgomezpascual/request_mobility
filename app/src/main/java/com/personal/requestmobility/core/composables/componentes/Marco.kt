package com.personal.requestmobility.core.composables.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.labels.LabelTituloTabla


@Composable
fun Marco(modifier: Modifier = Modifier, titulo: String = "", componente: @Composable () -> Unit) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(3.dp)
            .border(1.dp, Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        /* modifier = Modifier.fillMaxWidth()*/
    ) {
        LabelTituloTabla(
            titulo,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), alineacion = TextAlign.Center
        )

        componente()
    }

}