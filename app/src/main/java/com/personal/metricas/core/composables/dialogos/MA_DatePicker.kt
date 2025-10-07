package com.personal.metricas.core.composables.dialogos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.App
import com.personal.metricas.App.Companion.context
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils.TiempoHora
import com.personal.metricas.core.utils.if3
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(
	onDateSelected: (String) -> Unit,
) {
	// Estado para controlar la visibilidad del diálogo
	val showDatePicker = remember { mutableStateOf(false) }

	// Estado del selector de fecha
	val datePickerState = rememberDatePickerState(
		initialSelectedDateMillis = Instant.now().toEpochMilli()
	)

	// Formato para mostrar la fecha
	val selectedDateText by remember {
		derivedStateOf {
			datePickerState.selectedDateMillis?.let {
				val instant = Instant.ofEpochMilli(it)
				val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
				instant.atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)
			}
		}
	}

	val diaDefecto = TiempoHora.obtenerDiaDelMesNumerico().toString()
	//MA_Titulo("Dia : $diaDefecto")

	App.log.d("Dia defecto $diaDefecto");
	App.log.d("Dia Preferencias $diaDefecto");
	val diaBusqueda = App.sharedPrerfences.get(K.DIA, diaDefecto)

	val color : Color = if3(diaBusqueda.equals(diaDefecto),MaterialTheme.colorScheme.primary, Color(252, 111, 4, 255))
	Box(contentAlignment = Alignment.Center){
		MA_LabelNormal(
			modifier = Modifier.padding(top= 5.dp),
			color = color,
			valor =diaBusqueda,
			size = 16.sp, fontWeight = FontWeight.Bold)



		MA_Icono(icono = Icons.Default.CalendarToday, color = MaterialTheme.colorScheme.primary, modifier = Modifier
			.size(36.dp)
			.clickable(enabled = true,
					   onClick = {
						   showDatePicker.value = true
					   }))
	}


	/*// Botón que abre el diálogo
	Button(onClick = { showDatePicker.value = true }) {
		Text("Seleccionar Fecha")
	}
*/
	// El diálogo en sí
	if (showDatePicker.value) {
		DatePickerDialog(
			onDismissRequest = {
				showDatePicker.value = false
			},
			confirmButton = {
				TextButton(
					onClick = {
						showDatePicker.value = false
						selectedDateText?.let {
							onDateSelected(it)
						}
					}
				) {
					Text("Aceptar")
				}
			},
			dismissButton = {
				TextButton(
					onClick = {
						showDatePicker.value = false
					}
				) {
					Text("Cancelar")
				}
			}
		) {
			DatePicker(state = datePickerState)
		}
	}
}