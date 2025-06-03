package com.personal.requestmobility.core.composables.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.labels.MA_Titulo2

/**
 * Un composable que muestra una sección que puede ser expandida o contraída
 * para revelar u ocultar su contenido.
 *
 * @param titulo El título que se mostrará en la cabecera de la sección.
 * @param inicialmente Indica si la sección debe estar expandida inicialmente. Por defecto es `false`.
 * @param modifier El [Modifier] que se aplicará al composable raíz de la sección.
 * @param headerIcon El icono que se mostrará en la cabecera. Por defecto es [Icons.Filled.ArrowDropDown].
 * @param content El contenido composable que se mostrará cuando la sección esté expandida.
 */
@Composable
fun MA_Expandible(
    titulo: String,
    inicialmente: Boolean = false,
    modifier: Modifier = Modifier,
    headerIcon: ImageVector = Icons.Filled.ArrowDropDown,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(inicialmente) }
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "rotationAngle")

    Column(modifier = modifier.fillMaxWidth()) {
        Surface( // Usamos Surface para darle un fondo y manejar clics fácilmente
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            shape = MaterialTheme.shapes.small // Opcional: para bordes redondeados si usas un color de fondo
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = headerIcon,
                    contentDescription = if (isExpanded) "Contraer" else "Expandir",
                    modifier = Modifier.rotate(rotationAngle)
                )
                MA_Titulo2(titulo)

            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Padding para el contenido
            ) {
                content()
            }
        }
    }
}