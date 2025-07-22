package com.personal.metricas.core.composables.labels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.personal.metricas.core.composables.MA_Spacer


@Composable
fun MA_LabelTextoDestacado(modifier: Modifier = Modifier, valor: String, valorDestacado: String){
	Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
		MA_LabelNormal(valor = valor)
		MA_Spacer()
		MA_LabelNegrita(valor = valorDestacado)
	}
}