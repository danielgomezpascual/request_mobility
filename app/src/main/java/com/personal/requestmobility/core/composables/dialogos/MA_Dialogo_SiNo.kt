package com.personal.requestmobility.core.composables.dialogos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.botones.MA_BotonPrincipal
import com.personal.requestmobility.core.composables.botones.MA_BotonSecundario
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.labels.MA_Titulo
import com.personal.requestmobility.core.composables.modales.MA_BottomSheet
import kotlinx.coroutines.launch

sealed class ResultadoDialog {
    object Confirmar : ResultadoDialog()
    object Si : ResultadoDialog()
    object No : ResultadoDialog()
    object Cancelar : ResultadoDialog()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_Dialogo_SiNo(
    visibilidad : Boolean,
    titulo: String = "Atención",
    textoDialogo: String,
    resultado: (DialogosResultado) -> Unit) {


    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope() // Se mantiene dentro del componente


    val mostrarDialogo: () -> Unit = {  scope.launch { sheetState.show() } }
    val cerrar: () -> Unit = { scope.launch { sheetState.hide() } }

    mostrarDialogo()


    MA_BottomSheet(
        sheetState = sheetState,
        onClose = {
                        cerrar()
            resultado(DialogosResultado.Descartado)
        },
        contenido = {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.QuestionMark, contentDescription = titulo,
                    /*tint = Color.Red*/
                )

                MA_Titulo(valor = titulo)
                HorizontalDivider(Modifier.fillMaxWidth())
                Spacer(Modifier.size(16.dp))
                MA_LabelNormal(valor = textoDialogo, alineacion = TextAlign.Center)
                Spacer(Modifier.size(16.dp))

                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
                    MA_BotonSecundario(texto = "Cancelar") { cerrar()
                        resultado(DialogosResultado.No)}

                    Spacer(Modifier.size(16.dp))

                    MA_BotonPrincipal(texto = "Aceptar ") {
                        cerrar()
                        resultado(DialogosResultado.Si)
                    }
                }

            }


        }
    )
    // }


}