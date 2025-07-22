package com.personal.metricas.core.composables.listas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.R
import com.personal.metricas.core.composables.imagenes.MA_ImagenDrawable
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.utils._t

@Composable
fun MA_NoData(){
	Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
		MA_ImagenDrawable(R.drawable.no_data, s = 256.dp)
		MA_LabelNormal(_t(R.string.sin_informacion))
	}
}