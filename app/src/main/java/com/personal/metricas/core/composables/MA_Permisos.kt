package com.personal.metricas.core.composables

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun MA_Permisos(onPermissionGranted: () -> Unit) {
	val context = LocalContext.current

	// Launcher para solicitar el permiso
	val requestPermissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission()
	) { isGranted: Boolean ->
		if (isGranted) {
			// Permiso concedido, podemos continuar
			onPermissionGranted()
		} else {
			// El usuario denegó el permiso. Aquí podrías mostrar un
			// mensaje explicando por qué necesitas la notificación.
		}
	}

	// Comprobamos y solicitamos el permiso al entrar en la pantalla
	LaunchedEffect(Unit) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			when {
				ContextCompat.checkSelfPermission(
					context,
					Manifest.permission.POST_NOTIFICATIONS
				) == PackageManager.PERMISSION_GRANTED -> {
					// El permiso ya está concedido
					onPermissionGranted()
				}
				else                                   -> {
					// Solicitar el permiso
					requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
				}
			}
		} else {
			// En versiones anteriores a Android 13, el permiso se concede
			// automáticamente al instalar la app.
			onPermissionGranted()
		}
	}
}