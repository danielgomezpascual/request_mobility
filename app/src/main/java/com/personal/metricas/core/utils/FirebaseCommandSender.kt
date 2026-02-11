package com.personal.metricas.core.utils

import android.util.Base64
import android.util.Log
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Clase para solicitar datos a otro dispositivo mediante Firebase Cloud Messaging.
 *
 * Se ha adaptado para usar la CLAVE DE SERVIDOR (Server Key) proporcionada explícitamente. Nota: El
 * endpoint es la API Legacy de FCM, la cual requiere el encabezado "Authorization: key=YOUR_KEY".
 */
object FirebaseCommandSender {

    private const val TAG = "FirebaseCommandSender"
    private const val FCM_API_URL = "https://fcm.googleapis.com/fcm/send"

    // CLAVE DE SERVIDOR HARDCODEADA (Proporcionada por el usuario)
    // NOTA: En producción, idealmente esto no debería estar en el código fuente de la app.
    private const val SERVER_KEY =
        "AAAA714NdQs:APA91bFFX_rU_rAOiEpLVQiI1xi7KZ0bvNyj4X1NgB4vzGRBpLRK3VBcL27m52BbHaZXkoitKxDEbrv1-heuy6LLumX-xG30tPR2e4F9vTan5x8LNKs7fkcJFyQO2yqwQvreqaI8U6fR"

    /**
     * Construye y envía la petición para obtener una transacción por su ID.
     *
     * @param targetDeviceToken El token FCM del dispositivo donde está la base de datos (Destino).
     * @param transactionId El ID (numero) de la transacción que queremos consultar.
     * @param responsePackage El nombre del paquete de ESTA aplicación (para que la respuesta vuelva
     * aquí).
     * @param responseAction El action del BroadcastReceiver de ESTA aplicación.
     */
    suspend fun requestTransactionById(
        targetDeviceToken: String,
        transactionId: String,
        responsePackage: String,
        responseAction: String = "$responsePackage.SQL_RESULT",
        callback: (Boolean, String?) -> Unit
    ) {
        // 1. Construir la consulta SQL basada en el ID
        val sqlQuery = "SELECT * FROM T_TRANSACTION WHERE numero = '$transactionId'"

        Log.d(TAG, "Generando petición para Transacción ID: $transactionId")

        withContext(Dispatchers.IO) {
            try {
                // 2. Preparar el JSON interno de la acción (Lo que leerá la App destino)
                val sqlBase64 =
                    Base64.encodeToString(sqlQuery.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)

                val innerPayload =
                    JSONObject().apply {
                        put("accion", "EXECUTE_SQL")
                        put("sql", sqlBase64)
                        put("broadcast_action", responseAction)
                        put("target_package", responsePackage)
                    }

                // 3. Preparar el cuerpo de la petición FCM
                // ADAPTADO: Estructura compatible con el ejemplo SQL funcional
                val fcmJson =
                    JSONObject().apply {
                        put("to", targetDeviceToken)
                        put("priority", "high")

                        // Bloque "notification" (Para que FirebaseService.java lo capture en
                        // remoteMessage.getNotification().getBody())
                        val notif =
                            JSONObject().apply {
                                put(
                                    "title",
                                    "X Factory Mobile"
                                ) // Título usado en el ejemplo SQL
                                put(
                                    "body",
                                    innerPayload.toString()
                                ) // El body es el JSON stringificado con nuestra lógica
                                put("sound", "default")
                            }
                        put("notification", notif)

                        // Bloque "data"
                        val data =
                            JSONObject().apply {
                                put("type", "SQL_QUERY")
                                put("query_id", transactionId)
                                put("origin_package", responsePackage)
                                // Añadimos campos dummy del ejemplo SQL por si acaso,
                                // aunque no afecten la lógica
                                put("action", "SYNC")
                                put("title", "SQL Request")
                            }
                        put("data", data)
                    }

                // 4. Enviar vía HTTP (Nativo Android)
                val url = URL(FCM_API_URL)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.readTimeout = 10000
                conn.connectTimeout = 15000
                conn.doOutput = true
                conn.doInput = true

                // HEADER AUTHORIZATION
                conn.setRequestProperty("Content-Type", "application/json")
                // IMPORTANTE: Para la API Legacy, el estándar es "key=".
                // Aunque en SQL Server usaras "Bearer", es probable que el cliente SQL lo
                // transformara
                // o que FCM acepte ambos en ciertos contextos. Usamos "key=" por ser la
                // especificación oficial para tokens AAAA...
                conn.setRequestProperty("Authorization", "Bearer $SERVER_KEY")

                // Escribir cuerpo
                val os = OutputStreamWriter(conn.outputStream)
                os.write(fcmJson.toString())
                os.flush()
                os.close()

                // Leer respuesta
                val responseCode = conn.responseCode
                if (responseCode == 200) {
                    Log.d(TAG, "Petición enviada (200 OK).")
                    withContext(Dispatchers.Main) {
                        callback(true, "Solicitud enviada correctamente.")
                    }
                } else {
                    // Intentamos leer el error body
                    var errorBody = ""
                    try {
                        errorBody = conn.errorStream.bufferedReader().readText()
                    } catch (e: Exception) {}

                    val errorMsg = "Error HTTP $responseCode. $errorBody"
                    Log.e(TAG, errorMsg)
                    withContext(Dispatchers.Main) { callback(false, errorMsg) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "Excepción enviando comando: ${e.message}")
                withContext(Dispatchers.Main) { callback(false, "Excepción: ${e.message}") }
            }
        }
    }
}
