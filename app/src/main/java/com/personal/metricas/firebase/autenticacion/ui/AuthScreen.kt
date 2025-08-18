package com.personal.metricas.firebase.autenticacion.ui

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.personal.metricas.App
import com.personal.metricas.R


@Composable
fun AuthScreen(
	onSignInSuccess: () -> Unit,
	onSignInError: (String?) -> Unit,

	) {

	val providers = arrayListOf(
		AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true).build(),
		AuthUI.IdpConfig.AnonymousBuilder().build(),
		AuthUI.IdpConfig.GoogleBuilder().build())

	// Creamos el Intent para el flujo de inicio de sesión
	val signInIntent = AuthUI.getInstance()
		.createSignInIntentBuilder()
		.setLogo(R.drawable.logo)
		.setTheme(R.style.Theme_Metricas)
		.setLockOrientation(true)
		.setAvailableProviders(providers)
		.setAlwaysShowSignInMethodScreen(true)
		.setCredentialManagerEnabled(false)
		.build()


	val signInLauncher = rememberLauncherForActivityResult(
		contract = FirebaseAuthUIActivityResultContract(),
	) { result ->
		handleSignInResult(result, onSignInSuccess, onSignInError)
	}


	LaunchedEffect(Unit) {
		signInLauncher.launch(signInIntent)
	}

}

/**
 * Maneja el resultado devuelto por el flujo de Firebase AuthUI.
 */
private fun handleSignInResult(
	result: FirebaseAuthUIAuthenticationResult,
	onSignInSuccess: () -> Unit,
	onSignInError: (String?) -> Unit,
) {
	val response = result.idpResponse
	if (result.resultCode == android.app.Activity.RESULT_OK) {
		// ¡Éxito! El usuario ha iniciado sesión correctamente.
		App.log.d("Sign-in successful! User: ${FirebaseAuth.getInstance().currentUser?.displayName}")
		App.log.d("Sign-in ID! User: ${FirebaseAuth.getInstance().currentUser?.uid}")
		onSignInSuccess()
	} else {
		// Fallo en el inicio de sesión.
		val errorMessage = response?.error?.message ?: "Unknown error"
		App.log.d("Sign-in failed: $errorMessage")
		onSignInError(errorMessage)
	}
}