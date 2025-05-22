package com.personal.requestmobility.core.composables.edittext

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MA_TextoEditable(
    valor: String,
    titulo: String,
    modifier: Modifier = Modifier.Companion,
    icono: Icons? = null,
    onValueChange: (String) -> Unit,
) {
    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
        if (icono != null) {
            Icon(imageVector = Icons.Default.Home, contentDescription = titulo)
        }
        OutlinedTextField(
            value = valor,
            onValueChange = { onValueChange(it) },
            label = { Text(titulo) },
            readOnly = false,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            //  colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer)
        )
    }

}