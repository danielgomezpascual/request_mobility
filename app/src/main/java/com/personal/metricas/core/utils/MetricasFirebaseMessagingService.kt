
package com.personal.metricas.core.utils

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.personal.metricas.App

class MetricasFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 1. Notificación de mensaje recibido
        App.log.d("Mensaje recibido de: ${remoteMessage.from}")

        // 2. Procesar carga útil de datos (Data Payload)
        // Los mensajes de datos se manejan aquí tanto en primer como en segundo plano.
        if (remoteMessage.data.isNotEmpty()) {
            App.log.d("Payload de datos: ${remoteMessage.data}")
            
            // Ejemplo de procesamiento:
            // val accion = remoteMessage.data["accion"]
            // procesarAccion(accion)
        }

        // 3. Procesar carga útil de notificación (Notification Payload)
        // Si la app está en primer plano, se recibe aquí.
        // Si está en segundo plano, el sistema muestra la notificación automáticamente y este código NO se ejecuta
        // a menos que sea una notificación de "solo datos" o mixta con manejo específico.
        remoteMessage.notification?.let {
            App.log.d("Cuerpo de la notificación: ${it.body}")
            // Aquí puedes mostrar una notificación personalizada si lo deseas
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        App.log.d("Nuevo token generado: $token")
        
        // Aquí deberías enviar el token a tu servidor backend si necesitas
        // dirigirte a este dispositivo específicamente más tarde.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        // Implementar lógica de envío al servidor
        // Ej: Repository.enviarToken(token)
    }
}
