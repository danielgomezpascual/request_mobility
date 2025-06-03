package com.personal.requestmobility.kpi.ui.composables


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TableView
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenDrawable
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.transacciones.ui.screens.composables.ModalInferiorFiltros

@Composable
fun KpiComboItem(kpiUI: KpiUI) {

    Column {


        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {

            //  MA_ImagenDrawable(imagen = R.drawable.database, s = 26.dp)
            MA_Avatar(kpiUI.titulo)
            Spacer(modifier = Modifier.padding(4.dp))
            Column {


                Text(
                    text = "${kpiUI.titulo}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Companion.Bold
                )
                Text(
                    text = "${kpiUI.descripcion} ",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Companion.Normal
                )


            }
        }


    }
}