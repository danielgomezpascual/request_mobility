package com.personal.metricas.core.composables.dialogos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.personal.metricas.App

@Composable
fun AppGlobalDialogs(dialogManager: DialogManager) {

    val dialogoActual by dialogManager.dialogoActual.collectAsState()


    when (val dialogo = dialogoActual) {
        is Dialogos.Vacio -> {
            // No hacemos nada, no hay diálogo que mostrar
        }

        is Dialogos.Informacion -> {
            /* dialogManager.informacion(dialogo.texto) {
                 dialogo.onResult(DialogosResultado.Confirmado)
             }*/
            MA_Dialogo_Informacion(
                visibilidad = true,
                textoDialogo = dialogo.texto,
                resultado = { dialogo.onResult(DialogosResultado.Confirmado) }
            )
        }

        is Dialogos.SiNo -> {
            /*   dialogManager.sino(dialogo.texto) { res ->
                   dialogo.onResult(res)
               }*/
            MA_Dialogo_SiNo(
                visibilidad = true,
                textoDialogo = dialogo.texto,
                resultado = { res ->
                    App.log.d("EN el gestor dedialogos $res")
                    dialogo.onResult(res)
                })
        }

        is Dialogos.Input -> {
            MA_Dialogo_Input (
                visibilidad = true,
                textoInformacion = dialogo.textoInformacion,
                textoInicialEditText = dialogo.textoInicial,
                resultado = { res, str ->
                    dialogo.onResult(res, str)
                })
        }

        is Dialogos.Nota -> {
            MA_Dialogo_Nota (
                visibilidad = true,
                textoInformacion = dialogo.textoInformacion,
                textoInicialEditText = dialogo.textoInicial,
                resultado = { res, str ->
                    dialogo.onResult(res, str)
                })
        }
    }

}
