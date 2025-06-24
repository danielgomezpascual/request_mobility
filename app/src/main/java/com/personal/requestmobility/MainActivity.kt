package com.personal.requestmobility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.personal.requestmobility.core.navegacion.NavegacionGuia
import com.personal.requestmobility.core.composables.dialogos.AppGlobalDialogs
import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.ui.theme.RequestMobilityTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            RequestMobilityTheme {

                NavegacionGuia()
                val dialogManager: DialogManager = org.koin.compose.getKoin().get()
                AppGlobalDialogs(dialogManager)
            }

        }



    }


}

