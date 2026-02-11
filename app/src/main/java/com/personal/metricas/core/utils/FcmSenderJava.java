package com.personal.metricas.core.utils;

import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FcmSenderJava {

    private static final String TAG = "FcmSenderJava";

    // --- CONFIGURACIÓN: PEGA AQUÍ LOS DATOS DE TU JSON ---
    // NOTA: El private_key debe copiarse tal cual aparece en el JSON, incluyendo los \n
    private static final String CLIENT_EMAIL = "firebase-adminsdk-ywqlm@development-apps-3e540.iam.gserviceaccount.com";
    private static final String PRIVATE_KEY_PEM = "-----BEGIN PRIVATE KEY-----\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCWVCbvwq7CmNAm\npI3pDi5aTBBLUjAAqoDOXxFnKm8YLrdRp2VPK8qLpW1sfzVPF37W6Zx2JfRmcg8s\nkOX1sM8SiR2E9/rKFHecDbCq99nd40AJXYGlKORkM5tCDf0TYo9VhnZ5ez3unDak\n+9n0pQURg1qn7P8Xt40hOEe566ZePiB+HGtHlJOcYFHUfMaLQ/LKeswMVsTWw7M7\nniZCG63W0Y/U3H7e4U6lfVyC4hPPqXMrk5SoRocckU28IsmijXtEguMrBcUaGo6m\nmq56kXTk09kZLrW9d8FxX4YMBVeuXzjf/RCQ1Ci//PAGCMAgehQTGEHdp4/IVgez\ndOVDrlItAgMBAAECggEABsYJ7xm9mCUck/EsN8kcy5LL1SkB4rXMEgB0n0ppMRTB\ny9sYFIYrnTk6Af6FX2f2niZul2BEb+1Jw79PrU9nNTWab+hgk4Ryk0SGpl1+oqHf\nu8xL0C5UhDNTFpncwmfZf4gMhJLKCptdVvfGLWDWJqDdj9187QunP0zy5my41Vwz\nlKYDoJziJQjeQq2F+uK9xhJerT9qfJ3rw0BiqXCi5SdepEvIn9INyt87oB6Ln4qL\nkNYyLw7vzHBTWNGchAYKmyuc1aiKv9qNO4qRr+eXdyRM8HwsPMHS+SOjow9tz6Rc\nJ/F+oCxrMnS1VE0mryZF0gaqVhM6Zzp1Zetj6345vQKBgQDOr4swBDD6CB5kPL9g\nf24R4U+JBPHHFQ529gcZ5nfJSnm7CQxI+y96dECuToH4d8X2WP2wqoX84BsJK3L6\nDec52dV9Yc5NxYCb+JL9fSLQg1WiaaSFUnpd38rxOhKgCO28MJH1EI4OkzZcogc+\nfLIbJSZdarU5YE+g3XYdBEL68wKBgQC6MkwzfkuFTOjInWS+LQ/9y5hBPdQnGw0x\nxGgn2rQ/l609djZA2MymbPccW+RVa0r862Zhwi9B5yWTnYVNkzbNZ7X6yQK46/BJ\nBDf1b2hIE9Z1Ydq/5f/tJrkvPmS6U9oN9DHtq4dvfX5hXmp9tmxtw4K4887W1/Sp\njjunUKiGXwKBgQCCMF7rKuiyxeRm3UDrBrmNd6/CriOhdCj9U/GbJem0GD5fSs67\nNcVtS8bmRLtniAJhLyL1YiQt6ff9qamtyo4oiYcJiE8L++kL1eK421bPorqRKBwi\ngpZjhTDvC9ZpidY94RY/GGNy2NW4ObN4b9E8egUVeL6YWost1/GrS2DpcwKBgQCg\nFvbln0UXi5TqSEmCKgSB1jdGgP6T0PJ7Q7+55JTtyF+vZKApCT6MKUweGfQsbV/A\nLjWNAATZyP/J97nRhS2tVeplgcbRcCrc3L+wSuWCc2wE6OnH7N0q7gxyzlaUzdvB\n+5+iFcUQ2vl6hk9RXzPC/EK8+wNwPaWi0yP/K556SQKBgQCbgcRxEus8WRPt/jJr\ngqfCFyXrRXZQ2Kx5q3wU7rrtfhoNDCXwq+al+s7mR1VJvb3D9scIsbCXzESZ3pfJ\nYxRImeNQo08UaBVpQzLPIbgRompOPHMtByDOhPhrwAoIb/5S/s32LlPfP4jRh5CI\nbqoDbUDSFFgt7LosfvH9yiqQsw==\n-----END PRIVATE KEY-----\n";
    private static final String PROJECT_ID = "development-apps-3e540";

    // -----------------------------------------------------

    private final OkHttpClient client;

    // Singleton (optional, but convenient mainly if OkHttpClient is reused)
    private static final FcmSenderJava INSTANCE = new FcmSenderJava();

    public static FcmSenderJava getInstance() {
        return INSTANCE;
    }

    private FcmSenderJava() {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Función principal para enviar el mensaje.
     * <p>
     * NOTA: Este método realiza operaciones de red síncronas.
     * DEBE ser llamado desde un hilo secundario (background thread), no desde el Main Thread.
     */
    public String sendNotification(String deviceToken, String title, String bodyText) {
        try {
            // 1. Obtener el Token temporal de Google (OAuth 2.0)
            String accessToken = getAccessToken();
            
            // Obtener el token del remitente (dispositivo actual)
            // Nota: Tasks.await bloquea el hilo hasta que se completa la tarea.
            String tokenPropioFirebase = Tasks.await(FirebaseMessaging.getInstance().getToken());

            Log.d(TAG, "Token remitente: " + tokenPropioFirebase);

            // 2. Construir el JSON del mensaje (FCM v1)
            JSONObject dataJson = new JSONObject();
            dataJson.put("mob_request_id", "825945");
            dataJson.put("accion", "MOB_REQUEST_ID");
            dataJson.put("remitente", tokenPropioFirebase != null ? tokenPropioFirebase : "?");

            JSONObject msgJson = new JSONObject();
            msgJson.put("token", deviceToken);
            msgJson.put("data", dataJson);
            // msgJson.put("notification", ...); // Si quisieras enviar notificación visible

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("message", msgJson);

            // 3. Enviar la petición a FCM v1
            MediaType jsonMediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(jsonBody.toString(), jsonMediaType);

            Request request = new Request.Builder()
                    .url("https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseString = response.body() != null ? response.body().string() : "";
                if (response.isSuccessful()) {
                    return "Exito: " + responseString;
                } else {
                    return "Error HTTP " + response.code() + ": " + responseString;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Excepción: " + e.getMessage();
        }
    }

    /**
     * Genera el Access Token OAuth 2.0 manualmente firmando un JWT
     */
    private String getAccessToken() throws Exception {
        long now = System.currentTimeMillis() / 1000;
        long exp = now + 3600; // Expira en 1 hora

        // 1. Crear Header y Payload del JWT
        JSONObject header = new JSONObject();
        header.put("alg", "RS256");
        header.put("typ", "JWT");

        JSONObject payload = new JSONObject();
        payload.put("iss", CLIENT_EMAIL);
        payload.put("scope", "https://www.googleapis.com/auth/firebase.messaging");
        payload.put("aud", "https://oauth2.googleapis.com/token");
        payload.put("exp", exp);
        payload.put("iat", now);

        // 2. Codificar Base64URL
        String encodedHeader = Base64.encodeToString(header.toString().getBytes("UTF-8"), Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
        String encodedPayload = Base64.encodeToString(payload.toString().getBytes("UTF-8"), Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
        String input = encodedHeader + "." + encodedPayload;

        // 3. Limpiar y leer la clave privada RSA

        String realKey = PRIVATE_KEY_PEM
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\n", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.decode(realKey, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        java.security.PrivateKey privateKey = kf.generatePrivate(keySpec);

        // 4. Firmar (Sign)
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(input.getBytes("UTF-8"));
        byte[] signatureBytes = signature.sign();
        String encodedSignature = Base64.encodeToString(signatureBytes, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);

        String jwt = input + "." + encodedSignature;

        // 5. Intercambiar JWT por Access Token en Google
        okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                .add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                .add("assertion", jwt)
                .build();

        Request request = new Request.Builder()
                .url("https://oauth2.googleapis.com/token")
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Error obteniendo token: " + (response.body() != null ? response.body().string() : "null"));
            }
            if (response.body() != null) {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                return jsonResponse.getString("access_token");
            } else {
                return "";
            }
        }
    }
}
