package com.personal.requestmobility.core.composables.scaffold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.labels.MA_Titulo
import com.personal.requestmobility.core.navegacion.EventosNavegacion


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_ScaffoldGenerico(
    titulo: String,
    navegacion: (EventosNavegacion) -> Unit = {},
    volver: Boolean = false,
    contenidoBottomBar: @Composable () -> Unit = {},
    contenido: @Composable () -> Unit,

    ) {


    Scaffold(
        topBar = {

            TopAppBar(
                expandedHeight = if (titulo.isNotEmpty()) 95.dp else 15.dp,
                title = {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                    ) {

                        Column(
                            modifier =
                                Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            if (titulo.isNotEmpty()) {
                                MA_Avatar(titulo, size = 45.dp/*, color = MaterialTheme.colorScheme.primary*/)
                            }
                            MA_Titulo(titulo)
                        }
                    }

                },
                navigationIcon = {
                    if (volver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Volver",
                            modifier = Modifier.clickable(
                                enabled = true, onClick =
                                    {
                                        navegacion(EventosNavegacion.Volver)
                                    })
                        )
                    }

                })
        },
        bottomBar = {
            contenidoBottomBar()
        }
    ) { paddingValues ->
        Box(Modifier.padding(8.dp)) {

            Box(modifier = Modifier.padding(paddingValues)) {
                contenido()


            }


        }

    }
}