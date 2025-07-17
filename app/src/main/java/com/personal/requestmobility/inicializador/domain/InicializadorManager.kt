package com.personal.requestmobility.inicializador.domain

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.core.utils.Parametro
import com.personal.requestmobility.core.utils.Parametros
import com.personal.requestmobility.dashboards.domain.entidades.TipoDashboard
import com.personal.requestmobility.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.toDashboard
import com.personal.requestmobility.kpi.domain.interactors.GuardarKpiCU
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.paneles.domain.entidades.Panel
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion
import com.personal.requestmobility.paneles.domain.entidades.PanelTipoGrafica
import com.personal.requestmobility.paneles.domain.interactors.GuardarPanelCU
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

class InicializadorManager(
	private val operaciones: InicializadorOperaciones,
	private val dialog: DialogManager,
) {


	suspend fun eliminarDatosGeneradosPreviamente() {

		val trxDao = getKoin().get<AppDatabase>().transaccionesDao()
		val appDatabase = getKoin().get<AppDatabase>()

		val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase // Usamos readableDatabase para operaciones de lectura
		db.execSQL("DELETE FROM DASHBOARD")
		db.execSQL("DELETE FROM PANELES")
		db.execSQL("DELETE FROM KPIS")

	}

	suspend fun start() {


		eliminarDatosGeneradosPreviamente()

		val kpiOrganizaciones = (KpiUI(
			titulo = "Organizaciones",
			descripcion = "Organizaciones en el sistema",
			origen = "",
			sql = "SELECT DISTINCT  ORGANIZATION_CODE, ORGANIZATION_ID, ORGANIZATION_NAME, MASTERORGANIZATION_ID FROM TRANSACCIONES",
			dinamico = false,
			parametros = Parametros()))

		val kpiLectoras = (KpiUI(
			titulo = "Lectoras",
			descripcion = "Lectoras",
			origen = "",
			sql = "SELECT DISTINCT  LECTORA_ID, LECTORA_FISICA_ID FROM  TRANSACCIONES",
			dinamico = false,
			parametros = Parametros()))

		val kpiUsuarios = (KpiUI(
			titulo = "Usuarios",
			descripcion = "Usuarios",
			origen = "",
			sql = "SELECT DISTINCT  USUARIO_LECTORA FROM  TRANSACCIONES",
			dinamico = false,
			parametros = Parametros()))

		val kpiMovimientos = (KpiUI(
			titulo = "Movimientos",
			descripcion = "Movimientos realizadoso",
			origen = "",
			sql = "SELECT TIPO_MOV,  COUNT(*) AS EL FROM  TRANSACCIONES WHERE REQ_STATUS = 0 GROUP BY 1 , 2 ",
			dinamico = false,
			parametros = Parametros()))

		val kpiTransaccionesLectoiras = (KpiUI(
			titulo = "Transaccione de \$LECTORA_FISICA_ID",
			descripcion = "Lectoras",
			origen = "",
			sql = "SELECT MOB_REQUEST_ID, REQ_STATUS, LECTORA_FISICA_ID, USUARIO_LECTORA FROM  TRANSACCIONES WHERE LECTORA_FISICA_ID = '\$LECTORA_FISICA_ID'",
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "LECTORA_FISICA_ID", valor = "", defecto = "AU0604", fijo = false))))
										)

		val kpiTransacciones = KpiUI(
			titulo = "Transacciones",
			descripcion = "Transacciones",
			origen = "",
			sql = "SELECT 'TRX', COUNT(*) FROM TRANSACCIONES",
			dinamico = true,
			parametros = Parametros()
		)


		crearDashboardGeneral()
		crearDashboardInfoLectoras()

	}

	suspend fun crearDashboardInfoLectoras(){
		val k = (KpiUI(
			titulo = "Lectoras",
			descripcion = "Lectoras",
			origen = "",
			sql = "SELECT DISTINCT  LECTORA_ID, LECTORA_FISICA_ID FROM  TRANSACCIONES",
			dinamico = false,
			parametros = Parametros()))

		val kpiLectoras = operaciones.guardarKpi(k)
		operaciones.crearPanel(kpiLectoras, crearKPI = false, configuracion = PanelConfiguracion(mostrarGrafica = false))

		val kpiTransacciones = KpiUI(
			titulo = "Transacciones",
			descripcion = "Transacciones totales",
			origen = "",
			sql = "SELECT 'TRX', COUNT(*) FROM TRANSACCIONES WHERE LECTORA_FISICA_ID = '#LECTORA_FISICA_ID'",
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "LECTORA_FISICA_ID", valor = "", defecto = "AU0604", fijo = false)))
		)




		//transacciones diarias
		val kpiEstadoTransaccionesUltimoDiaPorLectora = KpiUI(
			titulo = "Estado ",
			descripcion = "Estado en el que se encuentrna las transacciones en el último día",
			origen = "",
			sql = """
				SELECT
					CASE 
					WHEN REQ_STATUS = 0 THEN 'OK' 
					WHEN REQ_STATUS = 1 THEN 'ERROR'
					WHEN REQ_STATUS = 2 THEN 'ERROR ORACLE'
					WHEN REQ_STATUS = 3 THEN 'OK'
					WHEN REQ_STATUS = 4 THEN 'REPROCESADO'
					ELSE 'DESCONOCIDO'
					
					END ESTADO,
					COUNT(MOB_REQUEST_ID) AS TRX
				FROM
					TRANSACCIONES
				WHERE
					DATE(CREATION_DATE) = (SELECT MAX(DATE(CREATION_DATE)) FROM TRANSACCIONES)
					AND LECTORA_FISICA_ID = '#LECTORA_FISICA_ID'
				GROUP BY
					REQ_STATUS		
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "LECTORA_FISICA_ID", valor = "", defecto = "AU0604", fijo = false)))
		)


//transacciones diarias
		val kpiTransaccionesRealizadas = KpiUI(
			titulo = "Transacciones diarias ",
			descripcion = "Transacciones realizadas el ultimo día",
			origen = "",
			sql = """
					SELECT MOB_REQUEST_ID,
					 		CREATION_DATE,
							CASE 
								WHEN REQ_STATUS = 0 THEN 'OK' 
								WHEN REQ_STATUS = 1 THEN 'ERROR'
								WHEN REQ_STATUS = 2 THEN 'ERROR ORACLE'
								WHEN REQ_STATUS = 3 THEN 'OK'
								WHEN REQ_STATUS = 4 THEN 'REPROCESADO'
								ELSE 'DESCONOCIDO'
								
							END ESTADO, 
							TIPO_MOV, 
							LECTORA_FISICA_ID, 
							LECTORA_ID, 
							PROGRAM_VERSION,
							ETIQUETAS					
				FROM
					TRANSACCIONES
				WHERE
					DATE(CREATION_DATE) = (SELECT MAX(DATE(CREATION_DATE)) FROM TRANSACCIONES)
					AND LECTORA_FISICA_ID = '#LECTORA_FISICA_ID'
				ORDER BY MOB_REQUEST_ID DESC
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "LECTORA_FISICA_ID", valor = "", defecto = "AU0604", fijo = false)))
		)


		//tipos de transacciones realziadas
		val kpiTiposTransaccionesRealizadas = KpiUI(
			titulo = "Transacciones diarias ",
			descripcion = "Transacciones realizadas el ultimo día",
			origen = "",
			sql = """
					SELECT 
							TIPO_MOV, 
							COUNT(*)
					FROM
						TRANSACCIONES
					WHERE
						DATE(CREATION_DATE) = (SELECT MAX(DATE(CREATION_DATE)) FROM TRANSACCIONES)
						AND LECTORA_FISICA_ID = '#LECTORA_FISICA_ID'
					GROUP BY TIPO_MOV
					ORDER BY 2 DESC
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "LECTORA_FISICA_ID", valor = "", defecto = "AU0604", fijo = false)))
		)
		val panelTransacciones = operaciones.crearPanel(kpiTransacciones, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.IndicadorVertical(), mostrarGrafica = true, mostrarTabla = false, height = 200.dp) )
		val panelTransaccionesEstados = operaciones.crearPanel(kpiEstadoTransaccionesUltimoDiaPorLectora, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.IndicadorVertical(), mostrarGrafica = false, height = 200.dp) )
		val panelTransaccionesDiarias = operaciones.crearPanel(kpiTransaccionesRealizadas, true, PanelConfiguracion().copy(mostrarGrafica = false, indicadorColor = false, colores = 3, ajustarContenidoAncho = false))
		val panelTiposTreansaccionesLectora = operaciones.crearPanel(kpiTiposTransaccionesRealizadas, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.Anillo(),   colores = 2, ajustarContenidoAncho = true))


		operaciones.guardarDashboard(nombre = "#LECTORA_FISICA_ID",  listOf<PanelUI>(panelTransacciones, panelTransaccionesEstados, panelTransaccionesDiarias, panelTiposTreansaccionesLectora), kpiOrigen =  kpiLectoras)
	}


	suspend fun crearDashboardGeneral(){

		//transacciones diarias
		val kpiTransaccionesDiarias = KpiUI(
			titulo = "Transacciones Diarias",
			descripcion = "Transacciones realizadas cada día",
			origen = "",
			sql = """
				SELECT
					DATE(CREATION_DATE) AS Fecha,
					COUNT(MOB_REQUEST_ID) AS NumeroDeTransacciones
				FROM
					TRANSACCIONES
				GROUP BY
					Fecha
				ORDER BY
					1 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		//transacciones diarias
		val kpiEstadoTransaccionesUltimoDia = KpiUI(
			titulo = "Estado",
			descripcion = "Estado en el que se encuentrna las transacciones en el último día",
			origen = "",
			sql = """
				SELECT
					CASE 
					WHEN REQ_STATUS = 0 THEN 'OK' 
					WHEN REQ_STATUS = 1 THEN 'ERROR'
					WHEN REQ_STATUS = 2 THEN 'ERROR ORACLE'
					WHEN REQ_STATUS = 3 THEN 'OK'
					WHEN REQ_STATUS = 4 THEN 'REPROCESADO'
					ELSE 'DESCONOCIDO'
					
					END ESTADO,
					COUNT(MOB_REQUEST_ID) AS TRX
				FROM
					TRANSACCIONES
				WHERE
					DATE(CREATION_DATE) = (SELECT MAX(DATE(CREATION_DATE)) FROM TRANSACCIONES)
				GROUP BY
					REQ_STATUS		
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)

		//versiones en el ultimo dia
		val kpiVersionesUltimoDia = KpiUI(
			titulo = "Fragmentación",
			descripcion = "Fragmentacion de versiones en las transacciones realizadas",
			origen = "",
			sql = """
				SELECT
					PROGRAM_VERSION as Version,
					COUNT(MOB_REQUEST_ID) AS  Trx
				FROM
					TRANSACCIONES
				WHERE
					DATE(CREATION_DATE) = (SELECT MAX(DATE(CREATION_DATE)) FROM TRANSACCIONES)
				GROUP BY
					PROGRAM_VERSION
				ORDER BY
					1;
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)


		//versiones en el ultimo dia
		val kpiHorasTransacciones = KpiUI(
			titulo = "Transacciones por horas",
			descripcion = "Estimación de las trnsacciones realizadas por horas",
			origen = "",
			sql = """
				SELECT
					STRFTIME('%H', CREATION_DATE) AS Hora,
					COUNT(MOB_REQUEST_ID) AS NumeroDeTransacciones
				FROM
					TRANSACCIONES
				/*WHERE
					DATE(CREATION_DATE) = (SELECT MAX(DATE(CREATION_DATE)) FROM TRANSACCIONES)*/
				GROUP BY
					Hora
				ORDER BY
					1;
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)

		//versiones en el ultimo dia
		val kpiErroresDiarios = KpiUI(
			titulo = "Errores",
			descripcion = "Errores diarios (REQ_STATUS = 2)",
			origen = "",
			sql = """
				SELECT
				   STRFTIME('%m-%d', CREATION_DATE)  AS Fecha,
				    COUNT(MOB_REQUEST_ID) AS NumeroDeErrores
				FROM
				    TRANSACCIONES
				WHERE
				    REQ_STATUS = 2
				GROUP BY
				    Fecha
				ORDER BY
				1 desc
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)

		val kpiTransaccionesLectoras = KpiUI(
			titulo = "Transacciones Lectoras",
			descripcion = "Esta mal, no esta tomando la medida en el mismo diaTransacciones realizadas cada lectora en el dia",
			origen = "",
			sql = """
				SELECT
					LECTORA_FISICA_ID as LECTORA,
					COUNT(MOB_REQUEST_ID) AS TRX
				FROM
					TRANSACCIONES
				WHERE
					DATE(CREATION_DATE) = (SELECT MAX(DATE(CREATION_DATE)) FROM TRANSACCIONES)
				GROUP BY
					LECTORA_FISICA_ID
				ORDER BY
					2 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)

		val panelTransaccionesDiarias = operaciones.crearPanel(kpiTransaccionesDiarias, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.BarrasFinasVerticales()))
		val panelTransaccionesUltimoDia = operaciones.crearPanel(kpiEstadoTransaccionesUltimoDia, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.Circular()))
		val panelFragmentacion = operaciones.crearPanel(kpiVersionesUltimoDia, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.Anillo()))
		val panelHoras = operaciones.crearPanel(kpiHorasTransacciones, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.BarrasFinasVerticales()))
		val panelErroresDiarios = operaciones.crearPanel(kpiErroresDiarios, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.Lineas(), agruparResto = false, limiteElementos = 7))
		val panelTransaccionesLectoras = operaciones.crearPanel(kpiTransaccionesLectoras, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.Lineas(), agruparResto = false, limiteElementos = 7))
		operaciones.guardarDashboard(nombre = "General", listOf<PanelUI>(panelTransaccionesDiarias, panelTransaccionesUltimoDia, panelFragmentacion, panelHoras, panelErroresDiarios, panelTransaccionesLectoras))

	}
}