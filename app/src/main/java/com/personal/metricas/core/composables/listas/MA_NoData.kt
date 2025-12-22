package com.personal.metricas.core.composables.listas

import MA_Morph
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.R
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.imagenes.MA_ImagenDrawable
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.utils._t

@Composable
fun MA_NoData(mensaje: String = _t(R.string.sin_informacion)) {
    Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 48.dp, horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(140.dp)) {
            // Animación de fondo morphing para un estado vacío moderno y dinámico
            MA_Morph(
                    size = 130.dp,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            )

            MA_ImagenDrawable(imagen = R.drawable.no_data, s = 60.dp)
        }

        MA_Spacer(modifier = Modifier.padding(16.dp))

        MA_LabelNormal(
                valor = mensaje,
                size = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                alineacion = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
        )

        MA_Spacer(modifier = Modifier.padding(4.dp))

        MA_LabelNormal(
                valor = "No se han encontrado registros en este momento.",
                size = 14.sp,
                alineacion = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}
