package com.personal.metricas.excel


import androidx.compose.ui.graphics.Color
import com.personal.metricas.App
import com.personal.metricas.App.Companion.context
import com.personal.metricas.core.composables.tabla.Fila
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFFont

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream


class GenerateExcel {


	fun generate(titulo: String = "Sin titulo", filas: List<Fila>, fichero: File ) {
		val workbook = XSSFWorkbook()

		val sheet = workbook.createSheet("Hoja 1")

		var desplazamientoFila = 3
		var desplazamientoCelda = 1


		//TITULO
		// 1. Crea la fila y la celda donde comenzará la combinación (la de la esquina superior izquierda)
		val rowtTitulo = sheet.createRow(desplazamientoFila)
		val cell = rowtTitulo.createCell(desplazamientoCelda)
		cell.setCellValue(titulo)

		// 2. Define el área a combinar: desde la fila 0, columna 0 hasta la fila 0, columna 2
		// Esto combina las celdas A1, B1 y C1.
		val region = CellRangeAddress(
			desplazamientoFila, // primera fila
			desplazamientoFila, // última fila (la misma porque es una combinación horizontal)
			desplazamientoCelda, // primera columna
			desplazamientoCelda + filas.first().celdas.size  // última columna (A=0, B=1, C=2)
		)
		// 3. Aplica la combinación a la hoja
		sheet.addMergedRegion(region)

		val fontTitulo = workbook.createFont().apply {
			fontName = "Fira" // Definimos el tipo de fuente
			fontHeightInPoints = 18.toShort()
			bold = false
			color = IndexedColors.BLACK.index
		}
		// Opcional: Para que quede bien, es buena idea crear un estilo para centrar el texto
		val style = workbook.createCellStyle().apply {
			setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER)
			setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER)
			setFont(fontTitulo)
		}
		cell.cellStyle = style



		desplazamientoFila = desplazamientoFila + 3
		//TITULOS DE TABLA
		// 1. Creamos la fuente que vamos a customizar
		val font = workbook.createFont().apply {
			fontName = "Fira" // Definimos el tipo de fuente
			fontHeightInPoints = 12.toShort()
			bold = false
			color = IndexedColors.WHITE.index
		}


		val xssfColor = XSSFColor(IndexedColors.WHITE, null)
		//(font as XSSFFont).setColor(xssfColor)


		val estiloCabecera = workbook.createCellStyle().apply {
			setFont(font)
		}
		estiloCabecera.setFillForegroundColor(XSSFColor(IndexedColors.GREY_50_PERCENT, null))
		estiloCabecera.fillPattern = FillPatternType.SOLID_FOREGROUND



		val row = sheet.createRow(0 + desplazamientoFila)
		filas.first().celdas.dropLast(1).forEachIndexed { indexCelda, celda ->
			val cell = row.createCell(indexCelda + desplazamientoCelda)
			cell.setCellValue(celda.titulo)
			cell.cellStyle = estiloCabecera
		}


		desplazamientoFila = desplazamientoFila + 1

		//FILAS
		filas.forEachIndexed { indexFila, fila ->

			val row = sheet.createRow(indexFila + desplazamientoFila)
			fila.celdas.dropLast(1).forEachIndexed { indexCelda, celda ->
				val cell = row.createCell(indexCelda + desplazamientoCelda)
				cell.setCellValue(celda.valor)
				cell.applyBackgroundColorFromHex(ColorManager.toPastelColor(ColorManager.toHexCode(fila.color), 0.9f))
			}
		}


		//filas.first().celdas.forEachIndexed { index, celda -> sheet.autoSizeColumnAndroid(desplazamientoCelda + index, maxContentLength = 250)   }



		// 5. Guardar el libro de trabajo en un archivo
		try {


			val fileOut = FileOutputStream(fichero.absolutePath)
			workbook.write(fileOut)
			fileOut.close()
			workbook.close()
			println("Archivo 'ejemplo.xlsx' creado con éxito. ✅")
		}
		catch (e: Exception) {
			e.printStackTrace()
		}
	}
}
/*fun generar(filas: List<Fila>) {

	val appSpecificDir: File? = context.getExternalFilesDir(null)
	val myExcelFile = File(appSpecificDir, "test.xls")


	val w = workbook {

		val corporateBlue = Color(239, 154, 154, 255) // Un azul oscuro

		// b) Crear el objeto XSSFColor a partir de los bytes del color.
		val rgbBytes = byteArrayOf(corporateBlue.red.toInt().toByte(), corporateBlue.green.toInt().toByte(), corporateBlue.blue.toInt().toByte())

		val blueColor = XSSFColor(rgbBytes, null)

		// c) Crear un estilo de celda y aplicar el color.
		val blueStyle = createCellStyle() as XSSFCellStyle
		blueStyle.fillPattern = FillPatternType.SOLID_FOREGROUND
		blueStyle.setFillForegroundColor(blueColor) // ¡Aquí se usa!


		val s = sheet("Test") {

			row {
				cell("Informe de Ventas Trimestral")
				// Dejamos las otras celdas de esta fila vacías
				cell("")
				cell("")
			}

			filas.take(1).forEach { fila ->
				row(dameEstiloTitulo()) {
					fila.celdas.forEach { celda -> cell(content = celda.titulo) }

				}
			}


			filas.forEach { fila ->
				row {
					fila.celdas.forEach { celda ->
						//	cell(content =  celda.valor, style = this@sheet.dameEstilo(fila.color))
						//cell(content = celda.valor, style = this@sheet.dameEstilo(fila.color))
						cell(content = celda.valor, style =createCellStyle {
							val colorPastel = Color.Red.toPastel(0.6f)

							//val pastelBlueHex = "#80%02X%02X%02X".format(colorPastel.red, colorPastel.green, colorPastel.blue)

							val redRgbBytes = byteArrayOf(
								colorPastel.toArgb().red.toInt().toByte(),
								colorPastel.toArgb().green.toInt().toByte(),
								colorPastel.toArgb().blue.toInt().toByte())

							val xssfColor = XSSFColor(redRgbBytes, null)
							// 4. Aplicamos el color al estilo
							setFillForegroundColor(xssfColor)
setFillBackgroundColor(xssfColor)

						})
					}
				}


			}


		}

		val rangoParaCombinar = CellRangeAddress(0, 0, 0, 2)

		s.xssfSheet.addMergedRegion(rangoParaCombinar)


		// 2. 💡 AQUÍ ESTÁ LA SOLUCIÓN
		// Determinas cuántas columnas tienes (usando la primera fila como referencia)
		val numColumnas = filas.firstOrNull()?.celdas?.size ?: 0


		/*(0..numColumnas).forEach {  columna ->

			try{
				/*s.xssfSheet.columnHelper.setColBestFit(columna.toLong(), true)
				s.xssfSheet.columnHelper.setCustomWidth(columna.toLong(), true)*/
//					s.xssfSheet.columnHelper.setColWidth(columna.toLong(), 200.toDouble())
			}catch (e: Exception){
				App.log.e(e.toString())
			}


		}*/


	}.write(myExcelFile.absolutePath)



	App.log.d(myExcelFile.absolutePath)
}*/
