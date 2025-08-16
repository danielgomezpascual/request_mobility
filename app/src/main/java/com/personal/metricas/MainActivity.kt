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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.personal.metricas.core.composables.dialogos.AppGlobalDialogs
import com.personal.metricas.core.composables.dialogos.DialogManager
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


				runBlocking {
					//val f: SubirContenidoLocalFirebase = getKoin().get()
					//f.uploadFirestore()

					//val f: DescargarContenidoFirestore = getKoin().get()
					//f.descargar()
				}


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


				/*	AuthScreen(onSignInSuccess = {
						App.log.d("OK")

					},
							   onSignInError = {
								   App.log.d("Eerrror")
							   })*/

				val dialogManager: DialogManager = getKoin().get()
				/*	AppGlobalDialogs(dialogManager)*/


				/*NavegacionGuia()
			*/

			}

		}


	}

}