package com.personal.metricas.core.di

import com.personal.metricas.core.domain.usecases.EnlaceMobilityCU
import com.personal.metricas.core.domain.usecases.ObtenerDatosTransaccionCU
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.transacciones.data.ds.local.TransaccionesLocalDS
import com.personal.metricas.transacciones.data.ds.local.dao.TansaccionesDao
import com.personal.metricas.transacciones.domain.repositorios.IRepoTransacciones
import org.koin.dsl.module

val moduloCore = module {
    // single<IRepoTransacciones> {}

    // Database
    single<TansaccionesDao> { get<AppDatabase>().transaccionesDao() }
    single<IRepoTransacciones> { TransaccionesLocalDS(get<TansaccionesDao>()) }

    factory { EnlaceMobilityCU(get()) }
    factory { ObtenerDatosTransaccionCU(get()) }
}
