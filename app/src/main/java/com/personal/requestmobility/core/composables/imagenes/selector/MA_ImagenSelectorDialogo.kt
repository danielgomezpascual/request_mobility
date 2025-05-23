package com.personal.requestmobility.core.composables.imagenes.selector

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.personal.requestmobility.core.composables.modales.MA_BottomSheet
import kotlinx.coroutines.launch

//todo: en un futuro cambiar esto por un cuadro inferior que se desplaza hacia arriba, un ModalSheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_ImagenSelectorDialogo(
    onDismissRequest: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPickFromGalleryClick: () -> Unit
) {








}