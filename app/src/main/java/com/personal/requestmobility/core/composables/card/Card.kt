package com.personal.requestmobility.core.composables.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Test_MA_Card(){

    MA_Card {
        Column {
            Text("pruebas")
            Text("pruebas1")
            Text("pruebas2")
            Text("pruebas3")

        }


    }
}
@Composable
fun MA_Card(contenido: @Composable () -> Unit) {
    Card(

       /* elevation = CardDefaults.cardElevation(
            defaultElevation =3.dp
        ),*/
        modifier = Modifier.
            background(color = Color(10,10,10,10))
            .fillMaxWidth(),
    ) {
        Box(Modifier.padding(10.dp)){
            contenido()
        }

    }


}