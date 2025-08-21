package com.personal.metricas


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.himanshoe.charty.bar.LineBarChart
import com.himanshoe.charty.bar.SignalProgressBarChart
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.ChartColor
import com.himanshoe.charty.common.TargetConfig
import com.himanshoe.charty.common.asSolidChartColor
import com.personal.metricas.core.composables.dialogos.AppGlobalDialogs
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.composables.edittext.MA_TextoNormal
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.labels.MA_Titulo2
import com.personal.metricas.core.navegacion.NavegacionGuia
import com.personal.metricas.core.notificaciones.NotificacionesManager
import com.personal.metricas.firebase.autenticacion.ui.AuthScreen
import com.personal.metricas.firebase.domain.FirebaseManager
import com.personal.metricas.ui.theme.RequestMobilityTheme
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {
	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		setContent {
			RequestMobilityTheme {


				//runBlocking {
				//val f: SubirContenidoLocalFirebase = getKoin().get()
				//f.uploadFirestore()

				//val f: DescargarContenidoFirestore = getKoin().get()
				//f.descargar()
				//	}


				/*Column() {
					MA_BotonSecundario(texto = "Refitros", onClick = {

						App.crash.log("Mensjae de preuias")
						App.crash.setCustomKey("valor", "45646")
						App.crash.setCustomKey("valor 2", "---")
						//	throw RuntimeException("Test Crash")
					})


					MA_BotonSecundario(texto = "Metemos log y custom keys", onClick = {


						val sts: Array<StackTraceElement>? = Thread.currentThread().stackTrace
						val st: StackTraceElement = sts!!.get(4)


						val nombreClase = st.className.split(".").last()
						val informacion: String = "[${Thread.currentThread().name}] ${nombreClase} > ${st.methodName}:${st.lineNumber.toString()}"

						App.crash.log(" Ahora hago clic en el boton que lanza la excepcion")
						App.crash.setCustomKey("Info", informacion)

					})
					MA_BotonSecundario(texto = "Lanzamos exp -> Guardamos el crash", onClick = {
						throw RuntimeException("Test Crash")

					})
				}*/

//-------------------------------------------------------------------------------------------
				/*	Column(
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally,
					/*modifier = Modifier.background(color = Color(10, 10, 10, 255))*/
				) {

					MA_Titulo2("PRuebas")



					Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
						.width(100.dp)
						.height(350.dp)

						.padding(10.dp)
					) {

						val valor = 800f
						val maximo = 1000f
						val texto = "Pruebnas"
						val color = Color.Red

						Text(
							text = valor.toInt().toString(),
							modifier = Modifier.padding(6.dp),
							color = color,
							fontWeight = FontWeight(800),
							fontSize = TextUnit(25.0f, TextUnitType.Sp),
							textAlign = TextAlign.Center
						)

						SignalProgressBarChart(
							modifier = Modifier
								.weight(1f)
								.fillMaxSize(),
							progress = { ((valor * 100f) / maximo) },
							//progress = { 500f},
							maxProgress = 100f,
							totalBlocks = 10,
							progressColor = ChartColor.Gradient(
								listOf(
									Color(0xFFE8C900), Color(0xFFBA1515)
								)
							),
							trackColor = Color.Gray.asSolidChartColor(),
							gapRatio = 0.15f
						)

						Text(
							text = texto,
							modifier = Modifier.padding(6.dp),
							color = color,
							fontWeight = FontWeight(800),
							fontSize = TextUnit(14.0f, TextUnitType.Sp),
							textAlign = TextAlign.Center
						)


					}



					Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
						.fillMaxWidth()
						.height(350.dp)

						.padding(10.dp)
					) {

						val valor = 800f
						val maximo = 1000f
						val texto = "Pruebnas"
						val color = Color.Red

						Text(
							text = texto,
							modifier = Modifier.weight(1f).padding(6.dp),
							color = color,
							fontWeight = FontWeight(800),
							fontSize = TextUnit(14.0f, TextUnitType.Sp),
							textAlign = TextAlign.Center
						)


						SignalProgressBarChart(
							modifier = Modifier
								.rotate(90f)
								.height(120.dp)
								.width(40.dp),

							progress = { ((valor * 100f) / maximo) },
							//progress = { 500f},
							maxProgress = 100f,
							totalBlocks = 10,
							progressColor = ChartColor.Gradient(
								listOf(
									Color(0xFFE8C900), Color(0xFFBA1515)
								)
							),
							trackColor = Color.Gray.asSolidChartColor(),
							gapRatio = 0.15f
						)
						Text(
							text = valor.toInt().toString(),
							modifier = Modifier.weight(1f).padding(6.dp),
							color = color,
							fontWeight = FontWeight(800),
							fontSize = TextUnit(25.0f, TextUnitType.Sp),
							textAlign = TextAlign.Center
						)


					}
				}


			*/


				val auth = FirebaseManager().getAuth()
				App.log.d(auth.currentUser?.displayName)
				var isAuthenticated by remember { mutableStateOf(auth.currentUser != null) }

				if (isAuthenticated) {
					// Si está autenticado, muestra la pantalla principal
					NavegacionGuia()
					val dialogManager: DialogManager = getKoin().get()
					AppGlobalDialogs(dialogManager)
				} else {
					// Si no, muestra la pantalla de autenticación
					AuthScreen(
						onSignInSuccess = { isAuthenticated = true },
						onSignInError = { error -> App.log.d("Eerrror") }
					)
				}


			}

		}
	}
	}






