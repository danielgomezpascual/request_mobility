package com.personal.metricas.core.utils

import android.util.Base64
import com.personal.metricas.App
import com.personal.metricas.firebase.domain.FirebaseManager
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object FcmSender {

        // --- CONFIGURACIÓN: PEGA AQUÍ LOS DATOS DE TU JSON ---
        // NOTA: El private_key debe copiarse tal cual aparece en el JSON, incluyendo los \n
        private const val CLIENT_EMAIL =
                "firebase-adminsdk-ywqlm@development-apps-3e540.iam.gserviceaccount.com"
        private const val PRIVATE_KEY_PEM =
                "-----BEGIN PRIVATE KEY-----\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCWVCbvwq7CmNAm\npI3pDi5aTBBLUjAAqoDOXxFnKm8YLrdRp2VPK8qLpW1sfzVPF37W6Zx2JfRmcg8s\nkOX1sM8SiR2E9/rKFHecDbCq99nd40AJXYGlKORkM5tCDf0TYo9VhnZ5ez3unDak\n+9n0pQURg1qn7P8Xt40hOEe566ZePiB+HGtHlJOcYFHUfMaLQ/LKeswMVsTWw7M7\nniZCG63W0Y/U3H7e4U6lfVyC4hPPqXMrk5SoRocckU28IsmijXtEguMrBcUaGo6m\nmq56kXTk09kZLrW9d8FxX4YMBVeuXzjf/RCQ1Ci//PAGCMAgehQTGEHdp4/IVgez\ndOVDrlItAgMBAAECggEABsYJ7xm9mCUck/EsN8kcy5LL1SkB4rXMEgB0n0ppMRTB\ny9sYFIYrnTk6Af6FX2f2niZul2BEb+1Jw79PrU9nNTWab+hgk4Ryk0SGpl1+oqHf\nu8xL0C5UhDNTFpncwmfZf4gMhJLKCptdVvfGLWDWJqDdj9187QunP0zy5my41Vwz\nlKYDoJziJQjeQq2F+uK9xhJerT9qfJ3rw0BiqXCi5SdepEvIn9INyt87oB6Ln4qL\nkNYyLw7vzHBTWNGchAYKmyuc1aiKv9qNO4qRr+eXdyRM8HwsPMHS+SOjow9tz6Rc\nJ/F+oCxrMnS1VE0mryZF0gaqVhM6Zzp1Zetj6345vQKBgQDOr4swBDD6CB5kPL9g\nf24R4U+JBPHHFQ529gcZ5nfJSnm7CQxI+y96dECuToH4d8X2WP2wqoX84BsJK3L6\nDec52dV9Yc5NxYCb+JL9fSLQg1WiaaSFUnpd38rxOhKgCO28MJH1EI4OkzZcogc+\nfLIbJSZdarU5YE+g3XYdBEL68wKBgQC6MkwzfkuFTOjInWS+LQ/9y5hBPdQnGw0x\nxGgn2rQ/l609djZA2MymbPccW+RVa0r862Zhwi9B5yWTnYVNkzbNZ7X6yQK46/BJ\nBDf1b2hIE9Z1Ydq/5f/tJrkvPmS6U9oN9DHtq4dvfX5hXmp9tmxtw4K4887W1/Sp\njjunUKiGXwKBgQCCMF7rKuiyxeRm3UDrBrmNd6/CriOhdCj9U/GbJem0GD5fSs67\nNcVtS8bmRLtniAJhLyL1YiQt6ff9qamtyo4oiYcJiE8L++kL1eK421bPorqRKBwi\ngpZjhTDvC9ZpidY94RY/GGNy2NW4ObN4b9E8egUVeL6YWost1/GrS2DpcwKBgQCg\nFvbln0UXi5TqSEmCKgSB1jdGgP6T0PJ7Q7+55JTtyF+vZKApCT6MKUweGfQsbV/A\nLjWNAATZyP/J97nRhS2tVeplgcbRcCrc3L+wSuWCc2wE6OnH7N0q7gxyzlaUzdvB\n+5+iFcUQ2vl6hk9RXzPC/EK8+wNwPaWi0yP/K556SQKBgQCbgcRxEus8WRPt/jJr\ngqfCFyXrRXZQ2Kx5q3wU7rrtfhoNDCXwq+al+s7mR1VJvb3D9scIsbCXzESZ3pfJ\nYxRImeNQo08UaBVpQzLPIbgRompOPHMtByDOhPhrwAoIb/5S/s32LlPfP4jRh5CI\nbqoDbUDSFFgt7LosfvH9yiqQsw==\n-----END PRIVATE KEY-----\n"
        const val PROJECT_ID = "development-apps-3e540"

        // -----------------------------------------------------

        val client = OkHttpClient()

        /** Función principal para enviar el mensaje */
        suspend fun sendNotification(deviceToken: String, title: String, mob_request_id: String): String {
                return withContext(Dispatchers.IO) {
                        try {
                                // 1. Obtener el Token temporal de Google (OAuth 2.0)
                                val accessToken = getAccessToken()
                                val tokenPropioFirebase = FirebaseManager().obtenerToken()

                                App.log.d("TOken Propip $tokenPropioFirebase")
                                App.log.d("TOken Acceso de Mobility $accessToken")

                                // 2. Construir el JSON del mensaje (FCM v1)
                                val jsonBody =
                                        JSONObject().apply {
                                                put(
                                                        "message",
                                                        JSONObject().apply {
                                                                put("token", deviceToken)
                                                                /*put("notification", JSONObject().apply {
                                                                	put("title", "title")
                                                                	put("body", "MOB_REQUEST_ID|825945")
                                                                })*/
                                                                // Datos opcionales
                                                                put(
                                                                        "data",
                                                                        JSONObject().apply {
                                                                                put(
                                                                                        "mob_request_id",
                                                                                        "$mob_request_id"
                                                                                )
                                                                                put(
                                                                                        "accion",
                                                                                        "MOB_REQUEST_ID"
                                                                                )
                                                                                put(
                                                                                        "remitente",
                                                                                        tokenPropioFirebase
                                                                                )
                                                                        }
                                                                )
                                                        }
                                                )
                                        }

                                // 3. Enviar la petición a FCM v1
                                val request =
                                        Request.Builder()
                                                .url(
                                                        "https://fcm.googleapis.com/v1/projects/$PROJECT_ID/messages:send"
                                                )
                                                .addHeader("Authorization", "Bearer $accessToken")
                                                .addHeader("Content-Type", "application/json")
                                                .post(
                                                        jsonBody.toString()
                                                                .toRequestBody(
                                                                        "application/json".toMediaType()
                                                                )
                                                )
                                                .build()

                                val response = client.newCall(request).execute()
                                val responseString = response.body?.string() ?: ""

                                if (response.isSuccessful) {
                                        return@withContext "Exito: $responseString"
                                } else {
                                        return@withContext "Error HTTP ${response.code}: $responseString"
                                }
                        } catch (e: Exception) {
                                e.printStackTrace()
                                return@withContext "Excepción: ${e.message}"
                        }
                }
        }

        /** Genera el Access Token OAuth 2.0 manualmente firmando un JWT */
        fun getAccessToken(): String {
                val now = System.currentTimeMillis() / 1000
                val exp = now + 3600 // Expira en 1 hora

                // 1. Crear Header y Payload del JWT
                val header = JSONObject().put("alg", "RS256").put("typ", "JWT").toString()
                val payload =
                        JSONObject()
                                .apply {
                                        put("iss", CLIENT_EMAIL)
                                        put(
                                                "scope",
                                                "https://www.googleapis.com/auth/firebase.messaging https://www.googleapis.com/auth/firebase.database"
                                        )
                                        put("aud", "https://oauth2.googleapis.com/token")
                                        put("exp", exp)
                                        put("iat", now)
                                }
                                .toString()

                // 2. Codificar Base64URL
                val encodedHeader =
                        Base64.encodeToString(
                                header.toByteArray(),
                                Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
                        )
                val encodedPayload =
                        Base64.encodeToString(
                                payload.toByteArray(),
                                Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
                        )
                val input = "$encodedHeader.$encodedPayload"

                // 3. Limpiar y leer la clave privada RSA
                // Quitar headers, footers y saltos de línea para obtener solo la data base64 pura
                val realKey =
                        PRIVATE_KEY_PEM
                                .replace("-----BEGIN PRIVATE KEY-----", "")
                                .replace("-----END PRIVATE KEY-----", "")
                                .replace("\\n", "") // Para saltos de linea literales del JSON
                                .replace("\\s+".toRegex(), "")

                val keyBytes = Base64.decode(realKey, Base64.DEFAULT)
                val keySpec = PKCS8EncodedKeySpec(keyBytes)
                val kf = KeyFactory.getInstance("RSA")
                val privateKey = kf.generatePrivate(keySpec)

                // 4. Firmar (Sign)
                val signature = Signature.getInstance("SHA256withRSA")
                signature.initSign(privateKey)
                signature.update(input.toByteArray())
                val signatureBytes = signature.sign()
                val encodedSignature =
                        Base64.encodeToString(
                                signatureBytes,
                                Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
                        )

                val jwt = "$input.$encodedSignature"

                // 5. Intercambiar JWT por Access Token en Google
                val formBody =
                        okhttp3.FormBody.Builder()
                                .add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                                .add("assertion", jwt)
                                .build()

                val request =
                        Request.Builder()
                                .url("https://oauth2.googleapis.com/token")
                                .post(formBody)
                                .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful)
                        throw Exception("Error obteniendo token: ${response.body?.string()}")

                val jsonResponse = JSONObject(response.body!!.string())
                return jsonResponse.getString("access_token")
        }
}
