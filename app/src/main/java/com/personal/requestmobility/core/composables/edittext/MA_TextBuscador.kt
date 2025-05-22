package com.personal.requestmobility.core.composables.edittext

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_TextBuscador(searchText: String, onSearchTextChanged: (String) -> Unit) {
    OutlinedTextField(
        maxLines = 1,
        value = searchText,
        onValueChange = { str ->
            App.log.d("Str: $str")
            onSearchTextChanged(str)
        },
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Buscar...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        shape = RoundedCornerShape(24.dp),

    )
}