package com.personal.metricas.kpi.data.ds.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.metricas.core.room.IRoom
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils.Utils
import com.personal.metricas.core.utils.Utils.esTrue
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.core.utils._toObjectFromJson
import com.personal.metricas.kpi.domain.entidades.Kpi


@Entity(tableName = "Kpis")
data class KpisRoom(
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0,
	val titulo: String = "",
	val descripcion: String = "",
	val origen: String = "",
	val sql: String = "",
	val parametros: String = "",
	//  val configuracion: String = ""
	val autogenerado: String = "N",
) : IRoom


fun KpisRoom.toKpi(): Kpi {

	//val configuracion = _toObjectFromJson<PanelConfiguracion>(this.configuracion)?: PanelConfiguracion()

	//App.log.d(configuracion.orientacion)
	//configuracion.orientacion = if3(configuracion.orientacion.equals("HORIZONTAL"), PanelOrientacion.HORIZONTAL, PanelOrientacion.VERTICAL)

	return Kpi(
		id = this.id,
		titulo = this.titulo,
		descripcion = this.descripcion,
		sql = this.sql,
		parametros = _toObjectFromJson<Parametros>(parametros) ?: Parametros(),
		autogenerado = esTrue(this.autogenerado, "Y", false)

		/*configuracion = configuracion*/)
}


fun KpisRoom.fromKPI(kpi: Kpi) = KpisRoom(
	id = kpi.id,
	titulo = kpi.titulo,
	descripcion = kpi.descripcion,
	sql = kpi.sql,
	parametros = _toJson(kpi.parametros),
	/*configuracion = _toJson(kpi.configuracion)*/
	autogenerado = Utils.toSiNo(kpi.autogenerado)
)

