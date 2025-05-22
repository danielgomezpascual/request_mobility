package com.personal.requestmobility.core.composables.combo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.layouts.MA_Box
import com.personal.requestmobility.core.composables.listas.MA_Lista
import com.personal.requestmobility.core.composables.modales.MA_BottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MA_Combo(titulo: String = "[TITULO]",
             descripcion: String = "",
             valorInicial: String = "",
             elementosSeleccionables: List<String>,
             onClickSeleccion: (String, Int) -> Unit) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope() // Se mantiene dentro del componente
    var textoSeleccionado by remember { mutableStateOf(valorInicial) }



    MA_Box(modifier = Modifier.clickable(enabled = true, onClick = { scope.launch { sheetState.show() } })) {
        Column {
            valorInicial
            MA_LabelNormal(titulo)
            Row {
                Text(fontWeight = FontWeight.ExtraBold, text =  textoSeleccionado)

            }


        }
    }


    MA_BottomSheet(
        sheetState,
        onClose = {
            { scope.launch { sheetState.hide() } }
        },
        contenido = {
            Column(
                Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = titulo,
                    style = MaterialTheme.typography.headlineLarge,
                )

                if (!descripcion.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        color = Color.Gray,
                        text = "Seleccione un aopcion correspondiente",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                }
                MA_Lista(elementosSeleccionables) { elemento ->
                    val posicion = elementosSeleccionables.indexOf(elemento)
                    Box(modifier = Modifier.clickable(enabled = true, onClick = {
                        textoSeleccionado= elemento
                        onClickSeleccion(elemento, posicion)
                        scope.launch { sheetState.hide() }
                    }
                    )) {

                        Column {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = elemento,
                                style = MaterialTheme.typography.bodyMedium,
                            )



                        }


                    }


                }
            }

        }
    )
}




