package com.personal.metricas.core.composables.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.firebase.auth.FirebaseAuth
import com.personal.metricas.App
import com.personal.metricas.App.Companion.ENTORNO
import com.personal.metricas.R
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.dialogos.DatePickerButton
import com.personal.metricas.core.composables.imagenes.MA_ImagenCirculoURL
import com.personal.metricas.core.composables.imagenes.MA_ImagenDrawable
import com.personal.metricas.core.composables.modales.MA_BottomSheet
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.firebase.domain.FirebaseManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cabecera(
	cabecera: TituloScreen,
	navegacion: (EventosNavegacion) -> Unit,
	acciones: @Composable () -> Unit = {},
) {

	val context = LocalContext.current
	val currentUser = FirebaseAuth.getInstance().currentUser

	val appDatabase = getKoin().get<AppDatabase>()
	val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase

	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	val scope = rememberCoroutineScope()

	// Fondo con gradiente sutil
	Box(
		modifier =
			Modifier
				.fillMaxWidth()
				.background(
					brush =
						Brush.verticalGradient(
							colors =
								listOf(
									Color(0xFFF8F9FA),
									Color(0xFFFFFFFF)
								)
						)
				)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp, vertical = 10.dp),
			verticalAlignment = Alignment.CenterVertically

		) {
			// Avatar/Logo
			val auth = FirebaseManager().getAuth()
			Box(
				modifier =
					Modifier
						.size(36.dp)
						.clickable(
							enabled = true,
							onClick = { navegacion(EventosNavegacion.Settings) }
						)
			) {
				if (auth.currentUser == null || auth.currentUser?.photoUrl == null) {
					MA_ImagenDrawable(imagen = R.drawable.logo, s = 36.dp)
				} else {
					MA_ImagenCirculoURL(url = auth.currentUser?.photoUrl.toString())
				}
			}

			MA_Spacer(Modifier.padding(2.dp))


			// Selector de fecha compacto debajo del avatar
			Surface(
			//	shape = RoundedCornerShape(6.dp),
				color = Color.White,
			//	shadowElevation = 1.dp

			) {
				Row(
					modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center
				) {
					// Icono de calendario pequeño
					DatePickerButton(
						onDateSelected = { date ->
							val f_start: String = date.toString()
							modificarVistaDatosTransacciones(navegacion, fechaInicial = f_start)
						}
					)

					MA_Spacer(Modifier.padding(2.dp))

					// Texto del periodo
					val MAX_DIAS_CONSULTA = 1000
					var diasConsulta: String =
						App.sharedPrerfences
							.get<Int>(
								Preferencias.DIAS_CONSULTA_TRANSACCIONES,
								MAX_DIAS_CONSULTA
							)
							.toString()
					diasConsulta =
						if3(
							diasConsulta == MAX_DIAS_CONSULTA.toString(),
							"Todo",
							"$diasConsulta d"
						)



					MA_Spacer()
					// Título y entorno (ocupa el espacio restante)
					Column(
						modifier = Modifier.weight(1f),
						verticalArrangement = Arrangement.Center
					) {
						Text(
							text = cabecera.titulo,
							style = MaterialTheme.typography.titleLarge,
							fontWeight = FontWeight.SemiBold,
							fontSize = 18.sp,
							color = Color(0xFF1F2937),
							maxLines = 1
						)

						val colorEntorno =
							if3(
								App.sharedPrerfences.get<Boolean>(
									Preferencias.ENTORNO_PRO,
									false
								),
								Color(0xFFDC2626),
								Color(0xFF2563EB)
							)
						Row() {

							Surface(
								shape = RoundedCornerShape(4.dp),
								color = Color(0xFFF3F4F6),
								modifier = Modifier.clickable(
									enabled = true,
									onClick = { scope.launch { sheetState.show() } }
								)
							) {
								Text(
									text = diasConsulta,
									modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
									style = MaterialTheme.typography.labelSmall,
									color = Color(0xFF6B7280),
									fontSize = 9.sp,
									fontWeight = FontWeight.Medium
								)


							}

							MA_Spacer()

							Surface(
								modifier = Modifier.padding(top = 2.dp),
								shape = RoundedCornerShape(4.dp),
								color = colorEntorno.copy(alpha = 0.1f)
							) {
								Text(
									text = App.ENTORNO,
									modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
									style = MaterialTheme.typography.labelSmall,
									color = colorEntorno,
									fontSize = 9.sp,
									fontWeight = FontWeight.Medium
								)
							}

						}
					}
				}
				MA_Spacer(Modifier.padding(4.dp))
				// Acciones - Sin weight para que ocupen solo el espacio necesario
				Row(
					modifier = Modifier.padding(5.dp).weight(1f),
					horizontalArrangement = Arrangement.End,
					verticalAlignment = Alignment.CenterVertically
				) {
					acciones()
				}
			}






		}

		// Bottom Sheet para filtros de fecha
		MA_BottomSheet(
			sheetState,
			onClose = { { scope.launch { sheetState.hide() } } },
			contenido = {
				Column(modifier = Modifier.fillMaxWidth()) {
					// Título del sheet
					Text(
						text = "Seleccionar Período",
						style = MaterialTheme.typography.titleLarge,
						fontWeight = FontWeight.Bold,
						fontSize = 20.sp,
						color = Color(0xFF1F2937),
						modifier = Modifier.padding(bottom = 16.dp)
					)

					MA_FiltroFecha(Etiquetas("Completo")) {
						modificarVistaDatosTransacciones(navegacion, dias = 1000)
						scope.launch { sheetState.hide() }
					}
					MA_FiltroFecha(Etiquetas("1 día")) {
						modificarVistaDatosTransacciones(navegacion, dias = 1)
						scope.launch { sheetState.hide() }
					}
					MA_FiltroFecha(Etiquetas("3 días")) {
						modificarVistaDatosTransacciones(navegacion, dias = 3)
						scope.launch { sheetState.hide() }
					}
					MA_FiltroFecha(Etiquetas("5 días")) {
						modificarVistaDatosTransacciones(navegacion, dias = 5)
						scope.launch { sheetState.hide() }
					}
					MA_FiltroFecha(Etiquetas("7 días")) {
						modificarVistaDatosTransacciones(navegacion, dias = 7)
						scope.launch { sheetState.hide() }
					}
					MA_FiltroFecha(Etiquetas("15 días")) {
						modificarVistaDatosTransacciones(navegacion, dias = 15)
						scope.launch { sheetState.hide() }
					}
					MA_FiltroFecha(Etiquetas("30 días")) {
						modificarVistaDatosTransacciones(navegacion, dias = 30)
						scope.launch { sheetState.hide() }
					}
					
					// Espacio adicional al final para mejor UX
					MA_Spacer(Modifier.padding(8.dp))
				}
			}
		)
	}
}

