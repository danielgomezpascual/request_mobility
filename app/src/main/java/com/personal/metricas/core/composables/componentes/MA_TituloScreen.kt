package com.personal.metricas.core.composables.componentes

import MA_IconBottom
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.sqlite.db.SupportSQLiteDatabase
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.personal.metricas.App
import com.personal.metricas.App.Companion.ENTORNO
import com.personal.metricas.R
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.botones.MA_BotonPrincipal
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.dialogos.AppGlobalDialogs
import com.personal.metricas.core.composables.dialogos.DatePickerButton
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.imagenes.MA_ImagenDrawable
import com.personal.metricas.core.composables.imagenes.MA_ImagenCirculoURL
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.composables.modales.MA_BottomSheet
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.core.utils.TiempoHora
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.ui.composables.MA_EtiquetaItem
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.firebase.domain.FirebaseManager
import com.personal.metricas.menu.Features
import com.personal.metricas.paneles.domain.entidades.PanelData
import com.personal.metricas.paneles.ui.componente.MA_Panel
import com.personal.metricas.paneles.ui.entidades.toPanel
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cabecera(cabecera: TituloScreen, navegacion: (EventosNavegacion) -> Unit, acciones: @Composable () -> Unit = {}) {

	val context = LocalContext.current
	val currentUser = FirebaseAuth.getInstance().currentUser

	val appDatabase = getKoin().get<AppDatabase>()
	val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase // Usamos readableDatabase para operaciones de lectura


	MA_Card(elevacion = 3.dp, modifier = Modifier.padding(0.dp),
			redondeo = 0.dp, paddingCard = 0.dp
	) {


		val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
		val scope = rememberCoroutineScope() // Se mantiene dentro del componente


		val m = Modifier.fillMaxWidth()
		Column(modifier = m
			.padding(start = 3.dp, top = 15.dp)
			.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {


			Row(
				modifier = m.padding(3.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				//MA_ImagenDrawable(imagen = R.drawable.logo, s = 24.dp)
				val auth = FirebaseManager().getAuth()

				Box(modifier = Modifier.clickable(enabled = true, onClick = {
					navegacion(EventosNavegacion.Settings)
				})) {
					if (auth.currentUser == null || auth.currentUser?.photoUrl == null) {
						MA_ImagenDrawable(imagen = R.drawable.logo, s = 24.dp)
					} else {
						MA_ImagenCirculoURL(url = auth.currentUser?.photoUrl.toString())
					}
				}
				//MA_Spacer(Modifier.padding(3.dp))
				//*val selectedDate = remember { mutableStateOf("") }


				Column(horizontalAlignment = Alignment.CenterHorizontally) {

					DatePickerButton(
						onDateSelected = { date ->
							//selectedDate.value = date
							val dia = date.split("-").last()
							val f_start: String = date.toString()
							/*db.execSQL(""" DROP VIEW  IF EXISTS TRX_7 """)
							db.execSQL(""" CREATE VIEW  TRX_7 AS SELECT * FROM TRANSACCIONES T INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE WHERE DATE(CREATION_DATE) >= DATE('$f_start', '-7 days')""")
							db.execSQL(""" DROP VIEW  IF EXISTS TRX_HOY """)
							db.execSQL(""" CREATE VIEW IF NOT EXISTS TRX_HOY AS SELECT * FROM  TRANSACCIONES T INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE WHERE  date(CREATION_DATE)  = date('$f_start', 'localtime');""")
							App.sharedPrerfences.put(K.DIA, dia)
							navegacion(EventosNavegacion.HomeApp)*/

							modificarVistaDatosTransacciones(navegacion, fechaInicial = f_start )

						}
					)
					val MAX_DIAS_CONSULTA = 1000
					var diasConsulta : String = App.sharedPrerfences.get<Int>(Preferencias.DIAS_CONSULTA_TRANSACCIONES, MAX_DIAS_CONSULTA).toString()
					diasConsulta = if3 (diasConsulta == MAX_DIAS_CONSULTA.toString(),  "Completo", "$diasConsulta días")


					MA_LabelMini(modifier = Modifier
						.clickable(enabled = true, onClick = {
							scope.launch { sheetState.show() }
						})
						.padding(horizontal = 6.dp),
								 valor = "${diasConsulta}",
								 color = Color(50, 50, 50))
				}


				//MA_Spacer(Modifier.padding(3.dp))
				Column(
					/*verticalAlignment = Alignment.CenterVertically,*/
					modifier = m
						.fillMaxWidth()
						.weight(1f),
				) {
					MA_Titulo(
						modifier = Modifier.padding(horizontal = 1.dp, vertical = 1.dp),
						alineacion = TextAlign.Start,
						valor = cabecera.titulo)
					val color = if3(App.sharedPrerfences.get<Boolean>(Preferencias.ENTORNO_PRO, false), Color(156, 27, 27, 255), Color(32, 77, 210, 255))
					Row() {
						MA_LabelMini(modifier = Modifier
							.padding(horizontal = 1.dp), valor = App.ENTORNO, color = color)
					}

				}

				MA_BottomSheet(sheetState, onClose = {
					{ scope.launch { sheetState.hide() } }
				}, contenido = {
					Column {
						MA_BotonPrincipal("Cerrar") { scope.launch { sheetState.hide() } }

						MA_FiltroFecha(Etiquetas("Completo")) {

							modificarVistaDatosTransacciones(navegacion, dias=1000)

						}
						MA_FiltroFecha(Etiquetas("1 días")) {
							modificarVistaDatosTransacciones(navegacion, dias=1)

						}

						MA_FiltroFecha(Etiquetas("3 días")) {
							modificarVistaDatosTransacciones(navegacion, dias=3)

						}
						MA_FiltroFecha(Etiquetas("5 días")) {
							modificarVistaDatosTransacciones(navegacion, dias=5)

						}

						MA_FiltroFecha(Etiquetas("7 días")) {
							modificarVistaDatosTransacciones(navegacion, dias=7)

						}

						MA_FiltroFecha(Etiquetas("15 días")) {
							modificarVistaDatosTransacciones(navegacion, dias=15)

						}
						MA_FiltroFecha(Etiquetas("30 días")) {
							modificarVistaDatosTransacciones(navegacion, dias=  30)

						}

					}
				})

				Row(
					modifier = m
						.weight(1f),
					horizontalArrangement = Arrangement.End,
					verticalAlignment = Alignment.Top

				) {

					Box(modifier = m.weight(1f), contentAlignment = Alignment.CenterEnd) {

						acciones()
					}
					/*Box(modifier = m.weight(1f), contentAlignment = Alignment.CenterEnd) { MA_IconBottom(
							//   modifier = Modifier.weight(1f),
							icon = Features.CerrarSesion().icono,
							labelText = "Setting",
							seleccionado = false,
							destacado = false,
							onClick = {


								AuthUI.getInstance()
									.signOut(context)
									.addOnCompleteListener {
										navegacion(EventosNavegacion.MenuApp)
									}

							}
						)
					}*/
				}
			}


			//HorizontalDivider(thickness = 1.dp)
		}


	}

}

fun modificarVistaDatosTransacciones(
	navegacion: (EventosNavegacion) -> Unit,
	fechaInicial: String = App.sharedPrerfences.get<String>(K.DIA, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
	dias: Int = App.sharedPrerfences.get<Int>(Preferencias.DIAS_CONSULTA_TRANSACCIONES, 1000),
) {

	if (fechaInicial.equals( LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
		App.sharedPrerfences.remove(K.DIA)
	}else{
		App.sharedPrerfences.put(K.DIA, fechaInicial)
	}

	val sql = "CREATE VIEW IF NOT EXISTS TRX_TIME AS SELECT   * " +
			  " FROM  TRANSACCIONES T  " +
			  " WHERE  date(CREATION_DATE)  >= DATE('$fechaInicial', '-$dias days')  AND date(CREATION_DATE) <= DATE('$fechaInicial');"

	App.log.d("DIAS $dias")
	App.log.d("FECHA INCIIAL  $fechaInicial")
	App.log.d(sql)

	val appDatabase = getKoin().get<AppDatabase>()
	val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase // Usamos readableDatabase para operaciones de lectura
	db.execSQL("DROP VIEW  IF EXISTS TRX_TIME ")
	db.execSQL(sql)
	App.sharedPrerfences.put(Preferencias.DIAS_CONSULTA_TRANSACCIONES, dias)


	navegacion(EventosNavegacion.HomeApp)
}