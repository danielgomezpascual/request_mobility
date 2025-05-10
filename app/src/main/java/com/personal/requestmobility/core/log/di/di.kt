package com.personal.requestmobility.core.log.di

import com.personal.requestmobility.core.log.domain.ConfiguracionMyLog
import com.personal.requestmobility.core.log.ui.LogcatOutput
import com.personal.requestmobility.core.log.domain.IOutputLog
import com.personal.requestmobility.core.log.domain.MyLog
import org.koin.dsl.module

val moduloLog = module {
    single { ConfiguracionMyLog() }
    single<IOutputLog> { LogcatOutput() }
    single { MyLog(get(), get()) }
}