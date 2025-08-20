package com.personal.metricas.firebase.domain

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.personal.metricas.App
import kotlinx.coroutines.tasks.await

class FirebaseManager {

	fun incializar(ctx: Context) = FirebaseApp.initializeApp(ctx)


	fun getAuth() = FirebaseAuth.getInstance()
	val _firestore = Firebase.firestore

	fun dameIdentidicadorUsuario(): String = FirebaseAuth.getInstance().currentUser?.uid ?: "?"

	var batch: WriteBatch? = null

	fun inicio() {
		batch = _firestore.batch()
	}

	suspend fun finalizar() {
		batch?.commit()?.await()
		batch = null
	}

	fun guardarFirestore(coleccion: String, obj: Any) {
		if (batch == null) inicio()
		val documento = _firestore.collection(coleccion).document()
		batch?.set(documento, obj)


	}

	suspend  fun eliminarPorUsuario(coleccion: String, identificadorUsuario: String) {
		if (batch == null ) inicio()
		val snapshot = _firestore.collection(coleccion).whereEqualTo("identificadorUsuario", identificadorUsuario).get().await()
		if (!snapshot.isEmpty){
			snapshot.documents.forEach { batch?.delete(it.reference) }
		}
	}

	suspend inline fun <reified T : Any>  obtenerDatos(coleccion: String, identificadorUsuario: String): List<T>{
		val snapshot = _firestore.collection(coleccion).whereEqualTo("identificadorUsuario", identificadorUsuario).get().await()
		val listaDeObjetos: List<T> = snapshot.documents.map { document -> document.toObject<T>() as T }
		 return listaDeObjetos
	}
}