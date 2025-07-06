package com.personal.requestmobility.paneles.ui.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenDrawable
import com.personal.requestmobility.core.composables.labels.MA_LabelLeyenda
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.tabla.MA_LabelCeldaTitulo
import com.personal.requestmobility.paneles.domain.entidades.Condiciones
import com.personal.requestmobility.paneles.domain.entidades.EsquemaColores
import com.personal.requestmobility.paneles.domain.entidades.FuncionesCondicionesCeldaManager

@Composable
fun MA_CondicionCeldaPanelLista(
	condicion: Condiciones,
	onClickAceptar: (Condiciones) -> Unit,
	onClickCancelar: (Condiciones) -> Unit) {
	
	
	Column(modifier = Modifier
		.clickable(enabled = true, onClick = { onClickAceptar(condicion) })
		.width(300.dp)
		.height(350.dp)
		.padding(1.dp),
			
		
			horizontalAlignment = Alignment.CenterHorizontally) {
		
		
		Row(modifier = Modifier.padding(1.dp), verticalAlignment = Alignment.CenterVertically) {
			val esquemaColores = EsquemaColores().dameEsquemaCondiciones()
			val indicadorColorCondicion = (condicion.color % esquemaColores.colores.size)
			val color = esquemaColores.colores.get(indicadorColorCondicion)
			
			MA_Card(modifier = Modifier.clickable(enabled = true, onClick = {
				onClickAceptar(condicion)
			})) {
				
				val condicionCelda = FuncionesCondicionesCeldaManager().get(condicion.condicionCelda)
				
				Column {
					//MA_LabelNormal(valor = condicion.id.toString())
					MA_LabelNormal(valor = condicion.columna.nombre)
					MA_LabelNormal(valor = condicionCelda.nombre)
					MA_LabelLeyenda(valor = condicion.descripion)
					
					MA_LabelNormal(valor = "Condicion: ${condicion.predicado.toString()}")
					
					Box(Modifier
						.size(36.dp)
						.background(color = color))
				
					Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
						condicionCelda.representaciones.forEach {
							MA_ImagenDrawable(it, s = 16.dp)
						}
					}
					MA_LabelNormal(valor = "Eliminar", color = Color.Red, modifier = Modifier.clickable(enabled = true, onClick = { onClickCancelar(condicion) }))
				}
			}
			
			
	
		}
	}
	
}