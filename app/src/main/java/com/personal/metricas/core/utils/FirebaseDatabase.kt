package com.personal.metricas.core.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

object FirebaseDatabase {
    fun guardar(path: String, data: String, callback: ((Int, String) -> Unit)?) {
        com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference(path)
                .setValue(data)
                .addOnSuccessListener { callback?.invoke(200, "OK") }
                .addOnFailureListener { e ->
                    callback?.invoke(500, e.message ?: "Error desconocido")
                }
    }

    fun obtenerDatos(path: String, callback: ((Int, String) -> Unit)?) {
        com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference(path)
                .addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val value = snapshot.value
                                    callback?.invoke(200, value?.toString() ?: "")
                                } else {
                                    callback?.invoke(404, "No data found")
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                callback?.invoke(error.code, error.message)
                            }
                        }
                )
    }

    fun eliminar(path: String, callback: ((Int, String) -> Unit)?) {
        com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference(path)
                .removeValue()
                .addOnSuccessListener { callback?.invoke(200, "OK") }
                .addOnFailureListener { e ->
                    callback?.invoke(500, e.message ?: "Error desconocido")
                }
    }

    // URL específica del proyecto 'development-apps-3e540'
    private const val SECONDARY_DB_URL =
            "https://development-apps-3e540-default-rtdb.firebaseio.com/"

    // ⚠️ RELLENA ESTOS DATOS OBTENIDOS DE LA CONSOLA DE FIREBASE DEL PROYECTO
    // development-apps-3e540
    // Configuración del proyecto -> Tus apps (Android) -> google-services.json (o busca los
    // valores)
    private const val SECONDARY_API_KEY = "AIzaSyCsC9j5ts89zJg9UIcEZH7BKMc03JiJcZQ"
    private const val SECONDARY_APP_ID = "1:1028075123979:android:d3666b386f23c3725cde0e"
    private const val SECONDARY_PROJECT_ID = "development-apps-3e540"

    private fun getSecondaryDatabase(): com.google.firebase.database.FirebaseDatabase {
        try {
            // Intenta obtener la app secundaria si ya existe
            val secondApp = com.google.firebase.FirebaseApp.getInstance("secondary")
            return com.google.firebase.database.FirebaseDatabase.getInstance(
                    secondApp,
                    SECONDARY_DB_URL
            )
        } catch (e: Exception) {
            // Si no existe, la inicializamos
            val options =
                    com.google.firebase.FirebaseOptions.Builder()
                            .setApiKey(SECONDARY_API_KEY)
                            .setApplicationId(SECONDARY_APP_ID)
                            .setDatabaseUrl(SECONDARY_DB_URL)
                            .setProjectId(SECONDARY_PROJECT_ID)
                            .build()

            val secondApp =
                    com.google.firebase.FirebaseApp.initializeApp(
                            com.personal.metricas.App.context,
                            options,
                            "secondary"
                    )
            return com.google.firebase.database.FirebaseDatabase.getInstance(
                    secondApp,
                    SECONDARY_DB_URL
            )
        }
    }

    fun monitorizar(path: String, callback: ((Int, String) -> Unit)?) {
        // Obtenemos la base de datos secundaria correctamente inicializada
        val database =
                try {
                    getSecondaryDatabase()
                } catch (e: Exception) {
                    callback?.invoke(500, "Error inicializando Firebase secundario: ${e.message}")
                    return
                }

        val ref = database.getReference(path)
        val listener =
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists() && snapshot.value != null) {
                            // Datos encontrados/recibidos
                            callback?.invoke(200, snapshot.value.toString())

                            // Opcional: Dejar de escuchar una vez recibido el dato para ahorrar
                            // batería/datos
                            ref.removeEventListener(this)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback?.invoke(error.code, error.message)
                    }
                }
        ref.addValueEventListener(listener)
    }

    fun monitorizar(path: String, callback: ((Int, String) -> Unit)?, usarRest: Boolean = false) {
        if (!usarRest) {
            // Intentamos conectar a la instancia secundaria
            val database =
                    try {
                        com.google.firebase.database.FirebaseDatabase.getInstance(SECONDARY_DB_URL)
                    } catch (e: Exception) {
                        // Fallback a la instancia por defecto si falla (aunque la URL sea distinta)
                        com.google.firebase.database.FirebaseDatabase.getInstance()
                    }

            val ref = database.getReference(path)
            val listener =
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists() && snapshot.value != null) {
                                // Datos encontrados/recibidos
                                callback?.invoke(200, snapshot.value.toString())

                                // Opcional: Dejar de escuchar una vez recibido el dato para ahorrar
                                // batería/datos
                                ref.removeEventListener(this)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            callback?.invoke(error.code, error.message)
                        }
                    }
            ref.addValueEventListener(listener)
        } else {
            // Monitorización via REST para el proyecto secundario
            // Se lanza en un hilo separado para no bloquear la UI si fuese llamada desde ahí,
            // aunque idealmente debería ser una coroutine.
            Thread {
                        try {
                            var encontrado = false
                            while (!encontrado) {
                                try {
                                    val token =
                                            FcmSender.getAccessToken() // Obtiene token actualizado
                                    val url =
                                            "https://${FcmSender.PROJECT_ID}-default-rtdb.firebaseio.com/$path.json?auth=$token"

                                    val request = okhttp3.Request.Builder().url(url).get().build()
                                    val response = FcmSender.client.newCall(request).execute()

                                    if (response.isSuccessful) {
                                        val bodyParams = response.body?.string()
                                        // Si body es "null" (string) es que no existe.
                                        if (bodyParams != null && bodyParams != "null") {
                                            encontrado = true
                                            callback?.invoke(200, bodyParams)
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    // callback?.invoke(500, e.message ?: "Error REST")
                                    // No notificamos error en bucle para no saturar, reintentamos.
                                }
                                if (!encontrado)
                                        Thread.sleep(3000) // Espera 3 segundos antes de reintentar
                            }
                        } catch (e: InterruptedException) {
                            // Hilo interrumpido
                        }
                    }
                    .start()
        }
    }
}
