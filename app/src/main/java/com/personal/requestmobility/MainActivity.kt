package com.personal.requestmobility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.personal.requestmobility.core.navegacion.NavegacionGuia
import com.personal.requestmobility.organizaciones.data.repositorio.OrganizacionesRepoImp
import com.personal.requestmobility.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.requestmobility.transacciones.domain.interactors.GuardarTransacciones
import com.personal.requestmobility.ui.theme.RequestMobilityTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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

        /*

        val repoTrx: TransaccionesRepoImp = getKoin().get()
        val guardar: GuardarTransacciones = getKoin().get()
        val repoOrganizaciones: OrganizacionesRepoImp = getKoin().get()


        runBlocking {
            val t = repoOrganizaciones.getAll()
            val totalOrganizaciones = t.size
            App.log.d("Organizaciones encontradas ${t.size}")

            t.filter { it.organizationCode.equals("DFM") }.forEachIndexed { indice, org ->

                launch {
                    App.log.d("[${indice+1} / $totalOrganizaciones] Procesando Organizacion encontradas ${org.toString()}")
                    val trx = repoTrx.getTrxOracle(org.organizationId)
                    guardar.guardar(trx)
                }
            }
        }*/

    }


}

