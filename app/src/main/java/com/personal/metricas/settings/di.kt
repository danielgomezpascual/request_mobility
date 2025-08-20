package com.personal.metricas.settings

import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.dashboards.data.ds.local.dao.DashboardDao
import com.personal.metricas.firebase.domain.FirebaseManager
import com.personal.metricas.firebase.domain.interactors.DescargarContenidoFirestore
import com.personal.metricas.firebase.domain.interactors.SubirContenidoLocalFirebase
import com.personal.metricas.kpi.data.ds.local.dao.KpisDao
import com.personal.metricas.notas.data.ds.local.dao.NotasDao
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.paneles.data.ds.local.dao.PanelesDao
import com.personal.metricas.settings.ui.SettingsScreen
import com.personal.metricas.settings.ui.SettingsViewModel
import com.personal.metricas.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM
import com.personal.metricas.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.metricas.transacciones.domain.interactors.GuardarTransacciones
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moduleSettings = module {

	single<DescargarContenidoFirestore> {
		DescargarContenidoFirestore(firebase = get<FirebaseManager>(),
									daoKpi = get<KpisDao>(),
									daoPanel = get<PanelesDao>(),
									daoDashboard = get<DashboardDao>(),
									daoNotas = get<NotasDao>())
	}


	single<SubirContenidoLocalFirebase> {
		SubirContenidoLocalFirebase(firebase = get<FirebaseManager>(),
									daoKpi = get<KpisDao>(),
									daoPanel = get<PanelesDao>(),
									daoDashboard = get<DashboardDao>(),
									daoNotas = get<NotasDao>())
	}
	//ViewModel
	viewModel {
		SettingsViewModel(
			descargarFirebase = get<DescargarContenidoFirestore>(),
			subirFirebase = get<SubirContenidoLocalFirebase>(),
			db = get<AppDatabase>(),

			dialog = get<DialogManager>(),


			)
	}

}