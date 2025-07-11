package com.personal.requestmobility.core.composables.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.componentes.Cabecera
import com.personal.requestmobility.core.composables.componentes.TituloScreen
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


	Scaffold(
		containerColor = Color(red = 227, green = 225, blue = 225, alpha = 0),
		topBar = {

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