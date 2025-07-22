package com.personal.metricas.core.composables.listas

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MA_Divider(){

	HorizontalDivider(modifier = Modifier.padding(horizontal = 15.dp), thickness = 1.dp,
					  color = Color(222, 221, 221, 100))


}