package com.personal.metricas.core.notificaciones

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.personal.metricas.App
import com.personal.metricas.App.Companion.context
import com.personal.metricas.MainActivity
import com.personal.metricas.core.notificaciones.NotificacionesConst
import com.personal.metricas.R
import com.personal.metricas.core.composables.tabla.Fila
import com.personal.metricas.core.utils.getAppName
import com.personal.metricas.core.utils.reemplazaValorFila
import com.personal.metricas.paneles.domain.entidades.Alarmas
import com.personal.metricas.paneles.domain.entidades.EsquemaColores

class NotificacionesManager {
	fun createNotificationChannel() {
		// Los canales solo son necesarios para API 26+
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				NotificacionesConst.CHANNEL_ID,
				NotificacionesConst.CHANNEL_NAME,
				NotificationManager.IMPORTANCE_DEFAULT
			).apply {
				description = "Canal para las notificaciones principales de la app."
				// Puedes configurar más cosas aquí, como luces, vibración, etc.
			}

			// Registrar el canal en el sistema
			val notificationManager: NotificationManager = context.getSystemService(NotificationManager::class.java)

			notificationManager.createNotificationChannel(channel)
		}

	}

	fun showNotificacion(
		context: Context,
		alarma: Alarmas,
		/*titulo : String = "Metricas" ,
		texto: String, color: Color = Color(96, 217, 69, 255)*/
	) {
		// 1. Crear un Intent para abrir una Activity cuando se toque la notificación
		val intent = Intent(context, MainActivity::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		}
		val color = EsquemaColores().dameEsquemaCondiciones().colores.get(alarma.color)
		val titulo = alarma.titulo //todo: NO se muestra el titulo, ponerlo en el info de la celda
		val texto = alarma.texto

		val pendingIntent: PendingIntent = PendingIntent.getActivity(
			context,
			0,
			intent,
			PendingIntent.FLAG_IMMUTABLE
		)

		// 2. Construir la notificación
		val builder = NotificationCompat.Builder(context, NotificacionesConst.CHANNEL_ID)
			.setSmallIcon(R.drawable.logo) // ¡IMPORTANTE! Un icono pequeño es obligatorio.
			.setContentTitle(titulo)
			.setColor(color.toArgb())
			.setContentText(texto)
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setContentIntent(pendingIntent) // Asocia el toque a la apertura de la app
			.setAutoCancel(true) // La notificación se cierra al tocarla

		// 3. Mostrar la notificación
		// El ID de la notificación (101 en este caso) debe ser único.
		// Si envías otra notificación con el mismo ID, la anterior se actualizará.
		with(NotificationManagerCompat.from(context)) {
			// Se necesita verificación de permiso aquí también por seguridad.
			// Aunque ya lo pedimos, el linter de Android lo exige.
			// El `NotificationPermissionRequester` que creamos antes se encarga de la lógica.
			// Aquí solo comprobamos de nuevo antes de publicar.
			if (ActivityCompat.checkSelfPermission(
					context,
					Manifest.permission.POST_NOTIFICATIONS
				) == PackageManager.PERMISSION_GRANTED
			) {
				val notificationId = System.currentTimeMillis().toInt()
				notify(notificationId, builder.build())
			}
		}
	}

	fun dameTexto(textoPlantilla: String, fila: Fila): String {
		return textoPlantilla.reemplazaValorFila(fila.toParametros())

	}
}