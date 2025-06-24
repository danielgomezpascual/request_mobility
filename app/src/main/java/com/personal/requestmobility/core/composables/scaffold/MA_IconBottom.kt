import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.ui.theme.RequestMobilityTheme




@Composable
fun MA_IconBottom(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    labelText: String,
    destacado: Boolean = false,
    seleccionado: Boolean = false,
    color : Color  = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,


) {
    Column(
        modifier = modifier
            .clickable(
                onClick ={
                    onClick()

                } ,
                role = Role.Button, // Indicar semánticamente que es un botón
                onClickLabel = labelText // Etiqueta para accesibilidad del clic
            )
            .padding(vertical = 8.dp, horizontal = 4.dp), // Padding para el área táctil y visual
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


         var colorIcono =  if (seleccionado) MaterialTheme.colorScheme.secondary else color



        Icon(
            tint =colorIcono,
            imageVector = icon,
            contentDescription = labelText, // La descripción de contenido se maneja en el Column clickeable
            // o podrías poner una descripción específica del icono si es diferente a la acción general.
            // Para este caso, como el texto ya describe la acción y el `clickable`
            // tiene `onClickLabel`, podemos dejarlo en null aquí para no ser redundante.
            // Si TalkBack leyera dos veces, entonces sí es mejor ponerlo aquí
            // y quitar onClickLabel del clickable o usar el mismo texto.
            // Por simplicidad y siguiendo un patrón común para este tipo de elementos:
            // contentDescription = contentDescription, // O una descripción más específica del icono.
            modifier = Modifier.size(if (destacado) 36.dp else 24.dp) // Tamaño estándar para iconos en Material 3
        )
        if (labelText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp)) // Pequeño espacio entre el icono y el texto
            Text(
                text = labelText,
                style = if (destacado) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall, // Estilo de Material 3 para etiquetas pequeñas
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface // Color apropiado del tema
            )
        }
    }
}

@Preview(showBackground = true, name = "IconTextButton - Inicio")
@Composable
fun PreviewIconTextButtonHome() {
    MaterialTheme { // Asegúrate de que tu Preview esté envuelto en MaterialTheme
        MA_IconBottom(
            onClick = { /* Acción para ir a Inicio */ },
            icon = Icons.Filled.Home,
            labelText = "Hola",

            )
    }
}

@Preview(showBackground = true, name = "IconTextButton - Ajustes")
@Composable
fun PreviewIconTextButtonSettings() {
    MaterialTheme {
        MA_IconBottom(
            onClick = { /* Acción para ir a Ajustes */ },
            icon = Icons.Filled.Settings,
            labelText = "Hola",

            modifier = Modifier.padding(16.dp) // Ejemplo con un padding adicional
        )
    }
}