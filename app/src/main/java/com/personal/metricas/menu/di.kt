package com.personal.metricas.menu

import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.inicializador.domain.InicializadorManager
import com.personal.metricas.menu.screen.HerramientasViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modulosMenu = module {

	viewModel {
		HerramientasViewModel(
			inicalizadorManager = get<InicializadorManager>(),
			dialog = get<DialogManager>()
		)
	}
}