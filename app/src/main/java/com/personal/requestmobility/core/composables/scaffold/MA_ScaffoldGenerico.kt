package com.personal.requestmobility.core.composables.scaffold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
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
                title = {

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                    ) {
                        MA_Titulo(titulo)

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
        Box(modifier = Modifier.padding(paddingValues)) {
            contenido()


        }
    }
}