package com.personal.metricas.core.composables.edittext

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.metricas.App

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_TextBuscador(searchText: String, onSearchTextChanged: (String) -> Unit) {
    TextField(
        singleLine =  true,
        maxLines = 1,
        value = searchText,
        onValueChange = { str ->
            App.log.d("Str: $str")
            onSearchTextChanged(str)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),

        placeholder = { Text("Buscar...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        shape = RoundedCornerShape(5.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFE0E0FF), // Un lila claro para el fondo enfocado
            unfocusedContainerColor = Color(0xFFF0F0F0), // Un gris claro para el fondo no enfocado)
        )
    )
}