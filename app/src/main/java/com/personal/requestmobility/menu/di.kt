package com.personal.requestmobility.menu

import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.inicializador.domain.InicializadorManager
import com.personal.requestmobility.menu.screen.HerramientasViewModel
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