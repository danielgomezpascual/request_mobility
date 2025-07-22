package com.personal.metricas.transacciones

import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.transacciones.data.ds.local.dao.TansaccionesDao
import com.personal.metricas.transacciones.data.ds.local.TransaccionesLocalDS
import com.personal.metricas.transacciones.domain.graficas.GraficaDistribucionErrores
import com.personal.metricas.transacciones.domain.graficas.GraficaErroresPorTipo
import com.personal.metricas.transacciones.domain.interactors.AplicarFiltrosTransaccionesCU
import com.personal.metricas.transacciones.domain.graficas.GraficaTransaccionPorTipoCU
import com.personal.metricas.transacciones.domain.interactors.ObtenerFiltrosTransaccionesCU
import com.personal.metricas.transacciones.domain.interactors.ObtenerTransaccionesCU
import com.personal.metricas.transacciones.domain.graficas.GraficaTransaccionesEstadoCU
import com.personal.metricas.transacciones.domain.graficas.ResumenTrx
import com.personal.metricas.kpi.domain.interactors.ObtenerKpisCU
import com.personal.metricas.transacciones.data.ds.remote.TransaccionesRemotoDS
import com.personal.metricas.transacciones.data.ds.remote.servicio.TransaccionesApiRemoto
import com.personal.metricas.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.metricas.transacciones.domain.interactors.GuardarTransacciones
import com.personal.metricas.transacciones.domain.repositorios.IRepoTransacciones
import com.personal.metricas.transacciones.ui.screens.listado.DockTransaccionesVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


val moduloTransacciones = module {

    // Database
    single<TansaccionesDao> { get<AppDatabase>().transaccionesDao() }
    single<TransaccionesLocalDS> { TransaccionesLocalDS(get<TansaccionesDao>()) }

    //Retrofit
    single<TransaccionesApiRemoto>{ RetrofitServicioTransacciones(get()) }
    single<TransaccionesRemotoDS>{ TransaccionesRemotoDS(get<TransaccionesApiRemoto>()) }

    //Repo
    single<TransaccionesRepoImp> {
        TransaccionesRepoImp(listOf(
            get<TransaccionesLocalDS>(),
            get<TransaccionesRemotoDS>())
        )
    }

    //CU
    single<ObtenerTransaccionesCU> { ObtenerTransaccionesCU(get<IRepoTransacciones>()) }
    single<ObtenerFiltrosTransaccionesCU> { ObtenerFiltrosTransaccionesCU() }
    single<ResumenTrx> { ResumenTrx() }
    single<GraficaTransaccionPorTipoCU> { GraficaTransaccionPorTipoCU() }
    single<GraficaTransaccionesEstadoCU> { GraficaTransaccionesEstadoCU() }
    single<GraficaDistribucionErrores> { GraficaDistribucionErrores() }
    single<GraficaErroresPorTipo> { GraficaErroresPorTipo() }
    single<AplicarFiltrosTransaccionesCU> { AplicarFiltrosTransaccionesCU(get<ObtenerFiltrosTransaccionesCU>()) }
    single<GuardarTransacciones> { GuardarTransacciones(get<TransaccionesRepoImp>()) }

    //UI - ViewModel
    viewModel {
        DockTransaccionesVM(
            get<ObtenerKpisCU>() //definido en el modulo de Kpis
        )
    }
}


fun RetrofitServicioTransacciones(retrofit: Retrofit): TransaccionesApiRemoto {
    return retrofit.create(TransaccionesApiRemoto::class.java)
}