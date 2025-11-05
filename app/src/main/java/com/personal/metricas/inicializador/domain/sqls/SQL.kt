package com.personal.metricas.inicializador.domain.sqls

import com.personal.metricas.App
import com.personal.metricas.core.utils.K
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.String

object SQL {


	val RATIO_OK_ERROR: String = """				
		SELECT
					strftime('%m-%d', CREATION_DATE) AS 'Fecha',
					COUNT(*) AS 'TRX',
				
					
					SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS 'ERRORES',
					SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS 'OK',
				
					
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% ERR',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% OK'
				FROM
					TRANSACCIONES
			
				GROUP BY
					strftime('%m-%d', CREATION_DATE)
				ORDER BY
					1 DESC
					
				LIMIT 30""".trimIndent()


	val RATIO_RESULTADO_TRX: String = """				
						SELECT
					strftime('%m-%d', CREATION_DATE) AS 'Fecha',
					COUNT(*) AS 'TRX',
				
					-- Contadores
					SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS 'ERRORES',
					SUM(CASE WHEN REQ_STATUS = 4 THEN 1 ELSE 0 END) AS 'REPRO',
					SUM(CASE WHEN REQ_STATUS = 3 THEN 1 ELSE 0 END) AS 'REPRO ORACLE',
					SUM(CASE WHEN REQ_STATUS = 1 THEN 1 ELSE 0 END) AS 'Desktop',
					SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS 'OK',
				
					-- Porcentajes (redondeados a 2 decimales)
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% ERR',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 4 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% REPRO',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 3 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% REPRO ORACLE',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 1 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% Desktop',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% OK'
				FROM
					TRANSACCIONES
				
				GROUP BY
					strftime('%m-%d', CREATION_DATE)
				ORDER BY
					1 DESC
					
				LIMIT 30
		""".trimIndent()


	val INFO_TRANSACCIONES_HOY: String = """
			SELECT
				strftime('%m-%d %H:%M', CREATION_DATE)  AS Fecha, 
				ORGANIZATION_CODE, 
				LECTORA_FISICA_ID as 'LECTORA',
				MOB_REQUEST_ID, TIPO_MOV, NUMERO, 
				ESTADO,
				REQ_MESSAGE, 
				USUARIO_LECTORA 
			FROM
				TRANSACCIONES  T LEFT JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE 
				
				WHERE 
					strftime('%Y-%m-%d', CREATION_DATE) = '${App.sharedPrerfences.get<String>(K.DIA, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))}'
			ORDER BY
				FECHA DESC	
	""".trimIndent()


	val INFO_TRANSACCIONES_ERROR_HOY: String = """
			SELECT
				strftime('%m-%d %H:%M', CREATION_DATE)  AS Fecha, 
				ORGANIZATION_CODE, 
				LECTORA_FISICA_ID as 'LECTORA',
				MOB_REQUEST_ID, TIPO_MOV, NUMERO, 
				ESTADO,
				REQ_MESSAGE, 
				USUARIO_LECTORA 
			FROM
				TRANSACCIONES  T LEFT JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE 
				
				WHERE 
					REQ_STATUS ='2' AND
					strftime('%Y-%m-%d', CREATION_DATE) = '${App.sharedPrerfences.get<String>(K.DIA, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))}'
			ORDER BY
				FECHA DESC	
	""".trimIndent()

	val INFO_TRANSACCIONES_ERROR: String = """
			SELECT
				strftime('%m-%d %H:%M', CREATION_DATE)  AS Fecha, 
				ORGANIZATION_CODE, 
				LECTORA_FISICA_ID as 'LECTORA',
				MOB_REQUEST_ID, TIPO_MOV, NUMERO, 
				ESTADO,
				REQ_MESSAGE, 
				USUARIO_LECTORA 
			FROM
				TRANSACCIONES  T LEFT JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE 
				
				WHERE 
					REQ_STATUS ='2'					
			ORDER BY
				FECHA DESC	
	""".trimIndent()

	val INFO_TRANSACCIONES_HISTORICO: String = """
			SELECT 
				strftime('%m-%d %H:%M', CREATION_DATE)  AS Fecha, 
				ORGANIZATION_CODE, 
				LECTORA_FISICA_ID as 'LECTORA',
				MOB_REQUEST_ID, TIPO_MOV, NUMERO, 
				ESTADO,
				REQ_MESSAGE, 
				USUARIO_LECTORA 
			FROM
				TRANSACCIONES  T
				LEFT  JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE
			ORDER BY
				FECHA DESC
					
			LIMIT 200
	""".trimIndent()


	val CONTEO_TRANSACCIONES: String = """		
			SELECT
					strftime('%m-%d', CREATION_DATE)  AS Fecha, 
					COUNT(*) AS TRX
									
			FROM
					TRANSACCIONES
				GROUP BY strftime('%m-%d', CREATION_DATE) 
				
				ORDER BY				
				strftime('%m-%d', CREATION_DATE)  DESC	
	""".trimIndent()


	val TRANSACCIONES_POR_HORAS: String = """		
			SELECT
					strftime('%H', CREATION_DATE)  AS Fecha, 
					COUNT(*) AS TRX
									
			FROM
					TRANSACCIONES
				GROUP BY strftime('%H', CREATION_DATE) 
				
				ORDER BY				
				1 asc 
	""".trimIndent()


