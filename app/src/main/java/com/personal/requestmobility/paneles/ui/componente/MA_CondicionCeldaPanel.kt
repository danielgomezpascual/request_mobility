package com.personal.requestmobility.paneles.ui.componente

import MA_IconBottom
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.combo.MA_ComboColores
import com.personal.requestmobility.core.composables.combo.MA_ComboLista
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.tabla.Columnas
import com.personal.requestmobility.core.composables.tabla.MA_LabelCelda
import com.personal.requestmobility.paneles.domain.entidades.EsquemaColores
import com.personal.requestmobility.paneles.domain.entidades.FuncionesCondicionesCeldaManager

import com.personal.requestmobility.paneles.ui.entidades.ColoresSeleccion
import com.personal.requestmobility.paneles.ui.entidades.CondicionesCelda

@Composable
fun MA_CondicionCeldaPanel(
    columnas: List<Columnas>,

    condicion: CondicionesCelda,
    onClickAceptar: (CondicionesCelda) -> Unit,
    onClickCancelar: (CondicionesCelda) -> Unit) {


    var condicion by remember { mutableStateOf<CondicionesCelda>(condicion) }
    var str by remember { mutableStateOf<String>(condicion.predicado) }




    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),

        //verticalAlignment = Alignment.CenterVertically
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        MA_ComboLista<Columnas>(
            modifier = Modifier.weight(1f),
            titulo = "Columna ",
            descripcion = "Columna",
            valorInicial = { if (condicion.columna != null) MA_ColumnaItemSeleccionable(condicion.columna) },
            elementosSeleccionables = columnas,
            item = { columna -> MA_ColumnaItemSeleccionable(columna) },
            onClickSeleccion = { c ->
                condicion = condicion.copy(columna = c)
                onClickAceptar(condicion)
            }


        )


        Row(  modifier = Modifier
            .fillMaxWidth()
          ){
            val esquemaColores = EsquemaColores().dameEsquemaCondiciones()
            MA_ComboColores(
                modifier = Modifier.weight(1f),
                titulo = "",
                descripcion = "Color para la condicion",
                valorInicial = {
                    val indicadorColorCondicion = (condicion.color % esquemaColores.colores.size)
                    val color = esquemaColores.colores.get(indicadorColorCondicion)
                    MA_SeleccionColor(color)
                },
                elementosSeleccionables = ColoresSeleccion().get(esquemaColores.id),
                item = { colorSeleccion ->
                    MA_SeleccionColor(colorSeleccion.color)
                },
                onClickSeleccion = { colorSeleccion ->
                    condicion = condicion.copy(color = colorSeleccion.indice)
                    onClickAceptar(condicion)
                }
            )



            MA_ComboLista(
                titulo = "Funcionalidad",
                descripcion = "Condicion ea aplicar",
                valorInicial = { MA_FuncionalidadCelda(FuncionesCondicionesCeldaManager().get(condicion.condicionCelda).nombre) },
                elementosSeleccionables =FuncionesCondicionesCeldaManager().get(),
                item = { fx -> MA_FuncionalidadCelda(fx.nombre) },
                onClickSeleccion = { funcion ->
                    condicion = condicion.copy(condicionCelda = funcion.id)
                    onClickAceptar(condicion)
                }
            )


            MA_TextoNormal(modifier = Modifier.weight(1f), valor = str, titulo = "Condición", onValueChange = { it ->
                condicion = condicion.copy(predicado = it)
                str = it
                onClickAceptar(condicion)
            })



            MA_IconBottom(color = Color.Red, icon = Icons.Default.Cancel, labelText = "") { onClickCancelar(condicion) }
        }


    }


}