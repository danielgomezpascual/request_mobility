package com.personal.metricas.notas.domain

import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.notas.domain.interactors.GuardarNotaCU
import com.personal.metricas.notas.domain.interactors.ObtenerNotasCU
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.mp.KoinPlatform

class NotasManager( val dialog: DialogManager = DialogManager()) {

	companion object{
		fun instancia(): NotasManager = getKoin().get<NotasManager>()
	}
	//val dialog = getKoin().get<DialogManager>()
	val scope = CoroutineScope(Dispatchers.IO)

	var notas: List<Notas> = emptyList()

	init {
		scope.launch { notas = dameNotas() }

	}
	suspend fun dameNotas(): List<Notas>{
		val notas = KoinPlatform.getKoin().get<ObtenerNotasCU>()
		val  lista = notas.getAll().first()
		return lista
	}


	suspend fun almacenarNota(nota : Notas){
		//actujalizamos la nota en el array que teneos
		notas = notas - notas.filter { it.hash.equals(nota.hash) }
		notas = notas.plus(nota)

		//almacenamos la nota en la base de datos room
		val notasCU = KoinPlatform.getKoin().get<GuardarNotaCU>()
		notasCU.guardar(nota)
	}






}