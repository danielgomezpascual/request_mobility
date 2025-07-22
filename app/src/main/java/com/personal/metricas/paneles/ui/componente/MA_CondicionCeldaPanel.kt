package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.App
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.botones.MA_BotonSecundario
import com.personal.metricas.core.composables.combo.MA_ComboColores
import com.personal.metricas.core.composables.combo.MA_ComboLista
import com.personal.metricas.core.composables.edittext.MA_TextoNormal
import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.FuncionesCondicionesCeldaManager
import com.personal.metricas.paneles.ui.entidades.ColoresSeleccion

@Composable
fun MA_CondicionCeldaPanel(
	columnas: List<Columnas>,
	condicion: Condiciones,
	onClickAceptar: (Condiciones) -> Unit,
	onClickCancelar: (Condiciones) -> Unit,
) {


	var condicion by remember { mutableStateOf<Condiciones>(condicion) }
	//	var str by remember { mutableStateOf<String>(condicion.predicado) }	//var strDesripcion by remember { mutableStateOf<String>(condicion.descripion) }


	App.log.d(condicion.toString())

	Column(modifier = Modifier
		.fillMaxWidth()
		.padding(1.dp),

		   verticalArrangement = Arrangement.Center,
		   horizontalAlignment = Alignment.CenterHorizontally) {


		Row(modifier = Modifier.fillMaxWidth()) {
			MA_ComboLista<Columnas>(modifier = Modifier,
									titulo = "Columna ",
									descripcion = "Columna",
									valorInicial = {
										if (condicion.columna != null) MA_ColumnaItemSeleccionable(condicion.columna)
									},
									elementosSeleccionables = columnas,
									item = { columna -> MA_ColumnaItemSeleccionable(columna) },
									onClickSeleccion = { c ->
										condicion = condicion.copy(columna = c) //onClickAceptar(condicion)
									}


			)

			MA_ComboLista(titulo = "Funcionalidad",
						  descripcion = "Condicion a aplicar",
						  valorInicial = {
							  MA_FuncionalidadCelda(FuncionesCondicionesCeldaManager().get(condicion.condicionCelda).nombre)
						  },
						  elementosSeleccionables = FuncionesCondicionesCeldaManager().get(),
						  item = { fx -> MA_FuncionalidadCelda(fx.nombre) },
						  onClickSeleccion = { funcion ->

							  condicion = condicion.copy(condicionCelda = funcion.id, descripion = funcion.descripcion) //    onClickAceptar(condicion)
						  })
		}



		Row(modifier = Modifier.fillMaxWidth()) {
			val esquemaColores = EsquemaColores().dameEsquemaCondiciones()
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



			MA_TextoNormal(modifier = Modifier.weight(1f),
						   valor = condicion.predicado,
						   titulo = "Condición",
						   onValueChange = { it ->
							   condicion = condicion.copy(predicado = it)                        //str = it // onClickAceptar(condicion)
						   })


		}


		MA_TextoNormal(valor = condicion.descripion, titulo = "Descripcion", onValueChange = { it ->
			condicion = condicion.copy(descripion = it)                        //strDesripcion = it // onClickAceptar(condicion)
		})

		MA_Spacer()


		Row(modifier = Modifier.fillMaxWidth()) {
			MA_BotonSecundario(texto = "Cancelar") {
				onClickCancelar(condicion)

			}
			MA_BotonPrincipal(texto = "Guardar") {
				App.log.d(condicion.toString())
				onClickAceptar(condicion)

			}
		}
	}
}
	
	
