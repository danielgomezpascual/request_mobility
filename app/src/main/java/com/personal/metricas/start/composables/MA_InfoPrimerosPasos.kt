package com.personal.metricas.start.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.R
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.imagenes.MA_ImagenDrawable
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.labels.MA_LabelNormal

@Composable
fun MA_PrimerosPasos(titulo: String = "Primeros Pasos", indice : String = "1", descripcion: String = ""){
	Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
		MA_Spacer()
		MA_ImagenDrawable(imagen = R.drawable.logo, s = 20.dp)
		MA_Spacer()
		MA_LabelNegrita(titulo)
		MA_Spacer()
		MA_Avatar(texto = indice, size = 70.dp, color = Color.Gray, fontSize = 40.sp, )
		MA_Spacer()
		MA_LabelNormal(descripcion, alineacion = TextAlign.Center)
		MA_Spacer()
	}


}