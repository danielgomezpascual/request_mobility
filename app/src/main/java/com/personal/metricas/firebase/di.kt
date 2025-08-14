package com.personal.metricas.firebase

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.personal.metricas.dashboards.data.ds.local.dao.DashboardDao
import com.personal.metricas.firebase.crashlytics.Crash
import com.personal.metricas.firebase.domain.FirebaseManager
import com.personal.metricas.firebase.domain.interactors.DescargarContenidoFirestore
import com.personal.metricas.firebase.domain.interactors.SubirContenidoLocalFirebase
import com.personal.metricas.kpi.data.ds.local.dao.KpisDao
import com.personal.metricas.notas.data.ds.local.dao.NotasDao
import com.personal.metricas.paneles.data.ds.local.dao.PanelesDao
import org.koin.dsl.module

val modulesFirebase = module {
	single<FirebaseCrashlytics> { FirebaseCrashlytics.getInstance() }

	single<Crash> { Crash(get<FirebaseCrashlytics>()) }

	single<FirebaseManager> { FirebaseManager() }

	single<SubirContenidoLocalFirebase> {
		SubirContenidoLocalFirebase(
			firebase = get<FirebaseManager>(),
			daoKpi = get<KpisDao>(),
			daoPanel = get<PanelesDao>(),
			daoDashboard = get<DashboardDao>(),
			daoNotas =  get<NotasDao>()
		)
	}

	single<DescargarContenidoFirestore> {
		DescargarContenidoFirestore(
			firebase = get<FirebaseManager>(),
			daoKpi = get<KpisDao>(),
			daoPanel = get<PanelesDao>(),
			daoDashboard = get<DashboardDao>(),
			daoNotas =  get<NotasDao>()
		)
	}
}