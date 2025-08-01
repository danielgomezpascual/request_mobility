package com.personal.metricas.inicializador.domain

import androidx.compose.ui.unit.dp
import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.core.utils.Parametro
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.paneles.domain.entidades.PanelTipoGrafica
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel
import com.personal.metricas.paneles.ui.entidades.PanelUI
import org.koin.mp.KoinPlatform.getKoin

class InicializadorManager(
	private val operaciones: InicializadorOperaciones,
	private val dialog: DialogManager,
) {


	suspend fun eliminarDatosGeneradosPreviamente() {

		val trxDao = getKoin().get<AppDatabase>().transaccionesDao()
		val appDatabase = getKoin().get<AppDatabase>()

		val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase // Usamos readableDatabase para operaciones de lectura
		db.execSQL("DELETE FROM DASHBOARD WHERE autogenerado = 'Y'")
		db.execSQL("DELETE FROM PANELES WHERE autogenerado = 'Y'")
		db.execSQL("DELETE FROM KPIS WHERE autogenerado = 'Y'")

		db.execSQL("DROP VIEW  IF EXISTS TRX_7 ")
		db.execSQL("CREATE VIEW  TRX_7 AS SELECT * FROM TRANSACCIONES  WHERE DATE(CREATION_DATE) >= DATE('now', '-7 days')")
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
					TRX_7
				WHERE		
					 LECTORA_FISICA_ID = '#LECTORA_FISICA_ID'
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
					TRX_7
				WHERE		
					 LECTORA_FISICA_ID = '#LECTORA_FISICA_ID'
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
						TRX_7
					WHERE		
						LECTORA_FISICA_ID = '#LECTORA_FISICA_ID'
					GROUP BY TIPO_MOV
					ORDER BY 2 DESC
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "LECTORA_FISICA_ID", valor = "", defecto = "AU0604", fijo = false)))
		)






		val panelTransacciones = operaciones.crearPanel(kpiTransacciones, true, PlantillasPanel.from(PlantillasPanel.TT.IndicadorVertical.valor).configuracion)
		val panelTransaccionesEstados = operaciones.crearPanel(kpiEstadoTransaccionesUltimoDiaPorLectora, true, PlantillasPanel.from(PlantillasPanel.TT.PanelesHorizontales.valor).configuracion)
		val panelTransaccionesDiarias = operaciones.crearPanel(kpiTransaccionesRealizadas, true, PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion)
		val panelTiposTreansaccionesLectora = operaciones.crearPanel(kpiTiposTransaccionesRealizadas, true, PlantillasPanel.from(PlantillasPanel.TT.Anillo.valor).configuracion)


		operaciones.guardarDashboard(nombre = "#LECTORA_FISICA_ID",
									 listOf<PanelUI>(panelTransacciones, panelTransaccionesEstados, panelTransaccionesDiarias, panelTiposTreansaccionesLectora),
									 kpiOrigen =  kpiLectoras,
									 etiqueta = Etiquetas.EtiquetaValor("Lectora"))
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
					TRX_7
				
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
					TRX_7				
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
			titulo = "Transacciones Lectoras 7 días",
			descripcion = "Número de transacciones que ha realziado cada lectora en los ultimos 7 dias",
			origen = "",
			sql = """
				SELECT
					LECTORA_FISICA_ID as LECTORA,
					COUNT(MOB_REQUEST_ID) AS TRX
				FROM
					TRX_7
				
				GROUP BY
					LECTORA_FISICA_ID
				ORDER BY
					2 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)


		//ultimas transaccion realizada
		val kpiUltimaTransaccionRealizada = KpiUI(
			titulo = "Ultima TRX de cada lectora",
			descripcion = "Obtiene la última transaccionrealizada de cada lectora y los dias transcurridos",
			origen = "",
			sql = """				 
				SELECT
					LECTORA_FISICA_ID AS LECTORA,
					MAX(DATE(CREATION_DATE)) AS 'ULT TRX',					
					CAST(julianday('now') - julianday(MAX(DATE(CREATION_DATE))) AS INTEGER) AS DIAS
				FROM
					TRANSACCIONES
				GROUP BY
					LECTORA_FISICA_ID				
				ORDER BY DIAS DESC
   
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)


		val panelTransaccionesDiarias = operaciones.crearPanel(kpiTransaccionesDiarias, true, PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion)
		val panelTransaccionesUltimoDia = operaciones.crearPanel(kpiEstadoTransaccionesUltimoDia, true, PlantillasPanel.from(PlantillasPanel.TT.Circular.valor).configuracion)
		val panelFragmentacion = operaciones.crearPanel(kpiVersionesUltimoDia, true, PlantillasPanel.from(PlantillasPanel.TT.Anillo.valor).configuracion)
		val panelHoras = operaciones.crearPanel(kpiHorasTransacciones, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.BarrasFinasVerticales()))
		val panelErroresDiarios = operaciones.crearPanel(kpiErroresDiarios, true, PlantillasPanel.from(PlantillasPanel.TT.Lineas.valor).configuracion)
		val panelTransaccionesLectoras = operaciones.crearPanel(kpiTransaccionesLectoras, true, PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion)
		val panelUltimaTransaccionRalizada = operaciones.crearPanel(kpiUltimaTransaccionRealizada, true, PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion)


		operaciones.guardarDashboard(nombre = "General", listOf<PanelUI>(panelTransaccionesDiarias, panelTransaccionesUltimoDia, panelFragmentacion, panelHoras, panelErroresDiarios, panelTransaccionesLectoras, panelUltimaTransaccionRalizada), etiqueta = Etiquetas.EtiquetaValor("General"))

	}
}