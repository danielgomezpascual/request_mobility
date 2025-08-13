package com.personal.metricas.firebase

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class FirebaseManager {

	fun incializar(ctx: Context) =	FirebaseApp.initializeApp(ctx)

	fun getAuth() = FirebaseAuth.getInstance()



}