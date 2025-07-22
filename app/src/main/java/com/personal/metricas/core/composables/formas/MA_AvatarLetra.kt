package com.personal.metricas.core.composables.formas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.core.utils.if3

// Función para generar un color aleatorio para el avatar
private fun getRandomColor(): Color {
	val red = (0..255).random()
	val green = (0..255).random()
	val blue = (0..255).random()
	return Color(red, green, blue)
}

@Preview
@Composable
fun Test_MA_Avatar() {
	MA_Avatar("Pruebas")
}

@Composable
fun MA_Avatar(
	texto: String, size: Dp = 35.dp, color: Color = getRandomColor(),
	fontSize: TextUnit = 18.sp,
			 ) {
	// Avatar
	var letras = if3( (texto.isNotEmpty() && texto.length == 3), 3 , 2)
	val fs : TextUnit = if3( (letras == 3 && fontSize >= 18.sp ), 14.sp, fontSize)
	
	Column(
			modifier = Modifier.Companion
				.size(size)
				.clip(CircleShape)
				.background(color),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		  
		  
		  ) {
		Text(modifier = Modifier.fillMaxWidth(),
			 textAlign = TextAlign.Center,
			 text = texto.take(letras).uppercase(),
			 fontSize = fs,
			 color = Color.Companion.White)
		
	}
	
}