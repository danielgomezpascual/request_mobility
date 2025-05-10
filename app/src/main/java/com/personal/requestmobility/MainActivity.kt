package com.personal.requestmobility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.personal.requestmobility.core.navegacion.NavegacionGuia
import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.transacciones.domain.interactors.ObtenerKPIsCU
import com.personal.requestmobility.ui.theme.RequestMobilityTheme
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            RequestMobilityTheme {
                NavegacionGuia()
            }

        }
    }



}

