package com.personal.requestmobility.sincronizacion

import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.requestmobility.paneles.domain.interactors.EliminarPanelCU
import com.personal.requestmobility.paneles.domain.repositorios.PanelesRepositorio
import com.personal.requestmobility.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM
import com.personal.requestmobility.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.requestmobility.transacciones.domain.interactors.GuardarTransacciones
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val moduloSincronizacion = module {






    //ViewModel
    viewModel {
        ListaOrganizacionesSincronizarVM(
            obtenerOrganizacion = get<ObtenerOrganizacionesCU>(),
            repoTrx = get<TransaccionesRepoImp>(),
            guardar = get<GuardarTransacciones>(),

            dialog = get<DialogManager>()

        )
    }

}
