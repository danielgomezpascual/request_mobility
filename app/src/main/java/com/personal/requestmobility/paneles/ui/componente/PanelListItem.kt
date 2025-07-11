package com.personal.requestmobility.paneles.ui.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenAssets
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenDrawable
import com.personal.requestmobility.core.composables.labels.MA_LabelEtiqueta
import com.personal.requestmobility.core.composables.labels.MA_LabelExtendido
import com.personal.requestmobility.core.composables.labels.MA_LabelMini
import com.personal.requestmobility.core.composables.labels.MA_LabelNegrita
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.listas.MA_Divider
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.paneles.domain.entidades.PanelTipoGrafica
import com.personal.requestmobility.paneles.ui.entidades.PanelUI

@Composable
fun PanelListItem(
	panelUI: PanelUI,
	onClickItem: (PanelUI) -> Unit,
) {

	//MA_Card {
	Column(modifier = Modifier.padding(vertical = 5.dp)) {

		Row(modifier = Modifier.Companion
			.fillMaxWidth()
			.clickable { onClickItem(panelUI)/* Manejar clic en el usuario  viewModel.abrirUsuario(usuario)*/ }
			.padding(5.dp), verticalAlignment = Alignment.Companion.CenterVertically) {


			Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
				MA_Avatar(panelUI.titulo)
				MA_InfoPanel(panelUI)
			}
			Spacer(modifier = Modifier.Companion.width(5.dp))

			// Nombre y detalles
			Column {
				Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {

					//MA_LabelNegrita(modifier = Modifier.weight(1f), valor = "${panelUI.titulo}")


				}
				MA_LabelNegrita(valor = "${panelUI.titulo}")
				MA_LabelMini(valor = "${panelUI.descripcion}")


			}
		}

		//
		MA_Divider()
		//Spacer(Modifier.width(18.dp))    //}
	}


}

@Composable
fun MA_InfoPanel(panel: PanelUI, mostrarNombre: Boolean = false) {


	Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {

		if (mostrarNombre) {
			MA_LabelMini(panel.titulo)
		}
		MA_Avatar("", size = 12.dp, color = panel.kpi.dameColorDinamico(), fontSize = 12.sp)

		Spacer(Modifier.width(5.dp))
		if (panel.configuracion.mostrarGrafica) {
			val idGrafico = when (panel.configuracion.tipo) {
				is PanelTipoGrafica.Anillo                 -> panel.configuracion.tipo.icono
				is PanelTipoGrafica.BarrasAnchasVerticales -> panel.configuracion.tipo.icono
				is PanelTipoGrafica.BarrasFinasVerticales  -> panel.configuracion.tipo.icono
				is PanelTipoGrafica.Circular               -> panel.configuracion.tipo.icono
				is PanelTipoGrafica.IndicadorHorizontal    -> panel.configuracion.tipo.icono
				is PanelTipoGrafica.IndicadorVertical      -> panel.configuracion.tipo.icono
				is PanelTipoGrafica.Lineas                 -> panel.configuracion.tipo.icono
			}
			MA_ImagenDrawable(idGrafico, s = 16.dp)
		}
		Spacer(Modifier.width(5.dp))
		if (panel.configuracion.mostrarTabla) {
			MA_ImagenDrawable(R.drawable.tabla, s = 16.dp)
		}
		Spacer(Modifier.width(5.dp))

	}


}


@Composable
fun MA_InfoPanelVertical(panel: PanelUI, mostrarNombre: Boolean = false) {

	Column (horizontalAlignment = Alignment.CenterHorizontally){

		if (mostrarNombre) {
			MA_LabelMini(panel.titulo, alineacion = TextAlign.Center)
		}

		Row(modifier = Modifier.padding(3.dp),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically) {


			MA_Avatar("", size = 12.dp, color = panel.kpi.dameColorDinamico(), fontSize = 12.sp)

			Spacer(Modifier.width(5.dp))
			if (panel.configuracion.mostrarGrafica) {
				val idGrafico = when (panel.configuracion.tipo) {
					is PanelTipoGrafica.Anillo                 -> panel.configuracion.tipo.icono
					is PanelTipoGrafica.BarrasAnchasVerticales -> panel.configuracion.tipo.icono
					is PanelTipoGrafica.BarrasFinasVerticales  -> panel.configuracion.tipo.icono
					is PanelTipoGrafica.Circular               -> panel.configuracion.tipo.icono
					is PanelTipoGrafica.IndicadorHorizontal    -> panel.configuracion.tipo.icono
					is PanelTipoGrafica.IndicadorVertical      -> panel.configuracion.tipo.icono
					is PanelTipoGrafica.Lineas                 -> panel.configuracion.tipo.icono
				}
				MA_ImagenDrawable(idGrafico, s = 16.dp)
			}
			Spacer(Modifier.width(5.dp))
			if (panel.configuracion.mostrarTabla) {
				MA_ImagenDrawable(R.drawable.tabla, s = 16.dp)
			}
			Spacer(Modifier.width(5.dp))

		}
	}


}