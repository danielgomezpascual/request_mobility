package com.personal.metricas.inicializador.domain.sqls

object SQL {


	val RATIO_OK_ERROR : String = """				
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


	val RATIO_RESULTADO_TRX : String = """				
						SELECT
					strftime('%m-%d', CREATION_DATE) AS 'Fecha',
					COUNT(*) AS 'TRX',
				
					-- Contadores
					SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS 'ERRORES',
					SUM(CASE WHEN REQ_STATUS = 4 THEN 1 ELSE 0 END) AS 'REPRO',
					SUM(CASE WHEN REQ_STATUS = 3 THEN 1 ELSE 0 END) AS 'REPRO ORACLE',
					SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS 'OK',
				
					-- Porcentajes (redondeados a 2 decimales)
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 2 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% ERR',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 4 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% REPRO',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 3 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% REPRO ORACLE',
					ROUND(CAST(SUM(CASE WHEN REQ_STATUS = 0 THEN 1 ELSE 0 END) AS REAL) * 100 / COUNT(*), 2) AS '% OK'
				FROM
					TRANSACCIONES
				WHERE
					ORGANIZATION_ID = '851'
				GROUP BY
					strftime('%m-%d', CREATION_DATE)
				ORDER BY
					1 DESC
					
				LIMIT 30
		""".trimIndent()


	val INFO_TRANSACCIONES_HOY : String = """
			SELECT
				strftime('%m-%d %H:%M', CREATION_DATE)  AS Fecha, 
				ORGANIZATION_CODE, 
				LECTORA_FISICA_ID as 'LECTORA',
				MOB_REQUEST_ID, TIPO_MOV, NUMERO, 
				ESTADO,
				REQ_MESSAGE, 
				USUARIO_LECTORA 
			FROM
				TRX_HOY  T
			ORDER BY
				FECHA DESC	
	""".trimIndent()

	val INFO_TRANSACCIONES_HISTORICO : String = """
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
					
			LIMIT 100
	""".trimIndent()


	val CONTEO_TRANSACCIONES : String = """		
			SELECT
					strftime('%m-%d', CREATION_DATE)  AS Fecha, 
					COUNT(*) AS TRX
									
			FROM
					TRANSACCIONES
				GROUP BY strftime('%m-%d', CREATION_DATE) 
				
				ORDER BY				
				strftime('%m-%d', CREATION_DATE)  DESC	
	""".trimIndent()


	val ORGANIZACIONES_TRANSACCIONES = """
		SELECT DISTINCT
					ORGANIZATION_CODE,
					ORGANIZATION_ID,
					ORGANIZATION_NAME,
					MASTERORGANIZATION_ID 
			   FROM
				TRANSACCIONES
		
	""".trimIndent()
}

