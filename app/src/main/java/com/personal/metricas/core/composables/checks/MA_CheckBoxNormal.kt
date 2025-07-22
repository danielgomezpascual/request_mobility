package com.personal.metricas.core.composables.checks

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MA_CheckBoxNormal(valor: Boolean,
                      titulo: String,
                      modifier: Modifier = Modifier.Companion,
                      onValueChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier

            .padding(8.dp)
    ) {
        Checkbox(
            checked = valor,
            onCheckedChange = { onValueChange(it) },
            enabled = true
        )
        Text(titulo)
    }
}