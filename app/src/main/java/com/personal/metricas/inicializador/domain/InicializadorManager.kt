package com.personal.metricas.inicializador.domain

import androidx.compose.ui.unit.dp
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

	}


	suspend fun start() {


		eliminarDatosGeneradosPreviamente()
		crearVistas()
		val kpiOrganizaciones = (KpiUI(
			titulo = "Organizaciones",
			descripcion = "Organizaciones en el sistema",
			origen = "",
			sql = "SELECT DISTINCT  ORGANIZATION_CODE, ORGANIZATION_ID,SELECT DISTINCT  ORGANIZATION_CODE, ORGANIZATION_ID, ORGANIZATION_NAME, MASTERORGANIZATION_ID FROM TRANSACCIONES",
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


		/*crearDashboardGeneral()
		crearDashboardInfoLectoras()*/

		crearDashboardGeneral()
		crearDashboardGeneralExtra()
		crearDashboardOrganizacion()
		crearDashboardVersiones()
		crearDashboardLectoras()


	}




	suspend fun crearDashboardGeneral() {


		val condiciones: Condiciones = Condiciones(id = 1,
												   columna = Columnas("LECTORA_FISICA_ID", 0, emptyList()),
												   color = 0,
												   condicionCelda = 1,
												   predicado = "",
												   descripion = "",
												   alarma = Alarmas())
		val listaCondicionesBanderas: List<Condiciones> = listOf<Condiciones>(condiciones)


		//transacciones diarias
		val kpiTransaccionesDiarias = KpiUI(
			titulo = "Transacciones",
			descripcion = "Transacciones realizadas hoy",
			origen = "",
			sql = """
				SELECT
					LECTORA_FISICA_ID,
					MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO,  USUARIO_LECTORA, 
					strftime('%d-%m', CREATION_DATE)  AS Fecha
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
															   PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(condicionesCeldas = listaCondicionesBanderas))


		//Errores TRX HOY
		val kpiTransaccionesDiariasError = KpiUI(
			titulo = "Errores del dia",
			descripcion = "Errores que se han producido en el día",
			origen = "",
			sql = """
				SELECT
					LECTORA_ID,
					MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO,  USUARIO_LECTORA, 
					strftime('%d-%m', CREATION_DATE)  AS Fecha
				FROM
					TRX_HOY  T
				WHERE REQ_STATUS = 2
				GROUP BY
					Fecha
				ORDER BY
					1 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		val panelTransaccionesDiariasError = operaciones.crearPanel(kpiTransaccionesDiariasError,
																	true,
																	PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(condicionesCeldas = listaCondicionesBanderas, colores = EsquemaColores.Paletas.ERRORES.valor, height = "200")
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
																																								colores = EsquemaColores.Paletas.NORMAL.valor)
		)


		// Evolucion de errores
		val kpiEvolucionErrores = KpiUI(
			titulo = "Errores diarios ",
			descripcion = "Evolucion de errores en el sistemas",
			sql = """
				SELECT
					strftime('%d-%m', CREATION_DATE) AS dia_y_mes,
					COUNT(*) AS numero_de_errores
				FROM
					TRANSACCIONES
				WHERE					
					 REQ_STATUS = 2
				GROUP BY
					dia_y_mes
				ORDER BY 1 ASC
					;
			""".trimIndent(),
			origen = "",
			dinamico = true,
			parametros = Parametros()
		)
		val panelEvolucionErrores = operaciones.crearPanel(kpiEvolucionErrores,
														   true,
														   PlantillasPanel.from(PlantillasPanel.TT.Lineas.valor).configuracion.copy(ajustarContenidoAncho = false,
																																	colores = EsquemaColores.Paletas.PERS.valor)
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


		val panelTransaccionesUltimoDia = operaciones.crearPanel(kpiEstadoTransaccionesUltimoDia, true, PlantillasPanel.from(PlantillasPanel.TT.Circular.valor).configuracion)
		val panelFragmentacion = operaciones.crearPanel(kpiVersionesUltimoDia, true, PlantillasPanel.from(PlantillasPanel.TT.Anillo.valor).configuracion)
		val panelHoras = operaciones.crearPanel(kpiHorasTransacciones, true, PanelConfiguracion().copy(tipo = PanelTipoGrafica.BarrasFinasVerticales()))
		val panelErroresDiarios = operaciones.crearPanel(kpiErroresDiarios, true, PlantillasPanel.from(PlantillasPanel.TT.Lineas.valor).configuracion)
		val panelTransaccionesLectoras = operaciones.crearPanel(kpiTransaccionesLectoras, true, PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion)
		val panelUltimaTransaccionRalizada = operaciones.crearPanel(kpiUltimaTransaccionRealizada, true, PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion)


		val dh = operaciones.guardarDashboard(nombre = "General",
											  listOf<PanelUI>(
												  panelTransaccionesDiarias,
												  panelTransaccionesDiariasError,
												  panelTransaccionesPorOrganizacion,
												  panelEvolucionErrores
												  /* panelTransaccionesUltimoDia,
												   panelFragmentacion,
												   panelHoras,
												   panelErroresDiarios,
												   panelTransaccionesLectoras,
												   panelUltimaTransaccionRalizada*/
											  ),
											  etiqueta = Etiquetas.EtiquetaValor("General"),
											  home = true
		)


	}

	suspend fun crearDashboardGeneralExtra() {


		val condiciones: Condiciones = Condiciones(id = 1,
												   columna = Columnas("LECTORA_FISICA_ID", 0, emptyList()),
												   color = 0,
												   condicionCelda = 1,
												   predicado = "",
												   descripion = "",
												   alarma = Alarmas())
		val listaCondicionesBanderas: List<Condiciones> = listOf<Condiciones>(condiciones)


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
					1 DESC				
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		val panelTransaccionesEmpleo = operaciones.crearPanel(kpiTranasccionesEmpleo,
															  true,
															  PlantillasPanel.from(PlantillasPanel.TT.Circular.valor).configuracion)


		//transacciones diarias
		val kpiTranasccionesPorLectora = KpiUI(
			titulo = "Transacciones por lectora",
			descripcion = "Indicador de las lectoreas que más trabajo hacen",
			origen = "",
			sql = """
				SELECT
					LECTORA_FISICA_ID,
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
															   PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion.copy(limiteElementos = 25))


		//transacciones diarias
		val kpiTranasccionesErrorPorLectora = KpiUI(
			titulo = "Errores por lectora",
			descripcion = "Indicador de errores que se proiducen por lectora",
			origen = "",
			sql = """
				SELECT
					LECTORA_FISICA_ID AS 'LECT',
					  
					COUNT(*) AS 'NUM' 
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
			descripcion = "Estimación de las trnsacciones realizadas por horas",
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
					1;
			""".trimIndent(),
			dinamico = true,
			parametros = Parametros()
		)
		val panelHoras = operaciones.crearPanel(kpiHorasTransacciones, true, PanelConfiguracion().copy(tipo =
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
											  home = true
		)


	}

	suspend fun crearDashboardOrganizacion() {


		val kpiOrganizaciones = (KpiUI(
			titulo = "Organizaciones",
			descripcion = "Organizaciones en el sistema",
			origen = "",
			sql = "SELECT DISTINCT  ORGANIZATION_CODE, ORGANIZATION_ID, ORGANIZATION_NAME, MASTERORGANIZATION_ID FROM TRANSACCIONES",
			dinamico = false,
			parametros = Parametros()))
		val k = operaciones.guardarKpi(kpiOrganizaciones)

		//endpoint de recarga de transacciones
		var listaPametrosEP: List<Parametro> = Parametros.dameParametrosPorDefectoMobility()
		listaPametrosEP = listaPametrosEP.plus(Parametro("P_ORGANIZATION_ID", "#ORGANIZATION_ID", "", false))
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
		listaPametrosKpi = listOf<Parametro>(Parametro(key = "ORGANIZATION_ID", valor = "", defecto = "2206", fijo = false))


		val kpiConteoTransacciones = KpiUI(
			titulo = "DIA",
			descripcion = "Transacciones realiadas",
			origen = "",
			sql = """
				SELECT TIPO_MOV, COUNT(*) FROM   TRX_HOY  WHERE REQ_STATUS = 0  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY TIPO_MOV
				UNION 
				SELECT 'ERROR', COUNT(*) FROM   TRX_HOY  WHERE REQ_STATUS = 2  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY TIPO_MOV
				UNION 
				SELECT 'REPROCESAMIENTO ', COUNT(*) FROM   TRX_HOY  WHERE REQ_STATUS = 4  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY TIPO_MOV""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelConteo = operaciones.crearPanel(kpiConteoTransacciones,
												 true,
												 PlantillasPanel.from(PlantillasPanel.TT.IndicadorHorizontal.valor).configuracion.copy(limiteElementos = 0))

		//trasnacciones ultimo dia
		val kpiTransaccionesUltimoDiaOrganizacion = KpiUI(
			titulo = "Hoy",
			descripcion = "Tranasacciones realizadas en el día de hoy ",
			origen = "",
			sql = "SELECT MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO, LECTORA_ID, USUARIO_LECTORA FROM TRX_HOY WHERE  ORGANIZATION_ID = '#ORGANIZATION_ID'",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelTransaccionesUltimoDia = operaciones.crearPanel(kpiTransaccionesUltimoDiaOrganizacion,
																 true,
																 PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(ajustarContenidoAncho = false))

		//errores que se han producido en el ultimo día
		val erroresDia = KpiUI(
			titulo = "Errores en el día",
			descripcion = "Tranasacciones realizadas en el día de hoy ",
			origen = "",
			sql = "SELECT MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO, LECTORA_ID, USUARIO_LECTORA FROM TRX_HOY WHERE  ORGANIZATION_ID = '#ORGANIZATION_ID' AND REQ_STATUS = '2'",
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
				SELECT TIPO_MOV, COUNT(*) FROM   TRX_7  WHERE REQ_STATUS = 0  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY TIPO_MOV
				UNION 
				SELECT '. ERROR', COUNT(*) FROM   TRX_7  WHERE REQ_STATUS = 2  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY TIPO_MOV
				UNION 
				SELECT '. REPROCESAMIENTO ', COUNT(*) FROM   TRX_7  WHERE REQ_STATUS = 4  AND  ORGANIZATION_ID = '#ORGANIZATION_ID' GROUP BY TIPO_MOV
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
			sql = "SELECT strftime('%d-%m', CREATION_DATE) AS DIA, MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO,  REQ_STATUS, LECTORA_ID, USUARIO_LECTORA FROM TRX_7 WHERE  ORGANIZATION_ID = '#ORGANIZATION_ID'",
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
			titulo = "Errores ",
			descripcion = "Errores registrados en el perido indicado",
			origen = "",
			sql = """
				SELECT
					strftime('%d-%m', CREATION_DATE) AS dia_y_mes,
					COUNT(*) AS numero_de_errores
				FROM
					TRANSACCIONES
				WHERE
					ORGANIZATION_ID = '#ORGANIZATION_ID' 
					AND REQ_STATUS = 2
				GROUP BY
					dia_y_mes;
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


		operaciones.guardarDashboard(nombre = "ORG #ORGANIZATION_ID #ORGANIZATION_CODE \n #ORGANIZATION_NAME",
									 listOf<PanelUI>(//panelOrganizaciones,

										 panelEndPointSolicitudes,

										 panelConteo,
										 panelTransaccionesUltimoDia,
										 panelErroresDia,

										 panelConteoSemana,
										 panelTransaccionesSemana,


										 panelErroresPeriodo,

										 /*panelTransaccionesOrganizacion,
										 panelEstadoTransacciones,
										 panelOrigenErrores,*/
										 panelErroresLectora

									 ),

									 kpiOrigen = k,
									 etiqueta = Etiquetas.EtiquetaValor("ORGS"))

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
				SELECT 'REPROCESAMIENTO ', COUNT(*) FROM   TRX_HOY  WHERE REQ_STATUS = 4  AND  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' GROUP BY TIPO_MOV""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelConteo = operaciones.crearPanel(kpiConteoTransacciones,
												 true,
												 PlantillasPanel.from(PlantillasPanel.TT.IndicadorHorizontal.valor).configuracion.copy(limiteElementos = 0))

		//trasnacciones ultimo dia
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
																 PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(ajustarContenidoAncho = false))

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
			sql = "SELECT strftime('%d-%m', CREATION_DATE) AS DIA,  MOB_REQUEST_ID, TIPO_MOV, NUMERO, ESTADO,  REQ_STATUS, LECTORA_ID, USUARIO_LECTORA FROM TRX_7 WHERE  LECTORA_FISICA_ID = '#LECTORA_FISICA_ID'",
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
			titulo = "Errores ",
			descripcion = "Errores registrados en el perido indicado",
			origen = "",
			sql = """
				SELECT
					strftime('%d-%m', CREATION_DATE) AS dia_y_mes,
					COUNT(*) AS numero_de_errores
				FROM
					TRANSACCIONES
				WHERE
					LECTORA_FISICA_ID = '#LECTORA_FISICA_ID' 
					AND REQ_STATUS = 2
				GROUP BY
					dia_y_mes;
				""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelErroresPeriodo = operaciones.crearPanel(kpiErroresPeriodo,
														 true,
														 PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion.copy(colores = EsquemaColores.Paletas.ERRORES.valor, limiteElementos = 0, mostrarEtiquetas = true, ajustarContenidoAncho = true))







		operaciones.guardarDashboard(nombre = "#LECTORA_FISICA_ID",
									 listOf<PanelUI>(//panelOrganizaciones,

										 panelEndPointSolicitudes,

										 panelConteo,
										 panelTransaccionesUltimoDia,
										 panelErroresDia,

										 panelConteoSemana,
										 panelTransaccionesSemana,


										 panelErroresPeriodo,


									 ),

									 kpiOrigen = k,
									 etiqueta = Etiquetas.EtiquetaValor("PDA"))

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
			descripcion = "Transacciones realiadas",
			origen = "",
			sql = """
				SELECT TIPO_MOV,  COUNT(*)
					FROM TRANSACCIONES
				WHERE PROGRAM_VERSION = '#PROGRAM_VERSION'
				GROUP BY 1						
			
					""",
			dinamico = true,
			parametros = Parametros(ps = listaPametrosKpi)
		)
		val panelConteoTipo = operaciones.crearPanel(kpiConteoTransaccionesTipo,
													  true,
													  PlantillasPanel.from(PlantillasPanel.TT.Anillo.valor).configuracion.copy(limiteElementos = 0,
																															   mostrarEtiquetas = true))





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
																																		   width = "150"))




		operaciones.guardarDashboard(nombre = "VERSION #PROGRAM_VERSION",
									 listOf<PanelUI>(//panelOrganizaciones,


										 panelAdopcion,
										 panelConteoTotal,
										 panelConteoTipo,
										 panelConteoEstado




									 ),

									 kpiOrigen = k,
									 etiqueta = Etiquetas.EtiquetaValor("VS"))

	}
}