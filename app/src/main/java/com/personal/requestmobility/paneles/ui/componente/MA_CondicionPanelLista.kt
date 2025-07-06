package com.personal.requestmobility.paneles.ui.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.labels.MA_LabelLeyenda
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.paneles.domain.entidades.EsquemaColores
import com.personal.requestmobility.paneles.domain.entidades.Condiciones

@Composable
fun MA_CondicionPanelLista(
    esquemaColores: EsquemaColores,
    condicion: Condiciones,
    onClickAceptar: (Condiciones) -> Unit,
    onClickCancelar: (Condiciones) -> Unit) {


    //var condicion by remember { mutableStateOf<Condiciones>(condicion) }
  //  var str by remember { mutableStateOf<String>(condicion.predicado) }


    val esquemaColores = EsquemaColores().dameEsquemaCondiciones()
    //esquemaColores.get()

    Row(
        modifier = Modifier
            .padding(1.dp).width(250.dp).height(250.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //MA_LabelNormal(valor = condicion.id.toString())

        val indicadorColorCondicion = (condicion.color % esquemaColores.colores.size)
        val color = esquemaColores.colores.get(indicadorColorCondicion)


        MA_Card(modifier = Modifier.clickable(enabled = true, onClick = {
            onClickAceptar(condicion)
        })) {
        Column {
            MA_LabelNormal(valor = condicion.id.toString())
            MA_LabelLeyenda(valor = condicion.descripion)
            Box(Modifier
                .size(36.dp)
                .background(color = color))
            MA_LabelNormal(valor = condicion.predicado)
            MA_LabelNormal(valor ="Eliminar", color = Color.Red,
                modifier =  Modifier.clickable(enabled = true, onClick = {onClickCancelar(condicion)})
                          )
        }
    }


    
    }


}