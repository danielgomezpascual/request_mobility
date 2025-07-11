package com.personal.requestmobility.core.composables.labels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenDrawable

@Preview
@Composable
fun Test_Titulo2() {
	MA_Titulo2("Prujebas")
}

@Composable
fun MA_Titulo2(
	valor: String,
	modifier: Modifier = Modifier,
	color: Color = Color.Black,
	fondo: Color = Color.White,
	alineacion: TextAlign = TextAlign.Start,
	icono: Icons? = null,
) {


	Row(modifier = modifier.fillMaxWidth().padding(top = 20.dp, bottom = 8.dp,
												   start = 5.dp, end = 5.dp
	), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
		if (valor.isNotEmpty()) {
			MA_ImagenDrawable(imagen = R.drawable.tit)
		}

		Text(text = valor,
			 fontSize = 16.sp,
			 modifier = modifier
			.fillMaxWidth(),
			 color = color, style = MaterialTheme.typography.titleMedium, textAlign = alineacion)
		// HorizontalDivider()
	}


}
