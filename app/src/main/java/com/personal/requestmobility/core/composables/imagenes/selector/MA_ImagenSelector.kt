package com.personal.requestmobility.core.composables.imagenes.selector

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape // O la forma que prefieras
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.personal.requestmobility.core.composables.imagenes.selector.MA_ImagenSelectorDialogo
import com.personal.requestmobility.core.composables.modales.MA_BottomSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_ImagenSelector(
    modifier: Modifier = Modifier,
    defaultImageResId: Int, // Ejemplo: R.drawable.default_avatar
    imageSize: Dp = 120.dp, // Tamaño del control de imagen
    onImageStored: (filePath: String?) -> Unit // Callback con la ruta del archivo guardado
) {
    var imageUriToDisplay by remember { mutableStateOf<Uri?>(null) }
    var showSelectionDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val imageFileHelper = remember(context) { ImageFileHelper(context) }


// Painter actual para la imagen
    val painter = if (imageUriToDisplay != null) {
        rememberAsyncImagePainter(model = imageUriToDisplay)
    } else {
        // USA COIL PARA CARGAR LA IMAGEN POR DEFECTO DESDE EL RECURSO ID
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(defaultImageResId) // Coil puede tomar el ID del recurso directamente
                .error(defaultImageResId) // Opcional: si Coil falla, intenta mostrarlo directamente
                .placeholder(defaultImageResId) // Opcional: placeholder mientras carga
                .build()
        )
    }

    // Launcher para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUriToDisplay = imageFileHelper.latestCameraUri
                onImageStored(imageFileHelper.latestCameraFilePath)
            } else {
                Toast.makeText(context, "Captura de foto cancelada.", Toast.LENGTH_SHORT).show()
                onImageStored(null)
            }
        }
    )

    // Launcher para seleccionar imagen de galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { galleryUri ->
            if (galleryUri != null) {
                coroutineScope.launch {
                    val filePath = withContext(Dispatchers.IO) {
                        imageFileHelper.copyFileToAppStorage(galleryUri, "GALLERY_IMG")
                    }
                    if (filePath != null) {
                        imageUriToDisplay = Uri.fromFile(File(filePath))
                        onImageStored(filePath)
                    } else {
                        Toast.makeText(context, "Error al guardar la imagen.", Toast.LENGTH_SHORT).show()
                        onImageStored(null)
                    }
                }
            } else {
                Toast.makeText(context, "Selección de galería cancelada.", Toast.LENGTH_SHORT).show()
                onImageStored(null)
            }
        }
    )

    // Launcher para solicitar permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val uriAndPath = imageFileHelper.createUriAndPathForCamera()
                if (uriAndPath != null) {
                    takePictureLauncher.launch(uriAndPath.first) // uriAndPath.first es el content URI
                } else {
                    Toast.makeText(context, "Error al preparar la cámara.", Toast.LENGTH_SHORT).show()
                    onImageStored(null)
                }
            } else {
                Toast.makeText(context, "Permiso de cámara denegado.", Toast.LENGTH_LONG).show()
                onImageStored(null)
            }
        }
    )



    val sheetState = rememberModalBottomSheetState()


    val scope = rememberCoroutineScope() // Se mantiene dentro del componente
    //var textoSeleccionado by remember { mutableStateOf(valorInicial) }


    Box(
        modifier = modifier
            .size(imageSize)
            .clip(CircleShape) // Puedes cambiar CircleShape a RoundedCornerShape(16.dp) o Modifier.None
            .background(MaterialTheme.colorScheme.surfaceVariant) // Un fondo sutil
            .clickable { scope.launch { sheetState.show() } },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = "Imagen seleccionable por el usuario",
            contentScale = ContentScale.Crop, // Crop para llenar el círculo/cuadrado
            modifier = Modifier.fillMaxSize()
        )
    }





    MA_BottomSheet(
        sheetState = sheetState,
        onClose = {
            {  scope.launch { sheetState.hide() }}
        },
        contenido = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {


                    Button(
                        onClick = {
                            scope.launch { sheetState.hide() }
                            showSelectionDialog = false
                            when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                                PackageManager.PERMISSION_GRANTED -> {
                                    val uriAndPath = imageFileHelper.createUriAndPathForCamera()
                                    if (uriAndPath != null) {
                                        takePictureLauncher.launch(uriAndPath.first)
                                    } else {
                                        Toast.makeText(context, "Error al preparar la cámara.", Toast.LENGTH_SHORT).show()
                                        onImageStored(null)
                                    }
                                }

                                else -> {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        },
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text("📸 Tomar Foto")
                    }
                    Button(
                        onClick = {
                            showSelectionDialog = false
                            scope.launch { sheetState.hide() }
                            pickImageLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text("🖼️ Seleccionar de Galería")
                    }

                    TextButton(
                        onClick = {  scope.launch { sheetState.hide() }} ,
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        })


   /* MA_ImagenSelectorDialogo(
        onDismissRequest = { showSelectionDialog = false },
        onTakePhotoClick = {
            showSelectionDialog = false
            when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                PackageManager.PERMISSION_GRANTED -> {
                    val uriAndPath = imageFileHelper.createUriAndPathForCamera()
                    if (uriAndPath != null) {
                        takePictureLauncher.launch(uriAndPath.first)
                    } else {
                        Toast.makeText(context, "Error al preparar la cámara.", Toast.LENGTH_SHORT).show()
                        onImageStored(null)
                    }
                }

                else -> {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        },
        onPickFromGalleryClick = {
            showSelectionDialog = false
            pickImageLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    )*/

}