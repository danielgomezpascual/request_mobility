package com.personal.metricas.core.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Clase utilitaria para enviar notificaciones Push a través de Firebase Cloud Messaging (FCM Legacy
 * API). Contiene toda la lógica (DTOs, Interfaz Retrofit y Cliente) en un mismo lugar.
 */
class FirebasePushSender {

    companion object {
        private const val TAG = "FirebasePushSender"
        // Clave del servidor obtenida de Firebase Console (Legacy API)
        private const val SERVER_KEY =
                "AAAA714NdQs:APA91bFFX_rU_rAOiEpLVQiI1xi7KZ0bvNyj4X1NgB4vzGRBpLRK3VBcL27m52BbHaZXkoitKxDEbrv1-heuy6LLumX-xG30tPR2e4F9vTan5x8LNKs7fkcJFyQO2yqwQvreqaI8U6fR"

        // Usamos la URL base general, pero el endpoint completo se define en la interfaz para
        // evitar errores de ruta.
        private const val BASE_URL = "https://fcm.googleapis.com/"
    }

    // --- Modelos de Datos (DTOs) ---

    data class PushNotificationDto(
            val to: String,
            val notification: NotificationDataDto? = null,
            val data: Map<String, String>? = null
    )

    data class NotificationDataDto(val title: String, val body: String)

    // --- Interfaz Retrofit ---

    interface FcmApi {
        // Se define la URL completa aquí para asegurar que no haya problemas de concatenación con
        // la Base URL.
        @POST("https://fcm.googleapis.com/fcm/send")
        suspend fun sendNotification(@Body notification: PushNotificationDto): Response<Unit>
    }

    // --- Configuración e Instancia del Cliente ---

    private val fcmApi: FcmApi by lazy {
        // Interceptor para añadir la cabecera de Autorización automáticamente
        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val requestBuilder =
                    original.newBuilder()
                            // IMPORTANTE: Para la API Legacy de FCM con Server Key (AAAA...), el
                            // prefijo debe ser "key=".
                            // Si esto falla con 401, verificar si la clave es correcta.
                            .header("Authorization", "key=$SERVER_KEY")
                            .header("Content-Type", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder().addInterceptor(authInterceptor).build()

        Retrofit.Builder()
                // Aunque ponemos BaseUrl, el @POST con URL completa la sobrescribe, lo cual es más
                // seguro.
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FcmApi::class.java)
    }

    /**
     * Envía una notificación Push a un dispositivo específico.
     *
     * @param targetToken Token FCM del dispositivo destino.
     * @param title Título de la notificación.
     * @param message Cuerpo del mensaje.
     * @param dataPayload Datos adicionales (opcional) key-value para procesar en background.
     */
    fun sendPush(
            targetToken: String,
            title: String,
            message: String,
            dataPayload: Map<String, String> = emptyMap()
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val notification =
                    PushNotificationDto(
                            to = targetToken,
                            notification = NotificationDataDto(title = title, body = message),
                            data = if (dataPayload.isNotEmpty()) dataPayload else null
                    )

            try {
                Log.d(TAG, "Enviando Push a $targetToken...")
                val response = fcmApi.sendNotification(notification)

                if (response.isSuccessful) {
                    Log.d(TAG, "Push enviado correctamente (Code: ${response.code()}).")
                } else {
                    // Leemos el cuerpo del error para depurar el 404 u otros errores
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "Error enviando Push: HTTP ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción enviando Push: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
