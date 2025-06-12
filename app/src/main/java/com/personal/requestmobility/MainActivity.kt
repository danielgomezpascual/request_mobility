package com.personal.requestmobility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.personal.requestmobility.core.navegacion.NavegacionGuia
import com.personal.requestmobility.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.requestmobility.transacciones.domain.interactors.GuardarTransacciones
import com.personal.requestmobility.ui.theme.RequestMobilityTheme
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin

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


        val repoTrx : TransaccionesRepoImp = getKoin().get()
val guardar: GuardarTransacciones =getKoin().get()

        runBlocking {
            val trx = repoTrx.getTrxOracle()
            guardar.guardar(trx)

        }

    }



}

