package com.personal.metricas.core.composables.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Test_MA_Card() {

	MA_Card {
		Column {
			Text("pruebas")
			Text("pruebas1")
			Text("pruebas2")
			Text("pruebas3")

		}


	}
}

@Composable
fun MA_Card(
	modifier: Modifier = Modifier,
	elevacion: Dp = 2.dp,
	contenido: @Composable () -> Unit,
) {


	ElevatedCard(

		elevation = CardDefaults.cardElevation(defaultElevation = elevacion), shape = RoundedCornerShape(2.dp), modifier = modifier
			.fillMaxWidth()
			.padding(5.dp), colors = CardDefaults.cardColors().copy(containerColor = Color.White)) {
		//	Box(Modifier.padding(2.dp)) {
		contenido()
		//	}
	}

	Spacer(modifier = Modifier.padding(vertical = 2.dp))


}