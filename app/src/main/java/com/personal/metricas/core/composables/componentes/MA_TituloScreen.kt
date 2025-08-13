package com.personal.metricas.core.composables.componentes

import MA_IconBottom
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.personal.metricas.R
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.imagenes.MA_ImagenDrawable
import com.personal.metricas.core.composables.imagenes.MA_ImagenCirculoURL
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.firebase.FirebaseManager
import com.personal.metricas.menu.Features

@Composable
fun Cabecera(cabecera: TituloScreen, navegacion: (EventosNavegacion) -> Unit, acciones: @Composable () -> Unit = {}) {

	val context = LocalContext.current
	val currentUser = FirebaseAuth.getInstance().currentUser

	MA_Card(elevacion = 0.dp, modifier = Modifier.padding(0.dp)) {

		val m = Modifier.fillMaxWidth()
		Column(modifier = m
			.padding(start = 5.dp, top = 15.dp)
			.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {


			Row(modifier = m,
				verticalAlignment = Alignment.CenterVertically) {
				//MA_ImagenDrawable(imagen = R.drawable.logo, s = 24.dp)
				val auth = FirebaseManager().getAuth()
				MA_ImagenCirculoURL(url = auth.currentUser?.photoUrl.toString())
				MA_Titulo(
					modifier = m
						.fillMaxWidth()
						.weight(1f),
					alineacion = TextAlign.Start,
					valor = cabecera.titulo)

				Row(
					modifier = m
						.weight(1f)
					,
					horizontalArrangement = Arrangement.End,
					verticalAlignment = Alignment.Top

				) {
					Box(modifier = m.weight(1f), contentAlignment = Alignment.CenterEnd) {

						acciones()
					}
					Box(modifier = m.weight(1f), contentAlignment = Alignment.CenterEnd) {
						MA_IconBottom(
							//   modifier = Modifier.weight(1f),
							icon = Features.CerrarSesion().icono,

							seleccionado = false,
							destacado = false,
							onClick = {


								AuthUI.getInstance()
									.signOut(context)
									.addOnCompleteListener {
										navegacion(EventosNavegacion.MenuApp)
									}

							}
						)
					}
				}
			}




			HorizontalDivider(thickness = 1.dp)
		}


	}

}