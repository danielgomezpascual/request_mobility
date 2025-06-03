package com.personal.requestmobility.core.composables.listas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T>MA_Seleccion(contenido: @Composable () -> Unit , value: T,  onClick: (T) -> Unit){
    Column(modifier = Modifier .clickable { onClick(value)/* Manejar clic en el usuario  viewModel.abrirUsuario(usuario)*/ }) {
        contenido()
    }
}