package com.personal.requestmobility.sincronizacion

import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.requestmobility.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM
import com.personal.requestmobility.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.requestmobility.transacciones.domain.interactors.GuardarTransacciones
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val moduloSincronizacion = module {

    //ViewModel
    viewModel {
        ListaOrganizacionesSincronizarVM(
            get<ObtenerOrganizacionesCU>(),
            get<TransaccionesRepoImp>(),
            get<GuardarTransacciones>(),
            get<DialogManager>()
        )
    }

}
