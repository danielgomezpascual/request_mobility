package com.personal.metricas.core.composables.edittext

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun MA_TextoNormal(
	valor: String, titulo: String,
	modifier: Modifier = Modifier.Companion,
	icono: Icons? = null,
	onValueChange: (String) -> Unit,
) {
	Row(
		modifier = modifier.padding(4.dp),
		verticalAlignment = Alignment.Companion.CenterVertically
	) {
		if (icono != null) {
			Icon(imageVector = Icons.Default.Home, contentDescription = titulo)
		}

		var localTextFieldValue by remember {
			mutableStateOf(TextFieldValue(text = valor))
		}

// Este LaunchedEffect es la clave para la sincronización
		LaunchedEffect(valor) {
			if (valor != localTextFieldValue.text) {
				localTextFieldValue = localTextFieldValue.copy(text = valor, selection = TextRange(valor.length))
			}
		}

		TextField(

			value = localTextFieldValue,
			//lue = valor,
			onValueChange = { nv ->
				localTextFieldValue = nv
				onValueChange(nv.text)

			},
			label = { Text(titulo) },
			readOnly = false,
			modifier = modifier
				.padding(4.dp),
			colors = TextFieldDefaults.colors(
				unfocusedContainerColor = Color.Transparent,
				unfocusedIndicatorColor = Color.LightGray,
				disabledIndicatorColor = Color.LightGray
			)


		)
	}


}