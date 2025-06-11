package com.personal.requestmobility.core.composables.dialogos

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.personal.requestmobility.core.composables.botones.MA_BotonNormal

import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.labels.MA_Titulo2
import com.personal.requestmobility.core.composables.modales.MA_BottomSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class ResultadoDialog{
    object Confirmar: ResultadoDialog()
    object Si: ResultadoDialog()
    object No: ResultadoDialog()
    object Cancelar: ResultadoDialog()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_Dialogo(trigger: @Composable (show: () -> Unit) -> Unit, resultado : (ResultadoDialog) -> Unit) {

    /*  val sheetState = rememberModalBottomSheetState()
      val scope = rememberCoroutineScope() // Se mantiene dentro del componente*/
    //var elemntoSeleccionado by remember { mutableStateOf(valorInicial) }


    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope() // Se mantiene dentro del componente


    val mostrarDialogo: () -> Unit = {
        scope.launch { sheetState.show() }
    }

    val cerrar : () -> Unit =  {
        scope.launch { sheetState.hide() }
    }

    trigger(mostrarDialogo)


    if (sheetState.isVisible) {
        MA_BottomSheet(
            sheetState = sheetState,
            onClose = {
                { scope.launch { sheetState.hide() } }
            },
            contenido = {
                Column {
                    MA_Titulo2("Campod e prebas")
                    MA_LabelNormal(valor = "El campo no tiene flores. ¿Se as quieres plantar?")


                    MA_BotonNormal(texto = "Boton SI ") {
                        cerrar()
                        resultado(ResultadoDialog.Si) }

                    MA_BotonNormal(texto = "Boton NO ") {
                        cerrar()
                        resultado(ResultadoDialog.No) }
                }



            }
        )
    }

}