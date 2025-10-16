package com.personal.metricas.excel

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.net.Uri
import androidx.compose.foundation.layout.Column

@Composable
fun FilePickerExample() {
	var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

	// 1. Registramos el launcher para el resultado de la actividad.
	val filePickerLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent()
	) { uri: Uri? ->
		// El resultado (la Uri del fichero) se recibe aquí.
		selectedFileUri = uri
	}

	// Interfaz de usuario
	Column {
		Button(onClick = {
			// 2. Lanzamos el explorador de archivos.
			// Puedes especificar el tipo de archivo que buscas.
			filePickerLauncher.launch("*/*") // Para cualquier tipo de archivo
		}) {
			Text("Seleccionar archivo")
		}

		selectedFileUri?.let { uri ->
			Text("Archivo seleccionado: ${uri.path}")
		}
	}
}