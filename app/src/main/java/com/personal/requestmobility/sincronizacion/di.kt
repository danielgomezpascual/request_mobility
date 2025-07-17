package com.personal.requestmobility.sincronizacion

import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.requestmobility.kpi.domain.interactors.GuardarKpiCU
import com.personal.requestmobility.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.requestmobility.paneles.domain.interactors.EliminarPanelCU
import com.personal.requestmobility.paneles.domain.interactors.GuardarPanelCU
import com.personal.requestmobility.paneles.domain.repositorios.PanelesRepositorio
import com.personal.requestmobility.sincronizacion.data.ds.remote.EndPointsRemotoDS
import com.personal.requestmobility.sincronizacion.data.ds.remote.servicio.EndPointRemotos
import com.personal.requestmobility.sincronizacion.domain.interactors.ObtenerDatosEndPoint
import com.personal.requestmobility.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM
import com.personal.requestmobility.transacciones.RetrofitServicioTransacciones
import com.personal.requestmobility.transacciones.data.ds.remote.TransaccionesRemotoDS
import com.personal.requestmobility.transacciones.data.ds.remote.servicio.TransaccionesApiRemoto
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