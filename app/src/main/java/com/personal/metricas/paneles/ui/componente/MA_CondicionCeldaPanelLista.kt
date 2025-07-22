package com.personal.metricas.paneles.ui.componente

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.imagenes.MA_ImagenDrawable
import com.personal.metricas.core.composables.labels.MA_LabelLeyenda
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.labels.MA_LabelTextoDestacado
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.FuncionesCondicionesCeldaManager

@Composable
fun MA_CondicionCeldaPanelLista(
	condicion: Condiciones,
	onClickAceptar: (Condiciones) -> Unit,
	onClickCancelar: (Condiciones) -> Unit,
) {

	MA_Card(modifier = Modifier.clickable(enabled = true, onClick = {
		onClickAceptar(condicion)
	})) {
		Column(modifier = Modifier
			.clickable(enabled = true, onClick = {  })
			.width(250.dp)
			.height(250.dp)
			.padding(5.dp),
			   verticalArrangement = Arrangement.Center,
			  ) {

			val esquemaColores = EsquemaColores().dameEsquemaCondiciones()
			val condicionCelda = FuncionesCondicionesCeldaManager().get(condicion.condicionCelda)
			val indicadorColorCondicion = (condicion.color % esquemaColores.colores.size)
			val color = esquemaColores.colores.get(indicadorColorCondicion)


			//MA_LabelNormal(valor = condicion.id.toString())
			MA_LabelTextoDestacado(valor = "Sobre", valorDestacado = condicion.columna.nombre)
			MA_LabelTextoDestacado(valor = "Función", valorDestacado = condicionCelda.nombre)
			MA_LabelTextoDestacado(valor = "Condicion", valorDestacado = condicion.predicado.toString())
			MA_LabelLeyenda(valor = condicion.descripion)

			Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
				Box(Modifier.size(16.dp).background(color = color))
				condicionCelda.representaciones.forEach {
					MA_ImagenDrawable(it, s = 16.dp)
				}
			}
			MA_Spacer()
			MA_LabelNormal(alineacion = TextAlign.Center, valor = "Eliminar", color = Color.Red, modifier = Modifier.fillMaxWidth().clickable(enabled = true, onClick = { onClickCancelar(condicion) }))


		}
	}


}