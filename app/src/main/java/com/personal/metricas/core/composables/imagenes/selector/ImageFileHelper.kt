package com.personal.metricas.core.composables.imagenes.selector
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageFileHelper(private val context: Context) {

    var latestCameraUri: Uri? = null
        private set
    var latestCameraFilePath: String? = null
        private set

    private fun createUniqueFileName(prefix: String, extension: String = "jpg"): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "${prefix}_${timeStamp}.$extension"
    }

    /**
     * Crea un archivo en el directorio de imágenes de la app (almacenamiento externo específico de la app)
     * y devuelve el URI de contenido para la cámara y la ruta absoluta del archivo.
     */
    fun createUriAndPathForCamera(): Pair<Uri, String>? {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir == null) {
            // No se puede acceder al directorio de almacenamiento
            return null
        }
        // Asegúrate de que el directorio exista
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val imageFileName = createUniqueFileName("CAMERA_")
        val imageFile = File(storageDir, imageFileName)

        latestCameraFilePath = imageFile.absolutePath
        latestCameraUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider", // Debe coincidir con 'authorities' en AndroidManifest.xml
            imageFile
        )
        return Pair(latestCameraUri!!, latestCameraFilePath!!)
    }

    /**
     * Copia un archivo desde un content URI (ej. de la galería) al almacenamiento
     * persistente de la app y devuelve la ruta del nuevo archivo.
     * Debe llamarse desde una corrutina con Dispatchers.IO.
     */
    fun copyFileToAppStorage(contentUri: Uri, prefix: String): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(contentUri) ?: return null
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (storageDir == null) {
                inputStream.close()
                return null
            }
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }

            // Intenta obtener la extensión original si es posible, sino usa jpg por defecto
            val fileExtension = context.contentResolver.getType(contentUri)?.substringAfterLast('/') ?: "jpg"
            val fileName = createUniqueFileName(prefix, fileExtension)
            val destinationFile = File(storageDir, fileName)

            FileOutputStream(destinationFile).use { outputStream ->
                inputStream.use { input ->
                    input.copyTo(outputStream)
                }
            }
            destinationFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: SecurityException) {
            e.printStackTrace() // Podría ocurrir si el URI no es accesible
            null
        }
    }
}