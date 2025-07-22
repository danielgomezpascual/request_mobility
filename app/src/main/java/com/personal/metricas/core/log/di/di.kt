package com.personal.metricas.core.log.di

import com.personal.metricas.core.log.domain.ConfiguracionMyLog
import com.personal.metricas.core.log.ui.LogcatOutput
import com.personal.metricas.core.log.domain.IOutputLog
import com.personal.metricas.core.log.domain.MyLog
import org.koin.dsl.module

val moduloLog = module {
    single { ConfiguracionMyLog() }
    single<IOutputLog> { LogcatOutput() }
    single { MyLog(get(), get()) }
}