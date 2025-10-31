package com.personal.metricas.inicializador.domain

import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.metricas.App
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.core.data.ds.remote.network.retrofit.request.Entornos
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.core.utils.Parametro
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.endpoints.ui.entidades.EndPointUI
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.paneles.domain.entidades.Alarmas
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.paneles.domain.entidades.PanelTipoGrafica
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel
import com.personal.metricas.paneles.ui.entidades.PanelUI
import org.koin.mp.KoinPlatform.getKoin


class InicializadorManager(
	private val operaciones: InicializadorOperaciones,
	private val initGeneral: InitDahsboardGeneral,
	private val initOrganizacioes: InitDashboardOrganizaciones,
	private val initLectoras: InitDahsboardLectoras,
	private val initLog: InitDahsboardLog,
	private val dialog: DialogManager,
) {
	val appDatabase = getKoin().get<AppDatabase>()

	val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase // Usamos readableDatabase para operaciones de lectura

	suspend fun eliminarDatosGeneradosPreviamente() {

		val trxDao = getKoin().get<AppDatabase>().transaccionesDao()

		db.execSQL("DELETE FROM DASHBOARD WHERE autogenerado = 'Y'")
		db.execSQL("DELETE FROM PANELES WHERE autogenerado = 'Y'")
		db.execSQL("DELETE FROM KPIS WHERE autogenerado = 'Y'")


	}

	suspend fun crearVistas() {


		db.execSQL("DROP TABLE  IF EXISTS ESTADOS_TRANSACCIONES ")
		db.execSQL("""CREATE TABLE IF NOT EXISTS ESTADOS_TRANSACCIONES (
							STATUS_CODE INTEGER PRIMARY KEY NOT NULL,
							ESTADO TEXT NOT NULL
						);""")

		db.execSQL("INSERT INTO ESTADOS_TRANSACCIONES (STATUS_CODE, ESTADO) VALUES (0, 'OK');")
		db.execSQL("INSERT INTO ESTADOS_TRANSACCIONES (STATUS_CODE, ESTADO) VALUES (1, 'ERROR DE MOBILITY DESKTOP');")
		db.execSQL("INSERT INTO ESTADOS_TRANSACCIONES (STATUS_CODE, ESTADO) VALUES (2, 'ERROR');")
		db.execSQL("INSERT INTO ESTADOS_TRANSACCIONES (STATUS_CODE, ESTADO) VALUES (4, 'REPROCESADO');")

		db.execSQL("DROP VIEW  IF EXISTS TRX_7 ")
		db.execSQL("CREATE VIEW  TRX_7 AS SELECT * FROM TRANSACCIONES T INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE WHERE DATE(CREATION_DATE) >= DATE('now', '-7 days')")

		db.execSQL("DROP VIEW  IF EXISTS TRX_HOY ")
		db.execSQL("CREATE VIEW IF NOT EXISTS TRX_HOY AS SELECT   * FROM  TRANSACCIONES T INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE WHERE  date(CREATION_DATE)  = date('now', 'localtime');")

		db.execSQL("DROP VIEW  IF EXISTS TRX_TIME ")
		db.execSQL("CREATE VIEW IF NOT EXISTS TRX_TIME AS SELECT   * FROM  TRANSACCIONES T INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE WHERE  date(CREATION_DATE)  > date('now', 'localtime', '-5 days');")



		//db.execSQL("CREATE VIEW IF NOT EXISTS TRX_HOY AS SELECT   * FROM  TRANSACCIONES T INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE WHERE  date(CREATION_DATE)  >= date('now', '-3 days');")

	}


	suspend fun start() {


		eliminarDatosGeneradosPreviamente()
		crearVistas()

		val dashboardLectora = initLectoras.dashboardLectoar()
		val dashboardUI = initOrganizacioes.generaDashboardOrganizaciones(dashboardLectora)
		initGeneral.crearGeneral(dashboardUI)
		initLog.crearDashboard()

		//crearDashboardGeneral()
		//crearDashboardErrores()

	/*	crearDashboardGeneralExtra()
		crearDashboardOrganizacion()
		crearDashboardVersiones()
		crearDashboardLectoras()*/


	}


	suspend fun crearDashboardGeneral() {


		val condiciones: Condiciones = Condiciones(id = 1,
												   columna =
													   Columnas(nombre = "LECTORA", posicion = 0, valores = emptyList()),
												   color = 0,
												   condicionCelda = 1,
												   predicado = "",
												   descripion = "",
												   alarma = Alarmas())
		val listaCondicionesBanderas: List<Condiciones> = listOf<Condiciones>(condiciones)


		val condicionesError: Condiciones = Condiciones(1, Columnas("ESTADO",
																	posicion = 4,
																	valores = emptyList()),
														condicionCelda = 0,
														color = 3,
														predicado = "== 'ERROR'",
														descripion = "",
														alarma = Alarmas())

		val condicionesReprocesameinto: Condiciones = Condiciones(1, Columnas("ESTADO",
																			  posicion = 4,
																			  valores = emptyList()),
																  condicionCelda = 0,
																  color = 5,
																  predicado = "== 'REPROCESADO'",
																  descripion = "",
																  alarma = Alarmas())

		val listaCondicionesErr = listOf<Condiciones>(condicionesError, condicionesReprocesameinto)


		//transacciones diarias
		val kpiTransaccionesDiarias = KpiUI(
			titulo = "Transacciones",
			descripcion = "Transacciones realizadas hoy",
			origen = "",
			sql = """
				SELECT
					LECTORA_FISICA_ID as 'LECTORA',
					MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO,  USUARIO_LECTORA, 
					strftime('%m-%d %H:%M', CREATION_DATE)  AS Fecha
				FROM
					TRX_HOY  T
			
				ORDER BY
					
				2 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		val panelTransaccionesDiarias = operaciones.crearPanel(kpiTransaccionesDiarias,
															   true,
															   PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(condicionesCeldas = listaCondicionesBanderas, condiciones = listaCondicionesErr, ajustarContenidoAncho = false))


		//Errores TRX HOY
		val kpiTransaccionesDiariasError = KpiUI(
			titulo = "Errores del dia",
			descripcion = "Errores que se han producido en el día",
			origen = "",
			sql = """
				SELECT
					LECTORA_FISICA_ID,
					MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO,  USUARIO_LECTORA, 
					strftime('%m-%d %H:%M', CREATION_DATE)  AS Fecha
				FROM
					TRX_HOY  T
				WHERE REQ_STATUS = 2
				
				ORDER BY
					2 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		val panelTransaccionesDiariasError = operaciones.crearPanel(kpiTransaccionesDiariasError,
																	true,
																	PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(condicionesCeldas = listaCondicionesBanderas, colores = EsquemaColores.Paletas.ERRORES.valor, height = "200", ajustarContenidoAncho = false)
		)

		// TRX por Organizacion en los ultimos 7 dias
		val kpiTransaccionesPorOrganizacion = KpiUI(
			titulo = "Transacciones por Organizacion ",
			descripcion = "Conteo de transacciones realizadas por organizacion procesadas correctamente",
			sql = """
				SELECT
					ORGANIZATION_CODE AS 'CODE',
					COUNT(*) AS 'TRX',
					ORGANIZATION_ID AS 'ID',
					ORGANIZATION_NAME AS 'NOMBRE'				
				
				FROM TRX_7
				WHERE REQ_STATUS = '0'
				GROUP BY ORGANIZATION_ID	
				ORDER BY 2 DESC
			""".trimIndent(),
			origen = "",
			dinamico = true,
			parametros = Parametros()
		)
		val panelTransaccionesPorOrganizacion = operaciones.crearPanel(kpiTransaccionesPorOrganizacion,
																	   true,
																	   PlantillasPanel.from(PlantillasPanel.TT.BarrasAnchasVertivales.valor).configuracion.copy(ajustarContenidoAncho = false,
																																								colores = EsquemaColores.Paletas.MULTICOLOR.valor)
		)


		// Evolucion de errores
		val kpiEvolucionErrores = KpiUI(
			titulo = "Errores diarios ",
			descripcion = "Evolucion de errores en el sistemas",
			sql = """
				SELECT
					strftime('%m-%d', CREATION_DATE) AS 'DIA (M/D)',
					COUNT(*) AS 'Errores'
				FROM
					TRANSACCIONES
				WHERE					
					 REQ_STATUS = 2
				GROUP BY
					1
				ORDER BY 1 DESC
					;
			""".trimIndent(),
			origen = "",
			dinamico = true,
			parametros = Parametros()
		)
		val panelEvolucionErrores = operaciones.crearPanel(kpiEvolucionErrores,
														   true,
														   PlantillasPanel.from(PlantillasPanel.TT.Lineas.valor).configuracion.copy(
															   ajustarContenidoAncho = true,
															   limiteElementos = 8,
															   colores = EsquemaColores.Paletas.PERS.valor)
		)


		//transacciones diarias
		val kpiConteoTransacciones = KpiUI(
			titulo = "Conteo Transacciones",
			descripcion = "Transacciones por dia",
			origen = "",
			sql = """
				SELECT
					strftime('%m-%d', CREATION_DATE)  AS Fecha, 
					COUNT(*) AS TRX
										
				FROM
					TRANSACCIONES
				GROUP BY 1
				
				ORDER BY				
				1 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		val panelConteoTransacciones = operaciones.crearPanel(kpiConteoTransacciones,
															  true,
															  PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion.copy(limiteElementos = 20, ajustarContenidoAncho = true))


		val dh = operaciones.guardarDashboard(nombre = "General",
											  listOf<PanelUI>(
												  panelTransaccionesDiarias,
												  panelTransaccionesDiariasError,
												  panelTransaccionesPorOrganizacion,
												  panelEvolucionErrores,
												  panelConteoTransacciones

											  ),
											  etiqueta = Etiquetas.EtiquetaValor("General"),
											  home = true,
											  color = -16744448
		)


	}

	suspend fun crearDashboardGeneralExtra() {


		//transacciones diarias
		val kpiTranasccionesEmpleo = KpiUI(
			titulo = "Empleo",
			descripcion = "En que transacciones se emplea más la aplicacion (No cuenta reprocesamiento)",
			origen = "",
			sql = """
				SELECT
					TIPO_MOV,
					COUNT(*) AS 'NUM' 
				FROM
					TRANSACCIONES  T
				WHERE REQ_STATUS = '0' OR REQ_STATUS = '2'
				GROUP  BY 1 
				ORDER BY
					2 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		val panelTransaccionesEmpleo = operaciones.crearPanel(kpiTranasccionesEmpleo,
															  true,

															  PlantillasPanel.from(PlantillasPanel.TT.Circular.valor).configuracion.copy(ajustarContenidoAncho = true))


		//transacciones diarias
		val kpiTranasccionesPorLectora = KpiUI(
			titulo = "Transacciones por lectora",
			descripcion = "Indicador de las lectoras que más trabajo se produce",
			origen = "",
			sql = """
				SELECT
					LECTORA_FISICA_ID as 'LECTORA',
					COUNT(*) AS 'NUM' 
				FROM
					TRANSACCIONES  T
				WHERE REQ_STATUS = '0' OR REQ_STATUS = '2'
				GROUP  BY 1 
				ORDER BY
					2 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		val panelTransaccionesLectora = operaciones.crearPanel(kpiTranasccionesPorLectora,
															   true,
															   PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor)
																   .configuracion.copy(limiteElementos = 10, ajustarContenidoAncho = true))


		//transacciones diarias
		val kpiTranasccionesErrorPorLectora = KpiUI(
			titulo = "Errores por lectora",
			descripcion = "Indicador de errores que se producen por lectora",
			origen = "",
			sql = """
				SELECT
					LECTORA_FISICA_ID AS 'LECT',					  
					COUNT(*) AS 'ERRORES' 
				FROM
					TRANSACCIONES  T
				WHERE REQ_STATUS = '2'
				GROUP  BY 1 
				ORDER BY
					2 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		val panelTransaccionesErrorLectora = operaciones.crearPanel(kpiTranasccionesErrorPorLectora,
																	true,
																	PlantillasPanel.from(PlantillasPanel.TT.BarrasAnchasVertivales.valor).configuracion.copy(
																		colores = EsquemaColores.Paletas.ERRORES.valor,
																		limiteElementos = 25,
																		mostrarEtiquetas = true
																	))


		//versiones en el ultimo dia
		val kpiHorasTransacciones = KpiUI(
			titulo = "Transacciones por horas",
			descripcion = "Estimación de ocupación.",
			origen = "",
			sql = """
				SELECT
					STRFTIME('%H', CREATION_DATE) AS Hora,
					COUNT(MOB_REQUEST_ID) AS 'Trx'
				FROM
					TRANSACCIONES
				
				GROUP BY
					Hora
				ORDER BY
					1 ASC;
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		val panelHoras = operaciones.crearPanel(kpiHorasTransacciones, true, PanelConfiguracion().copy(
			ajustarContenidoAncho = true,
			tipo =
				PanelTipoGrafica.BarrasFinasVerticales(),
			mostrarEtiquetas = true))


		val dh = operaciones.guardarDashboard(nombre = "General Extra",

											  listOf<PanelUI>(
												  panelTransaccionesEmpleo,
												  panelHoras,
												  panelTransaccionesLectora,
												  panelTransaccionesErrorLectora

											  ),
											  etiqueta = Etiquetas.EtiquetaValor("General"),
											  home = true,
											  color = -16744448
		)


	}

	suspend fun crearDashboardOrganizacion() {


		/*
				val condicion: Condiciones = Condiciones(1, Columnas("REQ_STATUS",
																	 posicion = 5,
																	 valores = emptyList()),
														 condicionCelda = 0,
														 color = 1,
														 predicado = "== '2'",
														 descripion = "",
														 alarma = Alarmas())

				val listaCondiciones = listOf<Condiciones>(condicion)*/


		val kpiOrganizaciones = (KpiUI(
			titulo = "Organizaciones",
			descripcion = "Organizaciones en el sistema",
			origen = "",
			sql = """SELECT DISTINCT
					ORGANIZATION_CODE,
					ORGANIZATION_ID,
					ORGANIZATION_NAME,
					MASTERORGANIZATION_ID 
  				   FROM
  				    TRANSACCIONES
				|  				    """.trimMargin(),
			dinamico = false,
			parametros = Parametros()))
		val k = operaciones.guardarKpi(kpiOrganizaciones)

		//endpoint de recarga de transacciones
		var listaPametrosEP: List<Parametro> = Parametros.dameParametrosPorDefectoMobility()
		listaPametrosEP = listaPametrosEP.plus(Parametro("P_ORGANIZATION_ID", "#ORGANIZATION_ID", "", false))
		val endPoint = EndPointUI(
			nombre = "Recargar datos)",
			descripcion = "Obtener Trx",
			url = "${Entornos.get(App.ENTORNO).url}GetSolicitudes",
			parametros = Parametros(listaPametrosEP),
			tabla = "Transacciones",
			nodoIdentificadorFila = "Solicitudes",
			eliminarDatos = false)
		val panelEndPointSolicitudes = operaciones.guardarEndPoint(endPoint)


		var listaPametrosKpi: List<Parametro> = Parametros.dameParametrosPorDefectoMobility()
		listaPametrosKpi = listOf<Parametro>(Parametro(key = "ORGANIZATION_ID", valor = "", defecto = "2206", fijo = false))


		val kpiConteoTransacciones = KpiUI(
			titulo = "Resumen  transacciones",
			descripcion = "",
			origen = "",
			sql = """
				SELECT TIPO_MOV, COUNT(*) FROM   TRX_HOY  WHERE   ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY TIPO_MOV
				UNION 
				SELECT 'ERROR', COUNT(*) FROM   TRX_HOY  WHERE REQ_STATUS = 2  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY 1
				UNION 
				SELECT 'REPROCESAMIENTO ', COUNT(*) FROM   TRX_HOY  WHERE REQ_STATUS = 4  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY 1""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelConteo = operaciones.crearPanel(kpiConteoTransacciones,
												 true,
												 PlantillasPanel.from(PlantillasPanel.TT.IndicadorHorizontal.valor).configuracion.copy(limiteElementos = 0))


		val condicionesError: Condiciones = Condiciones(1, Columnas("ESTADO",
																	posicion = 4,
																	valores = emptyList()),
														condicionCelda = 0,
														color = 3,
														predicado = "== 'ERROR'",
														descripion = "",
														alarma = Alarmas())

		val condicionesReprocesameinto: Condiciones = Condiciones(1, Columnas("ESTADO",
																			  posicion = 4,
																			  valores = emptyList()),
																  condicionCelda = 0,
																  color = 5,
																  predicado = "== 'REPROCESADO'",
																  descripion = "",
																  alarma = Alarmas())

		val listaCondicionesErr = listOf<Condiciones>(condicionesError, condicionesReprocesameinto)

		//trasnacciones ultimo dia
		val kpiTransaccionesUltimoDiaOrganizacion = KpiUI(
			titulo = "Hoy",
			descripcion = "Tranasacciones realizadas en el día de hoy ",
			origen = "",
			sql = """SELECT 
						MOB_REQUEST_ID, 
						strftime('%m-%d %H:%M', CREATION_DATE) AS 'DIA', 
						TIPO_MOV, 
						NUMERO, 
						ESTADO, 
						LECTORA_ID, 
						USUARIO_LECTORA 
					FROM 
						TRX_HOY 
					WHERE 
						 ORGANIZATION_ID = '#ORGANIZATION_ID'
					ORDER BY MOB_REQUEST_ID DESC""".trimMargin(),
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelTransaccionesUltimoDia = operaciones.crearPanel(kpiTransaccionesUltimoDiaOrganizacion,
																 true,

																 PlantillasPanel.from(
																	 PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(ajustarContenidoAncho = false,
																															condiciones = listaCondicionesErr))

		//errores que se han producido en el ultimo día
		val erroresDia = KpiUI(
			titulo = "Errores en el día",
			descripcion = "",
			origen = "",
			sql = "SELECT MOB_REQUEST_ID, strftime('%m-%d %H:%M', CREATION_DATE) AS 'DIA', TIPO_MOV, NUMERO, ESTADO, LECTORA_ID, USUARIO_LECTORA FROM TRX_HOY WHERE  ORGANIZATION_ID = '#ORGANIZATION_ID' AND REQ_STATUS = '2' ORDER BY MOB_REQUEST_ID DESC",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelErroresDia = operaciones.crearPanel(erroresDia,
													 true,
													 PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(colores = EsquemaColores.Paletas.ERRORES.valor, ajustarContenidoAncho = false))


		val kpiConteoTransaccionesSemana = KpiUI(
			titulo = "Semana",
			descripcion = "Resumen de transacciones realziadas en los útimos 7 días",
			origen = "",
			sql = """
				SELECT TIPO_MOV, COUNT(*) FROM   TRX_7  WHERE REQ_STATUS = 0  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY TIPO_MOV
				UNION 
				SELECT 'ERROR', COUNT(*) FROM   TRX_7  WHERE REQ_STATUS = 2  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY 1
				UNION 
				SELECT 'REPROCESADO ', COUNT(*) FROM   TRX_7  WHERE REQ_STATUS = 4  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY 1
				ORDER BY 1 ASC
				
				""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelConteoSemana = operaciones.crearPanel(kpiConteoTransaccionesSemana,
													   true,
													   PlantillasPanel.from(PlantillasPanel.TT.IndicadorHorizontal.valor).configuracion.copy(limiteElementos = 0))


		//trasnacciones ultima semana

		val kpiTransaccionesSemana = KpiUI(
			titulo = "Tranasacciones realizadas en la ultima semana ",
			descripcion = "",
			origen = "",
			sql = """SELECT 
						strftime('%Y-%m-%d %H:%M', CREATION_DATE) AS 'DIA',
						MOB_REQUEST_ID,
						TIPO_MOV, 
						NUMERO, 
						ESTADO,  
						REQ_STATUS,
						LECTORA_ID,
						USUARIO_LECTORA
					FROM 
						TRX_7 
					WHERE
					  ORGANIZATION_ID = '#ORGANIZATION_ID'
					ORDER BY 1 DESC
				    """.trimMargin(),
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)

		val condicion: Condiciones = Condiciones(1, Columnas("REQ_STATUS",
															 posicion = 5,
															 valores = emptyList()),
												 condicionCelda = 0,
												 color = 3,
												 predicado = "== '2'",
												 descripion = "",
												 alarma = Alarmas())

		val listaCondiciones = listOf<Condiciones>(condicion)
		val panelTransaccionesSemana: PanelUI = operaciones.crearPanel(kpiTransaccionesSemana,
																	   true,
																	   PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(ajustarContenidoAncho = false, condiciones = listaCondiciones))


		//errores que se ham producido en el periodo
		val kpiErroresPeriodo = KpiUI(
			titulo = "Errores",
			descripcion = "",
			origen = "",
			sql = """
				SELECT
					strftime('%m-%d', CREATION_DATE) AS 'DIA (M/D)',
					COUNT(*) AS 'ERRORES'
				FROM
					TRANSACCIONES
				WHERE
					ORGANIZATION_ID = '#ORGANIZATION_ID' 
					AND REQ_STATUS = 2
				GROUP BY
					strftime('%m-%d', CREATION_DATE) 
				ORDER BY 1 DESC
				""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelErroresPeriodo = operaciones.crearPanel(kpiErroresPeriodo,
														 true,
														 PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion.copy(colores = EsquemaColores.Paletas.ERRORES.valor, limiteElementos = 0, mostrarEtiquetas = true, ajustarContenidoAncho = true))


		//trasnacciones totales
		val kpiTransaccionesOrganizacion = KpiUI(
			titulo = "Transacciones ",
			descripcion = "Transacciones realizadas en la organizacin ",
			origen = "",
			sql = "SELECT LECTORA_ID , TIPO_MOV, NUMERO, USUARIO_LECTORA, ESTADO FROM TRANSACCIONES WHERE  ORGANIZATION_ID = '#ORGANIZATION_ID'",
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "ORGANIZATION_ID", valor = "", defecto = "2206", fijo = false)))
		)
		val panelTransaccionesOrganizacion = operaciones.crearPanel(kpiTransaccionesOrganizacion,
																	true,
																	PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion)


		val kpiEstadoTransaccionnes = KpiUI(
			titulo = "Estados Transacciones Enviadas",
			descripcion = "Estado en el que se encuentran las transacciones",
			origen = "",
			sql = "SELECT  REQ_STATUS, COUNT(MOB_REQUEST_ID) FROM TRANSACCIONES  WHERE  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY REQ_STATUS ",
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "ORGANIZATION_ID", valor = "", defecto = "2206", fijo = false)))
		)
		val panelEstadoTransacciones = operaciones.crearPanel(kpiEstadoTransaccionnes,
															  true,
															  PlantillasPanel.from(PlantillasPanel.TT.Circular.valor).configuracion)


		val kpiOrigenErrores = KpiUI(
			titulo = "Transacciones deonde se originan los errores",
			descripcion = "Transaccion donde se origan los errores",
			origen = "",
			sql = "SELECT  TIPO_MOV, COUNT(MOB_REQUEST_ID) FROM TRANSACCIONES  WHERE  ORGANIZATION_ID = '#ORGANIZATION_ID'  AND REQ_STATUS = 2 GROUP BY TIPO_MOV  ",
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "ORGANIZATION_ID", valor = "", defecto = "2206", fijo = false)))
		)
		val panelOrigenErrores = operaciones.crearPanel(kpiOrigenErrores,
														true,
														PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion)


		val kpiLectoraErrores = KpiUI(
			titulo = "Transacciones en error. Origen Lectora",
			descripcion = "Errores producidos en cada lectora",
			origen = "",
			sql = "SELECT  LECTORA_ID, COUNT(MOB_REQUEST_ID) FROM TRANSACCIONES  WHERE  ORGANIZATION_ID = '#ORGANIZATION_ID'  AND REQ_STATUS = 2 GROUP BY LECTORA_ID  ",
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "ORGANIZATION_ID", valor = "", defecto = "2206", fijo = false)))
		)
		val panelErroresLectora = operaciones.crearPanel(kpiLectoraErrores,
														 true,
														 PlantillasPanel.from(PlantillasPanel.TT.SignalVertical.valor).configuracion)


		val kpiRatioErrores = (KpiUI(
			titulo = "Ratios",
			descripcion = "Ratios de errores sobre las transacciones corectas",
			origen = "",
			sql = """ 
				SELECT
					strftime('%m-%d', CREATION_DATE) AS 'Fecha',
					COUNT(*) AS 'TRX',
				
					-- Contadores
					SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS 'ERRORES',
					SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS 'OK',
				
					-- Porcentajes (redondeados a 2 decimales)
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% ERR',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% OK'
				FROM
					TRANSACCIONES
				WHERE
					ORGANIZATION_ID = '#ORGANIZATION_ID'
				GROUP BY
					strftime('%m-%d', CREATION_DATE)
				ORDER BY
					1 DESC
					
				LIMIT 15;
				;""",
			dinamico = true,
			parametros = Parametros(ps = listOf<Parametro>(Parametro(key = "ORGANIZATION_ID", valor = "", defecto = "2206", fijo = false)))


		))

		val kErroresRatio = operaciones.guardarKpi(kpiRatioErrores)


		val condicionesRatio: Condiciones = Condiciones(1, Columnas("% ERR",
																	posicion = 4,
																	valores = emptyList()),
														condicionCelda = 0,
														color = 3,
														predicado = "> 5",
														descripion = "",
														alarma = Alarmas())


		val panelErroresRatio = operaciones.crearPanel(
			kErroresRatio,
			false,
			PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor)
				.configuracion.copy(
					colores = 7,
					indicadorColor = false,
					condiciones = listOf<Condiciones>(condicionesRatio),
					ajustarContenidoAncho = false,
					width = "500", height = "600"))


		operaciones.guardarDashboard(nombre = "ORG #ORGANIZATION_ID #ORGANIZATION_CODE \n #ORGANIZATION_NAME",
									 listOf<PanelUI>(
										 // panelOrganizaciones,
										 panelEndPointSolicitudes,
										 panelConteo,
										 panelTransaccionesUltimoDia,
										 panelErroresDia,
										 panelConteoSemana,
										 panelTransaccionesSemana,
										 panelErroresPeriodo,
										 panelErroresLectora,
										 panelErroresRatio

									 ),

									 kpiOrigen = k,
									 etiqueta = Etiquetas.EtiquetaValor("ORGS"),
									 color = -2354116

		)

	}


	suspend fun crearDashboardLectoras() {


		val kpiOrganizaciones = (KpiUI(
			titulo = "Lectoras",
			descripcion = "Lectoras en el sistema",
			origen = "",
			sql = "SELECT DISTINCT  LECTORA_FISICA_ID FROM TRANSACCIONES",
			dinamico = false,
			parametros = Parametros()))
		val k = operaciones.guardarKpi(kpiOrganizaciones)

		//endpoint de recarga de transacciones
		var listaPametrosEP: List<Parametro> = Parametros.dameParametrosPorDefectoMobility()
		listaPametrosEP = listaPametrosEP.plus(Parametro("LECTORA_FISICA_ID", "#LECTORA_FISICA_ID", "", false))
		val endPoint = EndPointUI(
			nombre = "Solicitudes ",
			descripcion = "Obtener Trx",
			url = "${Entornos.get(App.ENTORNO).url}GetSolicitudes",
			parametros = Parametros(listaPametrosEP),
			tabla = "Transacciones",
			nodoIdentificadorFila = "Solicitudes",
			eliminarDatos = false)
		val panelEndPointSolicitudes = operaciones.guardarEndPoint(endPoint)


		var listaPametrosKpi: List<Parametro> = Parametros.dameParametrosPorDefectoMobility()
		listaPametrosKpi = listOf<Parametro>(Parametro(key = "LECTORA_FISICA_ID", valor = "", defecto = "2206", fijo = false))


		val kpiConteoTransacciones = KpiUI(
			titulo = "DIA",
			descripcion = "Transacciones realiadas",
			origen = "",
			sql = """
				SELECT TIPO_MOV, COUNT(*) FROM   TRX_HOY  WHERE REQ_STATUS = 0  AND  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' GROUP BY TIPO_MOV
				UNION 
				SELECT 'ERROR', COUNT(*) FROM   TRX_HOY  WHERE REQ_STATUS = 2  AND  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' GROUP BY TIPO_MOV
				UNION 
				SELECT 'REPROCESADO ', COUNT(*) FROM   TRX_HOY  WHERE REQ_STATUS = 4  AND  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' GROUP BY TIPO_MOV""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelConteo = operaciones.crearPanel(kpiConteoTransacciones,
												 true,
												 PlantillasPanel.from(PlantillasPanel.TT.IndicadorHorizontal.valor).configuracion.copy(limiteElementos = 0))

		//trasnacciones ultimo dia

		val condicionesError: Condiciones = Condiciones(1, Columnas("ESTADO",
																	posicion = 3,
																	valores = emptyList()),
														condicionCelda = 0,
														color = 3,
														predicado = "== 'ERROR'",
														descripion = "",
														alarma = Alarmas())

		val condicionesReprocesameinto: Condiciones = Condiciones(2, Columnas("ESTADO",
																			  posicion = 3,
																			  valores = emptyList()),
																  condicionCelda = 0,
																  color = 5,
																  predicado = "== 'REPROCESAMIENTO'",
																  descripion = "",
																  alarma = Alarmas())

		val listaCondicionesErr = listOf<Condiciones>(condicionesError, condicionesReprocesameinto)

		val kpiTransaccionesUltimoDiaOrganizacion = KpiUI(
			titulo = "Hoy",
			descripcion = "Tranasacciones realizadas en el día de hoy ",
			origen = "",
			sql = "SELECT MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO, LECTORA_ID, USUARIO_LECTORA FROM TRX_HOY WHERE  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID'",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelTransaccionesUltimoDia = operaciones.crearPanel(kpiTransaccionesUltimoDiaOrganizacion,
																 true,
																 PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(ajustarContenidoAncho = false, condiciones = listaCondicionesErr))

		//errores que se han producido en el ultimo día
		val erroresDia = KpiUI(
			titulo = "Errores en el día",
			descripcion = "Tranasacciones realizadas en el día de hoy ",
			origen = "",
			sql = "SELECT MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO, LECTORA_ID, USUARIO_LECTORA FROM TRX_HOY WHERE  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' AND REQ_STATUS = '2'",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelErroresDia = operaciones.crearPanel(erroresDia,
													 true,
													 PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(colores = EsquemaColores.Paletas.ERRORES.valor, ajustarContenidoAncho = false))


		val kpiConteoTransaccionesSemana = KpiUI(
			titulo = "Semana",
			descripcion = "Transacciones realiadas",
			origen = "",
			sql = """
				SELECT TIPO_MOV, COUNT(*) FROM   TRX_7  WHERE REQ_STATUS = 0  AND  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' GROUP BY TIPO_MOV
				UNION 
				SELECT '. ERROR', COUNT(*) FROM   TRX_7  WHERE REQ_STATUS = 2  AND  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' GROUP BY TIPO_MOV
				UNION 
				SELECT '. REPROCESAMIENTO ', COUNT(*) FROM   TRX_7  WHERE REQ_STATUS = 4  AND  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' GROUP BY TIPO_MOV
				ORDER BY 1 ASC
				
				""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelConteoSemana = operaciones.crearPanel(kpiConteoTransaccionesSemana,
													   true,
													   PlantillasPanel.from(PlantillasPanel.TT.IndicadorHorizontal.valor).configuracion.copy(limiteElementos = 0))


		//trasnacciones ultima semana

		val kpiTransaccionesSemana = KpiUI(
			titulo = "Semana",
			descripcion = "Tranasacciones realizadas en la ultima semana ",
			origen = "",
			sql = """SELECT 
				|		strftime('%m-%d', CREATION_DATE) AS DIA '(M/D)',  MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO,  
				|		REQ_STATUS, LECTORA_ID, USUARIO_LECTORA 
				|FROM
				| TRX_7 
				|WHERE
				|  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID'
				|ORDER BY 1 DESC """.trimMargin(),
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)

		val condicion: Condiciones = Condiciones(1, Columnas("REQ_STATUS",
															 posicion = 5,
															 valores = emptyList()),
												 condicionCelda = 0,
												 color = 1,
												 predicado = "== '2'",
												 descripion = "",
												 alarma = Alarmas())

		val listaCondiciones = listOf<Condiciones>(condicion)
		val panelTransaccionesSemana: PanelUI = operaciones.crearPanel(kpiTransaccionesSemana,
																	   true,
																	   PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(ajustarContenidoAncho = false, condiciones = listaCondiciones))


		//errores que se ham producido en el periodo
		val kpiErroresPeriodo = KpiUI(
			titulo = "Errores",
			descripcion = "Errores registrados en el perido indicado",
			origen = "",
			sql = """
				SELECT
					strftime('%m-%d', CREATION_DATE) AS 'DIA(M/D)',
					COUNT(*) AS numero_de_errores
				FROM
					TRANSACCIONES
				WHERE
					LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' 
					AND REQ_STATUS = 2
				GROUP BY
					1
				ORDER BY 1 DESC;
				""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelErroresPeriodo = operaciones.crearPanel(kpiErroresPeriodo,
														 true,
														 PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion.copy(colores = EsquemaColores.Paletas.ERRORES.valor, limiteElementos = 0, mostrarEtiquetas = true, ajustarContenidoAncho = true))


		val kpiRatioErrores = (KpiUI(
			titulo = "Ratios",
			descripcion = "Ratios de errores sobre las transacciones corectas",
			origen = "",
			sql = """ 
				SELECT
					strftime('%m-%d', CREATION_DATE) AS 'Fecha',
					COUNT(*) AS 'TRX',
				
					-- Contadores
					SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS 'ERRORES',
					SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS 'OK',
				
					-- Porcentajes (redondeados a 2 decimales)
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% ERR',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% OK'
				FROM
					TRANSACCIONES
				WHERE
					LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' 
				GROUP BY
					strftime('%m-%d', CREATION_DATE)
				ORDER BY
					1 DESC
					
				LIMIT 15;
				;""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)))

		val kErroresRatio = operaciones.guardarKpi(kpiRatioErrores)


		val condicionesRatio: Condiciones = Condiciones(1, Columnas("% ERR",
																	posicion = 4,
																	valores = emptyList()),
														condicionCelda = 0,
														color = 3,
														predicado = "> 5",
														descripion = "",
														alarma = Alarmas())


		val panelErroresRatio = operaciones.crearPanel(
			kErroresRatio,
			false,
			PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor)
				.configuracion.copy(
					colores = 7,
					indicadorColor = false,
					condiciones = listOf<Condiciones>(condicionesRatio),
					ajustarContenidoAncho = false,
					width = "500", height = "600"))




		operaciones.guardarDashboard(nombre = "#LECTORA_FISICA_ID",
									 listOf<PanelUI>(
//panelOrganizaciones,

										 panelEndPointSolicitudes,

										 panelConteo,
										 panelTransaccionesUltimoDia,
										 panelErroresDia,

										 panelConteoSemana,
										 panelTransaccionesSemana,


										 panelErroresPeriodo,
										 panelErroresRatio

									 ),

									 kpiOrigen = k,
									 etiqueta = Etiquetas.EtiquetaValor("PDA"),
									 color = -16728065)

	}

	suspend fun crearDashboardVersiones() {


		val kpiVersiones = (KpiUI(
			titulo = "Versiones",
			descripcion = "Versiones en el sistema",
			origen = "",
			sql = "SELECT DISTINCT   PROGRAM_VERSION FROM TRANSACCIONES",
			dinamico = false,
			parametros = Parametros()))
		val k = operaciones.guardarKpi(kpiVersiones)


		var listaPametrosKpi: List<Parametro> = Parametros.dameParametrosPorDefectoMobility()
		listaPametrosKpi = listOf<Parametro>(Parametro(key = "PROGRAM_VERSION", valor = "", defecto = "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.77", fijo = false))


		val kpiAdopcionVersion = KpiUI(
			titulo = "Adopcion Versión",
			descripcion = "Adopción de la version segun las transacciones realizadas",
			origen = "",
			sql = """
				SELECT 
					SUBSTR(PROGRAM_VERSION,-8),  COUNT(*)
				FROM TRANSACCIONES
				WHERE 
					PROGRAM_VERSION = '#PROGRAM_VERSION'
				GROUP BY 	SUBSTR(PROGRAM_VERSION,-8)
				UNION 
				SELECT 
					'TOTAL',  COUNT(*)
				FROM 
						TRANSACCIONES
						
					""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelAdopcion = operaciones.crearPanel(kpiAdopcionVersion,
												   true,
												   PlantillasPanel.from(PlantillasPanel.TT.SignalVertical.valor).configuracion.copy(limiteElementos = 0, valorMaximo = "0", width = "300"))


		val kpiConteoTransacciones = KpiUI(
			titulo = "Transacciones",
			descripcion = "Transacciones realiadas",
			origen = "",
			sql = """
				SELECT '1 DIA',  COUNT(*)
					FROM TRX_HOY
				WHERE PROGRAM_VERSION = '#PROGRAM_VERSION'
				GROUP BY PROGRAM_VERSION
				union 
				SELECT '7 DIAS',  COUNT(*)
									FROM TRX_7
								WHERE PROGRAM_VERSION = '#PROGRAM_VERSION'
								GROUP BY PROGRAM_VERSION
				union 
				SELECT 'PERIODO',   COUNT(*)
									FROM TRANSACCIONES
								WHERE PROGRAM_VERSION ='#PROGRAM_VERSION'
								GROUP BY PROGRAM_VERSION				
			
					""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelConteoTotal = operaciones.crearPanel(kpiConteoTransacciones,
													  true,
													  PlantillasPanel.from(PlantillasPanel.TT.IndicadorHorizontal.valor).configuracion.copy(limiteElementos = 0))


		val kpiConteoTransaccionesTipo = KpiUI(
			titulo = "Transacciones",
			descripcion = "Transacciones realizadas",
			origen = "",
			sql = """
				SELECT TIPO_MOV,  COUNT(*)
					FROM TRANSACCIONES
				WHERE PROGRAM_VERSION = '#PROGRAM_VERSION'
				GROUP BY 1						
				ORDER BY 2 
					""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelConteoTipo = operaciones.crearPanel(kpiConteoTransaccionesTipo,
													 true,
													 PlantillasPanel.from(PlantillasPanel.TT.Circular.valor).configuracion.copy(limiteElementos = 0,
																																mostrarEtiquetas = true,
																																ajustarContenidoAncho = true)
		)


		val kpiConteoTransaccionesEstado = KpiUI(
			titulo = "Transacciones",
			descripcion = "Estado Transacciones realiadas",
			origen = "",
			sql = """
				SELECT ESTADO,  COUNT(*)
					FROM TRANSACCIONES T INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE
				WHERE PROGRAM_VERSION = '#PROGRAM_VERSION'
				GROUP BY 1						
			
					""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelConteoEstado = operaciones.crearPanel(kpiConteoTransaccionesEstado,
													   true,
													   PlantillasPanel.from(PlantillasPanel.TT.PanelesHorizontales.valor).configuracion.copy(limiteElementos = 0,
																																			 mostrarEtiquetas = true,
																																			 width = "500", height = "300"))


		val condiciones: Condiciones = Condiciones(id = 1,
												   columna =
													   Columnas(nombre = "LECTORA", posicion = 0, valores = emptyList()),
												   color = 0,
												   condicionCelda = 1,
												   predicado = "",
												   descripion = "",
												   alarma = Alarmas())
		val listaCondicionesBanderas: List<Condiciones> = listOf<Condiciones>(condiciones)


		val kpiTransaccionesPorOrganizacion = KpiUI(
			titulo = "Transacciones",
			descripcion = "Estado Transacciones realiadas",
			origen = "",
			sql = """									
			
			SELECT LECTORA_ID, LECTORA_FISICA_ID, COUNT(*)
			FROM TRANSACCIONES T INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE
			WHERE PROGRAM_VERSION = '#PROGRAM_VERSION'
						GROUP BY 1 ,2
			 ORDER BY 1, 2
				
				
				
					""",


			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelTransaccionesPorOrganizacion = operaciones.crearPanel(kpiTransaccionesPorOrganizacion,
																	   true,

																	   PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(limiteElementos = 0,
																																				   mostrarEtiquetas = true,
																																				   condiciones = listaCondicionesBanderas,
																																				   width = "500", height = "300"))


		val kpiRatioErrores = (KpiUI(
			titulo = "Ratios",
			descripcion = "Ratios de errores sobre las transacciones corectas",
			origen = "",
			sql = """ 
				SELECT
					strftime('%m-%d', CREATION_DATE) AS 'Fecha',
					COUNT(*) AS 'TRX',
				
					-- Contadores
					SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS 'ERRORES',
					SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS 'OK',
				
					-- Porcentajes (redondeados a 2 decimales)
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% ERR',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% OK'
				FROM
					TRANSACCIONES
				WHERE
					PROGRAM_VERSION = '#PROGRAM_VERSION'
				GROUP BY
					strftime('%m-%d', CREATION_DATE)
				ORDER BY
					1 DESC
					
				LIMIT 15;
				;""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)


		))

		val kErroresRatio = operaciones.guardarKpi(kpiRatioErrores)


		val condicionesRatio: Condiciones = Condiciones(1, Columnas("% ERR",
																	posicion = 4,
																	valores = emptyList()),
														condicionCelda = 0,
														color = 3,
														predicado = "> 5",
														descripion = "",
														alarma = Alarmas())


		val panelErroresRatio = operaciones.crearPanel(
			kErroresRatio,
			false,
			PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor)
				.configuracion.copy(
					colores = 7,
					indicadorColor = false,
					condiciones = listOf<Condiciones>(condicionesRatio),
					ajustarContenidoAncho = false,
					width = "500", height = "600"))


		operaciones.guardarDashboard(nombre = "VERSION #PROGRAM_VERSION",
									 listOf<PanelUI>(//panelOrganizaciones,


										 panelAdopcion,
										 panelConteoTotal,
										 panelConteoTipo,
										 panelConteoEstado,
										 panelTransaccionesPorOrganizacion,
										 panelErroresRatio
									 ),

									 kpiOrigen = k,
									 etiqueta = Etiquetas.EtiquetaValor("VS"),
									 color = -5952982)

	}

	suspend fun crearDashboardErrores() {


		val kpiErroresGeneral = (KpiUI(
			titulo = "Errores DE HOY",
			descripcion = "Errores HOY",
			origen = "",
			sql = """ 
				SELECT
				 	LECTORA_FISICA_ID as 'LECTORA',
					ORGANIZATION_CODE,  
					LECTORA_ID, 
					
					strftime('%m-%d %H:%M', CREATION_DATE)  AS Fecha, 
					MOB_REQUEST_ID,NUMERO, TIPO_MOV , 
					REQ_MESSAGE  
				FROM 
					TRANSACCIONES
				WHERE 
					REQ_STATUS = 2 
					AND date(CREATION_DATE) = date('now', 'localtime') 

				ORDER BY 
					MOB_REQUEST_ID DESC """,
			dinamico = false,
			parametros = Parametros()))
		val k = operaciones.guardarKpi(kpiErroresGeneral)

		val condiciones: Condiciones = Condiciones(id = 1,
												   columna =
													   Columnas(nombre = "LECTORA", posicion = 0, valores = emptyList()),
												   color = 0,
												   condicionCelda = 1,
												   predicado = "",
												   descripion = "",
												   alarma = Alarmas())
		val listaCondicionesBanderas: List<Condiciones> = listOf<Condiciones>(condiciones)


		val panelErroresGeneral = operaciones.crearPanel(
			kpiErroresGeneral,
			true,
			PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor)
				.configuracion.copy(limiteElementos = 25,
									colores = 5,
									ajustarContenidoAncho = false,
									condicionesCeldas = listaCondicionesBanderas,
									width = "500", height = "600"))


		val kpiRatioErrores = (KpiUI(
			titulo = "Ratio",
			descripcion = "Ratio de errores sobre las transacciones corectas",
			origen = "",
			sql = """ 
				SELECT
					strftime('%m-%d', CREATION_DATE) AS 'Fecha',
					COUNT(*) AS 'TRX',
				
					-- Contadores
					SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS 'ERRORES',
					SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS 'OK',
				
					-- Porcentajes (redondeados a 2 decimales)
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% ERR',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% OK'
				FROM
					TRANSACCIONES
				GROUP BY
					strftime('%m-%d', CREATION_DATE)
				ORDER BY
					1 DESC;
				;""",
			dinamico = false,
			parametros = Parametros()))
		val kErroresRatio = operaciones.guardarKpi(kpiRatioErrores)


		val condicionesRatio: Condiciones = Condiciones(1, Columnas("% ERR",
																	posicion = 4,
																	valores = emptyList()),
														condicionCelda = 0,
														color = 3,
														predicado = "> 5",
														descripion = "",
														alarma = Alarmas())


		val panelErroresRatio = operaciones.crearPanel(
			kErroresRatio,
			false,
			PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor)
				.configuracion.copy(
					colores = 5,
					indicadorColor = false,
					condiciones = listOf<Condiciones>(condicionesRatio),
					ajustarContenidoAncho = false,
					width = "500", height = "600"))


		val panelEvolucionErrores = operaciones.crearPanel(
			kErroresRatio,
			false,
			PlantillasPanel.from(PlantillasPanel.TT.Lineas.valor)
				.configuracion.copy(limiteElementos = 10,
									mostrarTabla = false,
									ajustarContenidoAncho = false,
									columnaX = 0,
									columnaY = 2,
									width = "500", height = "300"))


		val panelEvolucionPorcentaje = operaciones.crearPanel(
			kErroresRatio,
			false,
			PlantillasPanel.from(PlantillasPanel.TT.SignalVertical.valor)
				.configuracion.copy(titulo = "Nivel de alerta", limiteElementos = 0,
									ajustarContenidoAncho = false,
									columnaX = 0,
									columnaY = 4,
									valorMaximo = "10",
									width = "500", height = "300"))


		val kpiTransaccionesSolucionadas = (KpiUI(
			titulo = "Transaccion solucionadas",
			descripcion = "Transacciones que se han solucionado tras un nuevo procesamiento",
			origen = "",
			sql = """ 
				SELECT
					t_error.ORGANIZATION_CODE         AS ORG,
					t_error.LECTORA_FISICA_ID            AS LECTORA,
					t_error.numero            AS NUMERO,
					t_error.CREATION_DATE     AS CREACION,
					t_exito.CREATION_DATE     AS OK,
					t_error.req_message     AS MENS
					
				FROM
					Transacciones AS t_error
				INNER JOIN
					Transacciones AS t_exito ON t_error.numero = t_exito.numero
				WHERE
					t_error.req_status = '2' -- La transacción original debe ser un error
					AND t_exito.req_status = '0' -- La transacción vinculada debe ser un éxito
					AND t_error.MOB_REQUEST_ID < t_exito.MOB_REQUEST_ID -- Asegura que la solución es posterior al error
				ORDER BY t_error.CREATION_DATE DESC 
				;""",
			dinamico = false,
			parametros = Parametros()))
		val kTransaccionesSolucionadas = operaciones.guardarKpi(kpiTransaccionesSolucionadas)


		/*	val condicionesRatio: Condiciones = Condiciones(1, Columnas("% ERR",
																		posicion = 4,
																		valores = emptyList()),
															condicionCelda = 0,
															color = 3,
															predicado = "> 5",
															descripion = "",
															alarma = Alarmas())
	*/

		val panelTransaccionesSolucionadas = operaciones.crearPanel(
			kTransaccionesSolucionadas,
			false,
			PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor)
				.configuracion.copy(
					colores = 5,
					indicadorColor = false,
					condiciones = listOf<Condiciones>(condicionesRatio),
					ajustarContenidoAncho = false,
					width = "500", height = "600"))

		//----------------------------------------------------------------------------------------------------
		val kpiTransaccionesPendientesSolucion = (KpiUI(
			titulo = "Transaccion pendientes  (PERIODO)",
			descripcion = "Transacciones pendientes de solucion (PERIODO)",
			origen = "",
			sql = """ 
				SELECT
					t_error.MOB_REQUEST_ID AS id_transaccion_error,
					t_error.numero AS numero_transaccion,
					t_error.CREATION_DATE AS fecha_error,
					t_error.REQ_MESSAGE AS mensaje_error
				FROM
					Transacciones AS t_error
				WHERE
					t_error.req_status = 2
					AND NOT EXISTS (
						-- Subconsulta que busca si existe alguna transacción de éxito para el mismo número
						SELECT 1
						FROM Transacciones AS t_exito
						WHERE
							t_exito.numero = t_error.numero
							AND t_exito.req_status =0
					)
					
				order by 1 desc	
				""",
			dinamico = false,
			parametros = Parametros()))
		val kTransaccionesPendientesSolucionadas = operaciones.guardarKpi(kpiTransaccionesPendientesSolucion)


		val panelTransaccionesPendientesSolucionadas = operaciones.crearPanel(
			kTransaccionesPendientesSolucionadas,
			false,
			PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor)
				.configuracion.copy(
					colores = 5,
					limiteElementos = 20,
					indicadorColor = false,
					condiciones = listOf<Condiciones>(condicionesRatio),
					ajustarContenidoAncho = false,
					width = "500", height = "600"))

		//----------------------------------------------------------------------------------------------------

		val kpiTransaccionesPendientesSolucionDIA = (KpiUI(
			titulo = "Transaccion pendientes (DIA)",
			descripcion = "Transacciones pendientes de solucion DIA",
			origen = "",
			sql = """ 
				SELECT
					t_error.MOB_REQUEST_ID AS id_transaccion_error,
					t_error.numero AS numero_transaccion,
					t_error.CREATION_DATE AS fecha_error,
					t_error.REQ_MESSAGE AS mensaje_error
				FROM
					TRX_HOY AS t_error
				WHERE
					t_error.req_status = 2
					AND NOT EXISTS (
						-- Subconsulta que busca si existe alguna transacción de éxito para el mismo número
						SELECT 1
						FROM Transacciones AS t_exito
						WHERE
							t_exito.numero = t_error.numero
							AND t_exito.req_status = 0
					)
					
				order by 1 desc	
				""",
			dinamico = false,
			parametros = Parametros()))
		val kTransaccionesPendientesSolucionadasDIA = operaciones.guardarKpi(kpiTransaccionesPendientesSolucionDIA)


		val kpiErroresOrganizacion = (KpiUI(
			titulo = "Errores por organizacion (TOP  10)",
			descripcion = "Errores por organizacines",
			origen = "",
			sql = """ 
				SELECT
				 	
					ORGANIZATION_CODE,  
					COUNT(LECTORA_ID) 
					
					/*strftime('%m-%d %H:%M', CREATION_DATE)  AS Fecha, 
					MOB_REQUEST_ID,NUMERO, TIPO_MOV , 
					REQ_MESSAGE */ 
				FROM 
					TRANSACCIONES
				WHERE 
					REQ_STATUS = 2 
					--AND date(CREATION_DATE) = date('now', 'localtime') 
				GROUP BY ORGANIZATION_CODE
				ORDER BY 
					2 DESC """,
			dinamico = false,
			parametros = Parametros()))
		val kErrorOrganizacion = operaciones.guardarKpi(kpiErroresOrganizacion)


		val panelErroresOrganizacin = operaciones.crearPanel(
			kErrorOrganizacion,
			true,
			PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor)
				.configuracion.copy(limiteElementos = 10,
									columnaX = 0,
									columnaY = 1,
									ajustarContenidoAncho = true,
									width = "500", height = "600"))


		val panelTransaccionesPendientesSolucionadasDIA = operaciones.crearPanel(
			kTransaccionesPendientesSolucionadasDIA,
			false,
			PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor)
				.configuracion.copy(
					colores = 5,

					indicadorColor = false,
					condiciones = listOf<Condiciones>(condicionesRatio),
					ajustarContenidoAncho = false,
					width = "500", height = "500"))





		operaciones.guardarDashboard(nombre = "ERRORES",
									 listOf<PanelUI>(
										 panelErroresGeneral,
										 panelEvolucionErrores,
										 panelErroresRatio,
										 panelEvolucionPorcentaje,
										 panelTransaccionesSolucionadas,
										 panelTransaccionesPendientesSolucionadasDIA,
										 panelTransaccionesPendientesSolucionadas,
										 panelErroresOrganizacin

									 ),

			// kpiOrigen = k,
									 etiqueta = Etiquetas.EtiquetaValor("ERRORES"),
									 color = -5952982)
	}

}