fun modificarVistaDatosTransacciones(
	navegacion: (EventosNavegacion) -> Unit,
	fechaInicial: String =
		App.sharedPrerfences.get<String>(
			K.DIA,
			LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
		),
	dias: Int = App.sharedPrerfences.get<Int>(Preferencias.DIAS_CONSULTA_TRANSACCIONES, 1000),
) {

	if (fechaInicial.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
		App.sharedPrerfences.remove(K.DIA)
	} else {
		App.sharedPrerfences.put(K.DIA, fechaInicial)
	}

	val sql =
		"CREATE VIEW IF NOT EXISTS TRX_TIME AS SELECT   * " +
		" FROM  TRANSACCIONES T  " +
		" WHERE  date(CREATION_DATE)  >= DATE('$fechaInicial', '-$dias days')  AND date(CREATION_DATE) <= DATE('$fechaInicial');"

	App.log.d("DIAS $dias")
	App.log.d("FECHA INCIIAL  $fechaInicial")
	App.log.d(sql)

	val appDatabase = getKoin().get<AppDatabase>()
	val db: SupportSQLiteDatabase =
		appDatabase
			.openHelper
			.writableDatabase // Usamos readableDatabase para operaciones de lectura
	db.execSQL("DROP VIEW  IF EXISTS TRX_TIME ")
	db.execSQL(sql)
	App.sharedPrerfences.put(Preferencias.DIAS_CONSULTA_TRANSACCIONES, dias)

	navegacion(EventosNavegacion.HomeApp)
}
