package com.personal.requestmobility.sincronizacion

import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.requestmobility.endpoints.data.ds.remote.EndPointsRemotoDS
import com.personal.requestmobility.endpoints.data.ds.remote.servicio.EndPointRemotos
import com.personal.requestmobility.endpoints.domain.ObtenerDatosEndPoint
import com.personal.requestmobility.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM
import com.personal.requestmobility.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.requestmobility.transacciones.domain.interactors.GuardarTransacciones
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


val moduloSincronizacion = module {


    //



    //Retrofit
    single<EndPointRemotos>{ RetrofitServicioEndPointRemotos(get()) }
    single<EndPointsRemotoDS>{ EndPointsRemotoDS(get<EndPointRemotos>()) }


    //CU
    single<ObtenerDatosEndPoint> { ObtenerDatosEndPoint(get<EndPointsRemotoDS>()) }



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


fun RetrofitServicioEndPointRemotos(retrofit: Retrofit): EndPointRemotos {
    return retrofit.create(EndPointRemotos::class.java)
}