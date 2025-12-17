package com.personal.metricas.core.composables.card

import androidx.compose.foundation.layout.Box
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
fun MA_Card_Elevada(
	modifier: Modifier = Modifier,
	elevacion: Dp = 3.dp,
	redondeo: Dp = 10.dp,
	paddingCard : Dp = 6.dp,
	color:Color = Color.White,
	contenido: @Composable () -> Unit,
) {


	MA_Card (modifier, elevacion, redondeo, paddingCard, color, contenido)

}

@Composable
fun MA_Card(
	modifier: Modifier = Modifier,
	elevacion: Dp = 0.dp,
	redondeo: Dp = 10.dp,
	paddingCard : Dp = 6.dp,
	color:Color = Color.White,
	contenido: @Composable () -> Unit,
) {


	ElevatedCard(

		elevation = CardDefaults.cardElevation(defaultElevation = elevacion)
		, shape = RoundedCornerShape(redondeo), modifier = modifier
			.fillMaxWidth()
			.padding(paddingCard),
			colors = CardDefaults.cardColors().copy(containerColor = color)) {
		Box(Modifier.padding(5.dp)) {
			contenido()
		}
	}

	Spacer(modifier = Modifier.padding(vertical = 2.dp))


}