package com.personal.requestmobility.core.composables.labels

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MA_LabelMini(
    valor: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    alineacion : TextAlign = TextAlign.Unspecified,
    fontStyle: FontStyle = FontStyle.Normal,
    icono: Icons? = null,

) {
    Text(text = valor,
         modifier = modifier.padding(1.dp), color = color,
         fontSize = 10.sp,
        
        /*style = MaterialTheme.typography.bodySmall,*/ textAlign = alineacion,
        )
}