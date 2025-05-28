package com.personal.requestmobility.menu.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.botones.MA_BotonNormal
import com.personal.requestmobility.core.composables.imagenes.selector.MA_ImagenSelector
import com.personal.requestmobility.menu.navegacion.Modulos

@Composable
fun ScreenMenu(accion: (Modulos) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {


        Text("Selecciona tu foto de perfil:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        var userImageFilePath by remember { mutableStateOf<String?>(null) }
        var feedbackMessage by remember { mutableStateOf("") }


        MA_ImagenSelector(
            defaultImageResId = android.R.drawable.sym_def_app_icon, // Reemplaza con tu drawable por defecto
            // defaultImageResId = R.drawable.ic_default_profile, // Si tienes uno propio
            onImageStored = { filePath ->
                userImageFilePath = filePath
                if (filePath != null) {
                    feedbackMessage = "Imagen guardada en: $filePath"
                    // Aquí puedes guardar 'filePath' en tu base de datos
                    App.log.d("Esta es la ruta: $feedbackMessage")
                } else {
                    feedbackMessage = "No se seleccionó o guardó ninguna imagen."
                }
            }
        )



        Text("Menu")
        MA_BotonNormal("Peticiones") { accion(Modulos.Transacciones) }
        MA_BotonNormal("Kpis") { accion(Modulos.Kpis) }
        MA_BotonNormal("Dashboard") { accion(Modulos.Dashboards) }
        MA_BotonNormal("Cuadricula") { accion(Modulos.Cuadricula) }
        MA_BotonNormal("Panel") { accion(Modulos.Paneles) }
    }
}