	val ORGANIZACIONES_TRANSACCIONES = """
		SELECT DISTINCT
					ORGANIZATION_CODE,
					ORGANIZATION_ID,
					ORGANIZATION_NAME,
					MASTERORGANIZATION_ID 
			   FROM
				TRANSACCIONES
				ORDER BY ORGANIZATION_CODE
		
	""".trimIndent()


	val TIPOS_TRANSACCIONES_OK = """
		SELECT TIPO_MOV  ,
		 COUNT(* ) 
		 FROM TRANSACCIONES
		  WHERE REQ_STATUS = '0' 
		  GROUP BY 1 
		  ORDER BY 2  DESC
		
	""".trimIndent()

	val TIPOS_TRANSACCIONES_ERROR = """
		SELECT
    TIPO_MOV,
    SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS numero_de_errores,
    (SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) * 100.0) / COUNT(*) AS porcentaje_errores,
    COUNT(*) AS total_transacciones
FROM
    TRANSACCIONES
GROUP BY
    TIPO_MOV
ORDER BY
    porcentaje_errores DESC,
    numero_de_errores DESC;
		
	""".trimIndent()

	val CONTEO_ESTADO_TRNSACCIONES = """
		SELECT ET.ESTADO  , COUNT(* ) 
		FROM TRANSACCIONES  T 
		INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE  
		GROUP BY ET.ESTADO  
		ORDER BY T.REQ_STATUS
		
	""".trimIndent()





	val CONTEO_TRANSACCIONES_POR_ORGANIZACION = """
		 SELECT ORGANIZATION_CODE, ORGANIZATION_NAME,  COUNT(* ) 
		 FROM TRANSACCIONES  T 
		 GROUP BY ORGANIZATION_CODE, ORGANIZATION_NAME  ORDER BY ORGANIZATION_CODE ASC		
	""".trimIndent()

	val CONTEO_TRANSACCIONES_POR_LECTORA = """
		 SELECT LECTORA_FISICA_ID, COUNT(* ) 
		 FROM TRANSACCIONES  T 
		 INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE 
		  GROUP BY LECTORA_FISICA_ID  ORDER BY T.REQ_STATUS
		
	""".trimIndent()


	val LECTORAS_TRANSACCIONES = """
		 SELECT DISTINCT  LECTORA_FISICA_ID ,
		  ORGANIZATION_CODE,
		   ORGANIZATION_NAME 
		   FROM TRANSACCIONES ORDER BY LECTORA_FISICA_ID ASC
		
	""".trimIndent()


	val LOG_SINCRONZIACIONES = """
		 SELECT  ORGANIZATION_CODE AS ORG, HORA, TIPO , TRX 
		   FROM LOGS 
		   ORDER BY ID DESC
		
	""".trimIndent()


	val LOG_CONTEO_SINCRONZIACIONES = """
	SELECT
  TIPO,
  COUNT(1) AS Cantidad,
  ROUND((COUNT(1) * 100.0 / SUM(COUNT(1)) OVER ()),2) AS Porcentaje
FROM
  LOGS
GROUP BY
  TIPO
		
	""".trimIndent()

	val CONTEO_TRX_VERSION = """
SELECT
    CASE
        WHEN INSTR(PROGRAM_VERSION, 'APK:') > 0
        THEN SUBSTR(PROGRAM_VERSION, INSTR(PROGRAM_VERSION, 'APK: ') + 5)
        ELSE PROGRAM_VERSION
    END AS VERSION,
    
    COUNT(1) AS TRX,
    
   
    ROUND((COUNT(1) * 100.0 / SUM(COUNT(1)) OVER ()), 2) AS PERCENTAGE

FROM TRANSACCIONES
GROUP BY 1 
ORDER BY 2 DESC
	
	""".trimIndent()

val ERRORES_POR_VERSION = """
	SELECT
    PROGRAM_VERSION,
    COUNT(*) AS total_transacciones,
    SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS numero_de_errores,
    (SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) * 100.0) / COUNT(*) AS porcentaje_errores
FROM
    TRANSACCIONES
GROUP BY
    PROGRAM_VERSION
ORDER BY
    porcentaje_errores DESC;
""".trimIndent()


	val TASA_ERROR_USUARIO = """
		
		SELECT
    USUARIO_LECTORA,
    (SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) * 100.0) / COUNT(*) AS porcentaje_errores,
    COUNT(*) AS total_transacciones
FROM
    TRANSACCIONES
GROUP BY
    USUARIO_LECTORA
HAVING
    COUNT(*) > 10 -- Opcional: Filtra usuarios con pocas transacciones
ORDER BY
    porcentaje_errores DESC
	LIMIT 15
	""".trimIndent()

	val ERRORES_DISPOSITIVO_FISICO = """ SELECT
    LECTORA_FISICA_ID,
    COUNT(*) AS total_errores
FROM
    TRANSACCIONES
WHERE
    REQ_STATUS = 2
GROUP BY
    LECTORA_FISICA_ID
ORDER BY
    total_errores DESC
LIMIT 10
"""
}

