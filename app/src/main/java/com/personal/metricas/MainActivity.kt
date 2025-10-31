package com.personal.metricas


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.pm.ActivityInfo
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
	@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		setContent {
			RequestMobilityTheme {

				// 1. Calcula la clase de tamaño aquí
				App.windowSizeClass = calculateWindowSizeClass(this)


				// 1. Lee el valor booleano desde los recursos
				val allowSensorRotation = resources.getBoolean(R.bool.allow_sensor_rotation)

				// 2. Aplica la orientación a la Activity
				requestedOrientation = if (allowSensorRotation) {
					// En pantallas grandes: permite que el sensor decida (vertical u horizontal)
					ActivityInfo.SCREEN_ORIENTATION_SENSOR
				} else {
					// En pantallas pequeñas: fuerza siempre el modo vertical
					ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
				}

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






