package com.personal.requestmobility.core.composables.scaffold

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.componentes.Cabecera
import com.personal.requestmobility.core.composables.componentes.TituloScreen
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenDrawable
import com.personal.requestmobility.core.composables.labels.MA_LabelMini
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.labels.MA_Titulo
import com.personal.requestmobility.core.composables.labels.MA_Titulo2
import com.personal.requestmobility.core.navegacion.EventosNavegacion


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_ScaffoldGenerico(
	titulo: String,
	tituloScreen: TituloScreen,
	navegacion: (EventosNavegacion) -> Unit = {},
	volver: Boolean = false,
	contenidoBottomBar: @Composable () -> Unit = {},
	contenido: @Composable () -> Unit,

	) {


	Scaffold(topBar = {

		Box(modifier = Modifier.padding(vertical = 15.dp)){
			Cabecera(tituloScreen)
		}

/*
		TopAppBar(expandedHeight = 95.dp,


			//windowInsets = WindowInsets(0.dp) ,// Prueba esto si hay un padding no deseado

				  title = {


					  //MA_Card {

					   MA_Card(modifier = Modifier.padding(3.dp)) {

						   Row(modifier = Modifier
							   .padding(5.dp)
							   .fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {


							   Image(modifier = Modifier.height(120.dp),                            //modifier = Modifier.width(s),
									 painter = painterResource(id = R.drawable.panel_top), contentDescription = "Descripción de tu imagen", contentScale = ContentScale.Crop

							   )
							   Column {
								   MA_LabelNormal("Home! ")
								   MA_LabelMini("Elementos inicales ")
							   }

						   }

					   }


					  //}


				  }


		)
	*/
	}, bottomBar = {

		contenidoBottomBar()
	}) { paddingValues ->

		Box(Modifier.padding(paddingValues)) {
			contenido()
		}
	}
}