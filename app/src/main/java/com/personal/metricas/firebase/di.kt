package com.personal.metricas.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.personal.metricas.firebase.crashlytics.Crash
import org.koin.dsl.module

val modulesFirebase = module {
	single<FirebaseCrashlytics> { FirebaseCrashlytics.getInstance() }

	single<Crash> { Crash(get<FirebaseCrashlytics>()) }
}