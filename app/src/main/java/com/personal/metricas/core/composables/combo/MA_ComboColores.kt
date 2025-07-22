package com.personal.metricas.core.composables.combo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.labels.MA_LabelEtiqueta
import com.personal.metricas.core.composables.layouts.MA_Box
import com.personal.metricas.core.composables.modales.MA_BottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MA_ComboColores(modifier: Modifier = Modifier,
                      titulo: String = "[TITULO]",
                      descripcion: String = "Seleccione un opcion correspondiente",
                      valorInicial: @Composable () -> Unit,
                      elementosSeleccionables: List<T>,
                      item: @Composable (T) -> Unit,
                      onClickSeleccion: (T) -> Unit) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope() // Se mantiene dentro del componente
    var elemntoSeleccionado by remember { mutableStateOf(valorInicial) }



    MA_Box(modifier = Modifier.clickable(enabled = true, onClick = { scope.launch { sheetState.show() } })) {
        Column {

            MA_LabelEtiqueta(titulo)
            Row {
                elemntoSeleccionado()
            }
        }
    }


    MA_BottomSheet(
        sheetState,
        onClose = {
            { scope.launch { sheetState.hide() } }
        },
        contenido = {
            Column(
                Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = titulo,
                    style = MaterialTheme.typography.headlineLarge,
                )


                Text(
                    modifier = Modifier.padding(10.dp),
                    color = Color.Gray,
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic
                )


                if (elementosSeleccionables.isNotEmpty()) {



                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4), // Define exactamente 2 columnas
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp), // Espaciado alrededor de la cuadrícula
                        verticalArrangement = Arrangement.spacedBy(8.dp), // Espaciado vertical entre items
                        horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado horizontal entre items
                    ) {

                        items(items = elementosSeleccionables) { ds ->
                            // contenido(ds)
                            Box(modifier = Modifier.clickable(enabled = true, onClick = {
                                onClickSeleccion(ds)
                                elemntoSeleccionado = { item(ds) }
                                //valorInicial = valorInicial(el)
                                scope.launch { sheetState.hide() }
                            })) {
                                item(ds)
                            }
                        }


                    }

                }
            }

        }
    )
}


