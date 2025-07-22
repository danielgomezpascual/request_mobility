package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.personal.metricas.App
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.combo.MA_ComboColores
import com.personal.metricas.core.composables.combo.MA_ComboLista
import com.personal.metricas.core.composables.edittext.MA_TextoNormal
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.ui.entidades.ColoresSeleccion
import com.personal.metricas.paneles.domain.entidades.Condiciones

@Composable
fun MA_CondicionPanel(
	columnas: List<Columnas>,
	esquemaColores: EsquemaColores,
	condicion: Condiciones,
	onClickAceptar: (Condiciones) -> Unit,
	onClickCancelar: (Condiciones) -> Unit,
					 )
{
	
	
	var condicion by remember { mutableStateOf<Condiciones>(condicion) }
	// var str by remember { mutableStateOf<String>(condicion.predicado) }
	
	
	val esquemaColores = EsquemaColores().dameEsquemaCondiciones()
	//esquemaColores.get()
	
	
	Column {

		MA_Titulo("Condiciones a aplicar sobre la fila")
		MA_ComboLista<Columnas>(
				modifier = Modifier,
				titulo = "Columna ",
				descripcion = "Columna",
				valorInicial = { if (condicion.columna != null) MA_ColumnaItemSeleccionable(condicion.columna) },
				elementosSeleccionables = columnas,
				item = { columna -> MA_ColumnaItemSeleccionable(columna) },
				onClickSeleccion = { c ->
					condicion = condicion.copy(columna = c)
					//onClickAceptar(condicion)
					App.log.d(condicion.toString())
				})
		
		MA_TextoNormal(/*modifier = Modifier.weight(1f), */
					   valor = condicion.descripion,
					   titulo = "Descripcion",
					   onValueChange = { it ->
						   condicion = condicion.copy(descripion = it)
					   })
		
		
		//MA_LabelNormal(valor = condicion.id.toString())
		Row(){
			/*MA_ComboLista(
					/*modifier = Modifier.weight(1f),*/
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
						//onClickAceptar(condicion)
					}
						 
						 )*/

			MA_ComboColores(modifier = Modifier.weight(1f),
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
								condicion = condicion.copy(color = colorSeleccion.indice) //   onClickAceptar(condicion)
							})
			
			MA_TextoNormal(/*modifier = Modifier.weight(1f),*/ valor = condicion.predicado, titulo = "Condición", onValueChange = { it ->
				condicion = condicion.copy(predicado = it)
				//str = it
				/// onClickAceptar(condicion)
			})
		}
		
		
		
		//    MA_IconBottom(icon = Icons.Default.Check, labelText = "") { onClickAceptar(condicion) }
		//  MA_IconBottom(color = Color.Red, icon = Icons.Default.Cancel, labelText = "") { onClickCancelar(condicion) }
		
		
		Row() {
			MA_BotonSecundario(texto = "Cancelar") {
				onClickCancelar(condicion)
				
			}
			MA_BotonPrincipal(texto = "Guardar") {
				onClickAceptar(condicion)
				
			}
		}
		
		
	}
	
	
}