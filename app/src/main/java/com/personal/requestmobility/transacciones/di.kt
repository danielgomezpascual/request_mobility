package com.personal.requestmobility.transacciones

import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.transacciones.data.local.dao.TansaccionesDao
import com.personal.requestmobility.transacciones.data.repositorios.TransaccionesRepositorio
import com.personal.requestmobility.transacciones.domain.graficas.GraficaDistribucionErrores
import com.personal.requestmobility.transacciones.domain.graficas.GraficaErroresPorTipo
import com.personal.requestmobility.transacciones.domain.interactors.AplicarFiltrosTransaccionesCU
import com.personal.requestmobility.transacciones.domain.graficas.GraficaTransaccionPorTipoCU
import com.personal.requestmobility.transacciones.domain.interactors.ObtenerFiltrosTransaccionesCU
import com.personal.requestmobility.transacciones.domain.interactors.ObtenerTransaccionesCU
import com.personal.requestmobility.transacciones.domain.graficas.GraficaTransaccionesEstadoCU
import com.personal.requestmobility.transacciones.domain.graficas.ResumenTrx
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKPIsCU_NO
import com.personal.requestmobility.transacciones.domain.repositorios.IRepoTransacciones
import com.personal.requestmobility.transacciones.ui.screens.listado.DockTransaccionesVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val moduloTransacciones = module {

    // Database
    single<TansaccionesDao> { get<AppDatabase>().transaccionesDao() }

    //Repo
    single<IRepoTransacciones> { TransaccionesRepositorio(get<TansaccionesDao>()) }

    //CU

    single<ObtenerKPIsCU_NO> { ObtenerKPIsCU_NO(get<IRepoTransacciones>()) }

    single<ObtenerTransaccionesCU> { ObtenerTransaccionesCU(get<IRepoTransacciones>()) }
    single<ObtenerFiltrosTransaccionesCU> { ObtenerFiltrosTransaccionesCU() }
    single<ResumenTrx> { ResumenTrx() }
    single<GraficaTransaccionPorTipoCU> { GraficaTransaccionPorTipoCU() }
    single<GraficaTransaccionesEstadoCU> { GraficaTransaccionesEstadoCU() }
    single<GraficaDistribucionErrores> { GraficaDistribucionErrores() }
    single<GraficaErroresPorTipo> { GraficaErroresPorTipo() }
    single<AplicarFiltrosTransaccionesCU> { AplicarFiltrosTransaccionesCU(get<ObtenerFiltrosTransaccionesCU>()) }

    //UI - ViewModel
    viewModel {
        DockTransaccionesVM(
            get<AplicarFiltrosTransaccionesCU>(),
            get<ObtenerTransaccionesCU>(),
            get<ObtenerKPIsCU_NO>(),

            get<ResumenTrx>(),
            get<GraficaTransaccionPorTipoCU>(),
            get<GraficaTransaccionesEstadoCU>(),
            get<GraficaDistribucionErrores>(),
            get<GraficaErroresPorTipo>()
        )
    }
}
