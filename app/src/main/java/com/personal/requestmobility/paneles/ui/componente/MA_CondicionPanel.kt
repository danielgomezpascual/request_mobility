package com.personal.requestmobility.paneles.ui.componente

import MA_IconBottom
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.botones.MA_BotonNormal
import com.personal.requestmobility.core.composables.combo.MA_ComboLista
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.paneles.domain.entidades.EsquemaColores
import com.personal.requestmobility.paneles.ui.entidades.ColoresSeleccion
import com.personal.requestmobility.paneles.ui.entidades.Condiciones

@Composable
fun MA_CondicionPanel(
    esquemaColores: EsquemaColores,
    condicion: Condiciones,
    onClickAceptar: (Condiciones) -> Unit,
    onClickCancelar: (Condiciones) -> Unit) {


    var condicion by remember { mutableStateOf<Condiciones>(condicion) }
    var str by remember { mutableStateOf<String>(condicion.predicado) }



        Row (modifier = Modifier.fillMaxWidth()){
            MA_LabelNormal(  valor = condicion.id.toString())

            MA_ComboLista(
                modifier = Modifier.weight(1f),
                titulo = "",
                descripcion = "Color para la condicion",
                valorInicial = {
                    val color = ColoresSeleccion().get(esquemaColores.id).get(condicion.color).color


                    Box(
                        modifier =
                            Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .background(color = color)
                    ) {
                        MA_LabelNormal("")
                    }
                },
                elementosSeleccionables =ColoresSeleccion().get(esquemaColores.id),
                item = { colorSeleccion ->


                    Box(
                        modifier =
                            Modifier
                                .width(40.dp)
                                .height(40.dp)
                                .background(color =colorSeleccion.color)
                    ) {
                        MA_LabelNormal("Rojo")
                    }
                },
                onClickSeleccion = { colorSeleccion ->
                    condicion = condicion.copy(color = colorSeleccion.indice)
                }

            )

            MA_TextoNormal(modifier = Modifier.weight(1f), valor =str, titulo = "Predicado", onValueChange = { it ->
                condicion = condicion.copy(predicado = it)
                str = it
               

            })


            MA_IconBottom(icon =  Icons.Default.Check, labelText = "Guardar") { onClickAceptar(condicion)}
            MA_IconBottom(icon =  Icons.Default.Cancel, labelText = "ELiminar") {onClickCancelar(condicion)}
        }




}