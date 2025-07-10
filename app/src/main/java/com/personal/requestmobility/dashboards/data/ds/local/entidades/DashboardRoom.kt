package com.personal.requestmobility.dashboards.data.ds.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.dialogos.AppGlobalDialogs
import com.personal.requestmobility.core.room.IRoom
import com.personal.requestmobility.core.utils.Utils.esTrue
import com.personal.requestmobility.core.utils.Utils.toSiNo
import com.personal.requestmobility.core.utils._toJson
import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.entidades.TipoDashboard
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpiCU
import com.personal.requestmobility.paneles.data.ds.local.entidades.PanelesRoom
import com.personal.requestmobility.paneles.data.ds.local.entidades.fromPanel
import com.personal.requestmobility.paneles.domain.entidades.Panel
import com.personal.requestmobility.paneles.domain.interactors.ObtenerPanelCU
import com.personal.requestmobility.paneles.domain.interactors.ObtenerPanelesCU
import kotlinx.coroutines.flow.first
import org.koin.java.KoinJavaComponent.getKoin

@Entity(tableName = "Dashboard")
class DashboardRoom(
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0,
	val tipo: Int = 0,
	val nombre: String = "",
	val home: String = "N",
	val logo: String = "",
	val descripcion: String = "",
	val idKpi: Int = 0,
	val paneles: String = "",
				   ) : IRoom

suspend fun DashboardRoom.toDashboard(): Dashboard {
	
	
	val obtenerPaneles: ObtenerPanelesCU = getKoin().get()
	val obtenerKpi: ObtenerKpiCU = getKoin().get()
	
	val type = object : TypeToken<List<PanelDashboardRoom>>() {}.type
	val panelesDashboard: List<PanelDashboardRoom> = Gson().fromJson<List<PanelDashboardRoom>>(this.paneles, type)
	
	App.log.d(paneles)
	App.log.d(panelesDashboard)
	
	val todosPaneles = obtenerPaneles.getAll().first()
	// 2. Procesamos la lista de forma funcional.
	// Usamos 'map' para transformar cada 'panel' de la lista original en uno nuevo.
	// Esto es más limpio y seguro que crear una 'var' e ir añadiendo elementos.
	val listaPanelesActualizado = todosPaneles.map { panel ->
		val panelDashboardRoom: PanelDashboardRoom? = panelesDashboard.firstOrNull { it.idPanel == panel.id }
		
		// Usamos 'let' para un manejo más elegante de los nulos
		panelDashboardRoom?.let { dashboardRoom ->
			panel.copy(seleccionado = dashboardRoom.seleccionado, orden = dashboardRoom.orden)
		} ?: panel // El operador Elvis (?:) devuelve el panel original si no se encuentra
	}.sortedBy { it.orden } // Finalmente, ordenamos la lista resultante.
	
	//App.log.lista("Paneles", listaPanelesActualizado)
	
	// 3. Ahora que tenemos todos los datos, construimos y devolvemos el objeto.
	// Todo el bloque de la función se ha ejecutado de forma secuencial y legible,
	// pero de manera no bloqueante gracias a 'suspend'.
	
	return Dashboard(
			id = this.id,
			tipo = TipoDashboard.fromId(this.tipo),
			nombre = this.nombre,
			home = esTrue(valor = this.home.toString()),
			logo = this.logo,
			descripcion = this.descripcion,
			kpi = obtenerKpi.obtener(this.idKpi), // Asumo que esta también es una función suspend
			paneles = listaPanelesActualizado
					)

	
	

	
	/*return Dashboard(
			id = this.id,
			tipo = TipoDashboard.fromId(this.tipo),
			nombre = this.nombre,
			home = esTrue(valor = this.home.toString()),
			logo = this.logo,
			descripcion = this.descripcion,
			kpi = obtenerKpi.obtener(this.idKpi),
			paneles = listaPanelesActualizado
					)*/
}

fun DashboardRoom.fromDashboard(dashboard: Dashboard): DashboardRoom {
	
	
	val idTipo = when (dashboard.tipo) {
		is TipoDashboard.Dinamico -> dashboard.tipo.id
		is TipoDashboard.Estatico -> dashboard.tipo.id
	}
	
	
	
	return DashboardRoom(
			id = dashboard.id, // Room maneja la autogeneración si id es 0 en la inserción.
			tipo = idTipo,
			nombre = dashboard.nombre,
			home = toSiNo(dashboard.home),
			logo = dashboard.logo,
			descripcion = dashboard.descripcion,
			idKpi = dashboard.kpi.id,
			paneles = _toJson(dashboard.paneles.map { PanelDashboardRoom.fromPanel(it) })
						)
}
