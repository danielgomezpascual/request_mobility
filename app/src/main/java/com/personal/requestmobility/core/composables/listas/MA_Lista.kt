package com.personal.requestmobility.core.composables.listas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.imagenes.MA_Icono
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenDrawable
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.utils._t

@Composable
fun <T> MA_Lista(data: List<T>, itemContent: @Composable (T) -> Unit) {
   // val its = remember { mutableStateOf(data) }

    if (data.isEmpty()){
       MA_NoData()


    }else {
        LazyColumn(modifier = Modifier.Companion.fillMaxWidth()) {


            items(items = data) { elememto ->
                itemContent(elememto)
            }
        }
    }
}